package com.contasdacasa.divida;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.contasdacasa.TestcontainersConfiguration;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class DividaApiTest {

    @Autowired MockMvc mockMvc;

    @Test
    void listaDividaPendenteComoDevedorDeUmMorador() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participanteId = adicionarMorador(casaId, "Bruno");
        criarDespesa(casaId, "100.00", pagadorId, List.of(participanteId));

        mockMvc.perform(get("/casas/" + casaId + "/moradores/" + participanteId + "/dividas-a-pagar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dividaResponseList.length()").value(1))
                .andExpect(
                        jsonPath("$._embedded.dividaResponseList[0].participanteId")
                                .value(participanteId))
                .andExpect(jsonPath("$._embedded.dividaResponseList[0].pagadorId").value(pagadorId))
                .andExpect(jsonPath("$._embedded.dividaResponseList[0].valor").value(100.00))
                .andExpect(jsonPath("$._embedded.dividaResponseList[0].saldo").value(100.00))
                .andExpect(jsonPath("$._embedded.dividaResponseList[0].pendente").value(true));
    }

    @Test
    void listaDividaPendenteComoCredorDeUmMorador() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participanteId = adicionarMorador(casaId, "Bruno");
        criarDespesa(casaId, "100.00", pagadorId, List.of(participanteId));

        mockMvc.perform(get("/casas/" + casaId + "/moradores/" + pagadorId + "/dividas-a-receber"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.dividaResponseList.length()").value(1))
                .andExpect(
                        jsonPath("$._embedded.dividaResponseList[0].participanteId")
                                .value(participanteId));
    }

    @Test
    void moradorSemDividaPendenteRecebeListaVazia() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String moradorId = adicionarMorador(casaId, "Ana");

        mockMvc.perform(get("/casas/" + casaId + "/moradores/" + moradorId + "/dividas-a-pagar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void registraQuitacaoParcialMantemDividaPendenteComSaldoResidual() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participanteId = adicionarMorador(casaId, "Bruno");
        String dividaId = criarDespesaERetornarPrimeiraDividaId(casaId, "100.00", pagadorId, participanteId);

        mockMvc.perform(
                        post("/dividas/" + dividaId + "/quitacoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"data\": \"2026-08-10\", \"valor\": \"40.00\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.saldo").value(60.00))
                .andExpect(jsonPath("$.pendente").value(true))
                .andExpect(jsonPath("$.quitacoes.length()").value(1))
                .andExpect(jsonPath("$.quitacoes[0].valor").value(40.00));

        mockMvc.perform(get("/casas/" + casaId + "/moradores/" + participanteId + "/dividas-a-pagar"))
                .andExpect(jsonPath("$._embedded.dividaResponseList.length()").value(1));
    }

    @Test
    void registraQuitacaoTotalZeraSaldoEDividaSomeDaListagemDePendentes() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participanteId = adicionarMorador(casaId, "Bruno");
        String dividaId = criarDespesaERetornarPrimeiraDividaId(casaId, "100.00", pagadorId, participanteId);

        mockMvc.perform(
                        post("/dividas/" + dividaId + "/quitacoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"data\": \"2026-08-10\", \"valor\": \"100.00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.saldo").value(0))
                .andExpect(jsonPath("$.pendente").value(false));

        mockMvc.perform(get("/casas/" + casaId + "/moradores/" + participanteId + "/dividas-a-pagar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void rejeitaQuitacaoComValorMaiorQueOSaldo() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participanteId = adicionarMorador(casaId, "Bruno");
        String dividaId = criarDespesaERetornarPrimeiraDividaId(casaId, "100.00", pagadorId, participanteId);

        mockMvc.perform(
                        post("/dividas/" + dividaId + "/quitacoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"data\": \"2026-08-10\", \"valor\": \"100.01\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void rejeitaQuitacaoParaDividaInexistente() throws Exception {
        String dividaInexistente = UUID.randomUUID().toString();

        mockMvc.perform(
                        post("/dividas/" + dividaInexistente + "/quitacoes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"data\": \"2026-08-10\", \"valor\": \"10.00\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaBuscarDividaInexistente() throws Exception {
        String dividaInexistente = UUID.randomUUID().toString();

        mockMvc.perform(get("/dividas/" + dividaInexistente)).andExpect(status().isNotFound());
    }

    @Test
    void consultaDividaPeloLocationRetornadoNaQuitacao() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participanteId = adicionarMorador(casaId, "Bruno");
        String dividaId = criarDespesaERetornarPrimeiraDividaId(casaId, "100.00", pagadorId, participanteId);

        String location =
                mockMvc.perform(
                                post("/dividas/" + dividaId + "/quitacoes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"data\": \"2026-08-10\", \"valor\": \"20.00\"}"))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        assert location != null;
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(80.00))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    private String criarDespesaERetornarPrimeiraDividaId(
            String casaId, String valor, String pagadorId, String participanteId) throws Exception {
        String response = criarDespesa(casaId, valor, pagadorId, List.of(participanteId));
        return JsonPath.read(response, "$.dividas[0].id");
    }

    private String criarDespesa(
            String casaId, String valor, String pagadorId, List<String> participantesIds)
            throws Exception {
        String participantesJson =
                participantesIds.stream()
                        .map(id -> "\"" + id + "\"")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
        return mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"valor\": \""
                                                + valor
                                                + "\", \"natureza\": \"VARIAVEL\", \"pagadorId\": \""
                                                + pagadorId
                                                + "\", \"participantesIds\": ["
                                                + participantesJson
                                                + "], \"dataVencimento\": \"2026-09-10\"}"))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private String adicionarMorador(String casaId, String nome) throws Exception {
        String usuarioId = criarUsuario(nome);
        String response =
                mockMvc.perform(
                                post("/casas/" + casaId + "/moradores")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                "{\"nome\": \""
                                                        + nome
                                                        + "\", \"usuarioId\": \""
                                                        + usuarioId
                                                        + "\"}"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        return JsonPath.read(response, "$.id");
    }

    private String criarUsuario(String nome) throws Exception {
        String response =
                mockMvc.perform(
                                post("/usuarios")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"nome\": \"" + nome + "\"}"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        return JsonPath.read(response, "$.id");
    }

    private String criarCasa(String nome) throws Exception {
        String response =
                mockMvc.perform(
                                post("/casas")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"nome\": \"" + nome + "\"}"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        return JsonPath.read(response, "$.id");
    }
}
