package com.contasdacasa.despesa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import java.util.Map;
import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class DespesaApiTest {

    @Autowired MockMvc mockMvc;

    @Test
    void cadastraDespesaComRateioIgualGerandoDividasParaOsParticipantesExcetoOPagador()
            throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante1 = adicionarMorador(casaId, "Bruno");
        String participante2 = adicionarMorador(casaId, "Carla");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "100.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante1, participante2),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").value(100.00))
                .andExpect(jsonPath("$.natureza").value("VARIAVEL"))
                .andExpect(jsonPath("$.pagadorId").value(pagadorId))
                .andExpect(jsonPath("$.dataVencimento").value("2026-09-10"))
                .andExpect(jsonPath("$.dividas.length()").value(2))
                .andExpect(
                        jsonPath(
                                "$.dividas[*].participanteId",
                                org.hamcrest.Matchers.containsInAnyOrder(
                                        participante1, participante2)))
                .andExpect(jsonPath("$.dividas[*].pagadorId").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is(pagadorId))))
                .andExpect(jsonPath("$.dividas[*].valor").value(org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is(33.33))))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.casa.href").exists());
    }

    @Test
    void diferencaDeCentavoDoRateioIgualFicaComOPrimeiroParticipante() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante1 = adicionarMorador(casaId, "Bruno");
        String participante2 = adicionarMorador(casaId, "Carla");
        String participante3 = adicionarMorador(casaId, "Diego");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "10.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(participante1, participante2, participante3),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dividas[0].participanteId").value(participante1))
                .andExpect(jsonPath("$.dividas[0].valor").value(3.34))
                .andExpect(jsonPath("$.dividas[1].participanteId").value(participante2))
                .andExpect(jsonPath("$.dividas[1].valor").value(3.33))
                .andExpect(jsonPath("$.dividas[2].participanteId").value(participante3))
                .andExpect(jsonPath("$.dividas[2].valor").value(3.33));
    }

    @Test
    void consultaDespesaPeloLocationRetornadoNaCriacao() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");

        String location =
                mockMvc.perform(
                                post("/casas/" + casaId + "/despesas")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                despesaJson(
                                                        "50.00",
                                                        "FIXA",
                                                        pagadorId,
                                                        List.of(pagadorId, participante),
                                                        "2026-09-10")))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        assert location != null;
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.natureza").value("FIXA"));
    }

    @Test
    void permiteParticipantesQueNaoSaoTodosOsMoradoresDaCasa() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");
        adicionarMorador(casaId, "Carla");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "20.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dividas.length()").value(1));
    }

    @Test
    void rejeitaCadastrarDespesaParaCasaInexistente() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");
        String casaInexistente = UUID.randomUUID().toString();

        mockMvc.perform(
                        post("/casas/" + casaInexistente + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "20.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante),
                                                "2026-09-10")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaCadastrarDespesaComPagadorInexistente() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String participante = adicionarMorador(casaId, "Bruno");
        String pagadorInexistente = UUID.randomUUID().toString();

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "20.00",
                                                "VARIAVEL",
                                                pagadorInexistente,
                                                List.of(participante),
                                                "2026-09-10")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaCadastrarDespesaComPagadorDeOutraCasa() throws Exception {
        String casaA = criarCasa("Republica das Flores");
        String casaB = criarCasa("Republica dos Girassois");
        String pagadorDaCasaB = adicionarMorador(casaB, "Ana");
        String participante = adicionarMorador(casaA, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaA + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "20.00",
                                                "VARIAVEL",
                                                pagadorDaCasaB,
                                                List.of(participante),
                                                "2026-09-10")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaCadastrarDespesaComParticipanteDeOutraCasa() throws Exception {
        String casaA = criarCasa("Republica das Flores");
        String casaB = criarCasa("Republica dos Girassois");
        String pagador = adicionarMorador(casaA, "Ana");
        String participanteDeOutraCasa = adicionarMorador(casaB, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaA + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "20.00",
                                                "VARIAVEL",
                                                pagador,
                                                List.of(pagador, participanteDeOutraCasa),
                                                "2026-09-10")))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaCadastrarDespesaSemParticipantes() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(despesaJson("20.00", "VARIAVEL", pagadorId, List.of(), "2026-09-10")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaCadastrarDespesaComValorNegativoOuZero() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "0.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(participante),
                                                "2026-09-10")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaCadastrarDespesaComNaturezaInvalida() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"valor\": \"20.00\", \"natureza\": \"ESPORADICA\", \"pagadorId\": \""
                                                + pagadorId
                                                + "\", \"participantesIds\": [\""
                                                + participante
                                                + "\"], \"dataVencimento\": \"2026-09-10\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastraDespesaComRateioPorRendaDivideProporcionalmenteARendaDosParticipantes()
            throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante1 = adicionarMorador(casaId, "Bruno");
        String participante2 = adicionarMorador(casaId, "Carla");
        atualizarRenda(casaId, pagadorId, "1000.00");
        atualizarRenda(casaId, participante1, "3000.00");
        atualizarRenda(casaId, participante2, "1000.00");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJsonPorRenda(
                                                "100.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante1, participante2),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoRateio").value("POR_RENDA"))
                .andExpect(jsonPath("$.dividas.length()").value(2))
                .andExpect(jsonPath("$.dividas[0].participanteId").value(participante1))
                .andExpect(jsonPath("$.dividas[0].valor").value(60.00))
                .andExpect(jsonPath("$.dividas[1].participanteId").value(participante2))
                .andExpect(jsonPath("$.dividas[1].valor").value(20.00));
    }

    @Test
    void diferencaDeCentavoDoRateioPorRendaFicaComOPrimeiroParticipante() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante1 = adicionarMorador(casaId, "Bruno");
        String participante2 = adicionarMorador(casaId, "Carla");
        String participante3 = adicionarMorador(casaId, "Diego");
        atualizarRenda(casaId, participante1, "1000.00");
        atualizarRenda(casaId, participante2, "2000.00");
        atualizarRenda(casaId, participante3, "3000.00");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJsonPorRenda(
                                                "10.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(participante1, participante2, participante3),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dividas[0].participanteId").value(participante1))
                .andExpect(jsonPath("$.dividas[0].valor").value(1.67))
                .andExpect(jsonPath("$.dividas[1].participanteId").value(participante2))
                .andExpect(jsonPath("$.dividas[1].valor").value(3.33))
                .andExpect(jsonPath("$.dividas[2].participanteId").value(participante3))
                .andExpect(jsonPath("$.dividas[2].valor").value(5.00));
    }

    @Test
    void rejeitaCadastrarDespesaComRateioPorRendaQuandoParticipanteNaoTemRendaCadastrada()
            throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");
        atualizarRenda(casaId, pagadorId, "1000.00");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJsonPorRenda(
                                                "10.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante),
                                                "2026-09-10")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastraDespesaComRateioIgualQuandoTipoRateioNaoEhInformado() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJson(
                                                "20.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoRateio").value("IGUAL"));
    }

    @Test
    void cadastraDespesaComRateioValorFixoUsandoOsValoresDefinidosPorParticipante()
            throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante1 = adicionarMorador(casaId, "Bruno");
        String participante2 = adicionarMorador(casaId, "Carla");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJsonValorFixo(
                                                "100.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante1, participante2),
                                                Map.of(
                                                        pagadorId, "20.00",
                                                        participante1, "70.00",
                                                        participante2, "10.00"),
                                                "2026-09-10")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoRateio").value("VALOR_FIXO"))
                .andExpect(jsonPath("$.dividas.length()").value(2))
                .andExpect(
                        jsonPath(
                                "$.dividas[?(@.participanteId=='" + participante1 + "')].valor")
                                .value(70.00))
                .andExpect(
                        jsonPath(
                                "$.dividas[?(@.participanteId=='" + participante2 + "')].valor")
                                .value(10.00));
    }

    @Test
    void rejeitaCadastrarDespesaComRateioValorFixoQuandoSomaDivergeDoValorTotal()
            throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJsonValorFixo(
                                                "10.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante),
                                                Map.of(pagadorId, "4.00", participante, "5.00"),
                                                "2026-09-10")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaCadastrarDespesaComRateioValorFixoQuandoParticipanteNaoTemValorDefinido()
            throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String pagadorId = adicionarMorador(casaId, "Ana");
        String participante = adicionarMorador(casaId, "Bruno");

        mockMvc.perform(
                        post("/casas/" + casaId + "/despesas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        despesaJsonValorFixo(
                                                "10.00",
                                                "VARIAVEL",
                                                pagadorId,
                                                List.of(pagadorId, participante),
                                                Map.of(pagadorId, "10.00"),
                                                "2026-09-10")))
                .andExpect(status().isBadRequest());
    }

    private String despesaJson(
            String valor,
            String natureza,
            String pagadorId,
            List<String> participantesIds,
            String dataVencimento) {
        String participantesJson =
                participantesIds.stream()
                        .map(id -> "\"" + id + "\"")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
        return "{\"valor\": \""
                + valor
                + "\", \"natureza\": \""
                + natureza
                + "\", \"pagadorId\": \""
                + pagadorId
                + "\", \"participantesIds\": ["
                + participantesJson
                + "], \"dataVencimento\": \""
                + dataVencimento
                + "\"}";
    }

    private String despesaJsonPorRenda(
            String valor,
            String natureza,
            String pagadorId,
            List<String> participantesIds,
            String dataVencimento) {
        String participantesJson =
                participantesIds.stream()
                        .map(id -> "\"" + id + "\"")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
        return "{\"valor\": \""
                + valor
                + "\", \"natureza\": \""
                + natureza
                + "\", \"tipoRateio\": \"POR_RENDA\", \"pagadorId\": \""
                + pagadorId
                + "\", \"participantesIds\": ["
                + participantesJson
                + "], \"dataVencimento\": \""
                + dataVencimento
                + "\"}";
    }

    private String despesaJsonValorFixo(
            String valor,
            String natureza,
            String pagadorId,
            List<String> participantesIds,
            Map<String, String> valoresFixos,
            String dataVencimento) {
        String participantesJson =
                participantesIds.stream()
                        .map(id -> "\"" + id + "\"")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
        String valoresFixosJson =
                valoresFixos.entrySet().stream()
                        .map(entry -> "\"" + entry.getKey() + "\": \"" + entry.getValue() + "\"")
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
        return "{\"valor\": \""
                + valor
                + "\", \"natureza\": \""
                + natureza
                + "\", \"tipoRateio\": \"VALOR_FIXO\", \"pagadorId\": \""
                + pagadorId
                + "\", \"participantesIds\": ["
                + participantesJson
                + "], \"valoresFixos\": {"
                + valoresFixosJson
                + "}, \"dataVencimento\": \""
                + dataVencimento
                + "\"}";
    }

    private void atualizarRenda(String casaId, String moradorId, String valor) throws Exception {
        mockMvc.perform(
                put("/casas/" + casaId + "/moradores/" + moradorId + "/renda")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\": \"" + valor + "\"}"));
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
