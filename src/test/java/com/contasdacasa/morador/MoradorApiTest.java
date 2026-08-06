package com.contasdacasa.morador;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class MoradorApiTest {

    @Autowired MockMvc mockMvc;

    @Test
    void adicionaMoradorAUmaCasaExistente() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String usuarioId = criarUsuario("Ana");

        mockMvc.perform(
                        post("/casas/" + casaId + "/moradores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Ana\", \"usuarioId\": \""
                                                + usuarioId
                                                + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Ana"))
                .andExpect(jsonPath("$.casaId").value(casaId))
                .andExpect(jsonPath("$.usuarioId").value(usuarioId))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.casa.href").exists());
    }

    @Test
    void consultaMoradorPeloLocationRetornadoNaCriacao() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String usuarioId = criarUsuario("Ana");

        String location =
                mockMvc.perform(
                                post("/casas/" + casaId + "/moradores")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                "{\"nome\": \"Ana\", \"usuarioId\": \""
                                                        + usuarioId
                                                        + "\"}"))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"));
    }

    @Test
    void rejeitaAdicionarMoradorAUmaCasaInexistente() throws Exception {
        String casaInexistente = UUID.randomUUID().toString();
        String usuarioId = criarUsuario("Ana");

        mockMvc.perform(
                        post("/casas/" + casaInexistente + "/moradores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Ana\", \"usuarioId\": \""
                                                + usuarioId
                                                + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listaMoradoresDeUmaCasa() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        adicionarMorador(casaId, "Ana");
        adicionarMorador(casaId, "Bruno");

        mockMvc.perform(get("/casas/" + casaId + "/moradores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.moradorResponseList.length()").value(2))
                .andExpect(
                        jsonPath(
                                "$._embedded.moradorResponseList[*].nome",
                                org.hamcrest.Matchers.containsInAnyOrder("Ana", "Bruno")));
    }

    @Test
    void removeMoradorDeUmaCasa() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String moradorId = adicionarMorador(casaId, "Ana");

        mockMvc.perform(delete("/casas/" + casaId + "/moradores/" + moradorId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/casas/" + casaId + "/moradores"))
                .andExpect(jsonPath("$._embedded").doesNotExist());
    }

    @Test
    void rejeitaRemoverMoradorQueNaoPertenceACasaInformada() throws Exception {
        String casaA = criarCasa("Republica das Flores");
        String casaB = criarCasa("Republica dos Girassois");
        String moradorDaCasaA = adicionarMorador(casaA, "Ana");

        mockMvc.perform(delete("/casas/" + casaB + "/moradores/" + moradorDaCasaA))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaRemoverMoradorInformandoCasaInexistente() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String moradorId = adicionarMorador(casaId, "Ana");
        String casaInexistente = UUID.randomUUID().toString();

        mockMvc.perform(delete("/casas/" + casaInexistente + "/moradores/" + moradorId))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaAdicionarMoradorComUsuarioInexistente() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String usuarioInexistente = UUID.randomUUID().toString();

        mockMvc.perform(
                        post("/casas/" + casaId + "/moradores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Ana\", \"usuarioId\": \""
                                                + usuarioInexistente
                                                + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void rejeitaAdicionarUsuarioComoMoradorDuasVezesNaMesmaCasa() throws Exception {
        String casaId = criarCasa("Republica das Flores");
        String usuarioId = criarUsuario("Ana");
        adicionarMoradorComUsuario(casaId, usuarioId, "Ana");

        mockMvc.perform(
                        post("/casas/" + casaId + "/moradores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Ana\", \"usuarioId\": \""
                                                + usuarioId
                                                + "\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void permiteMesmoUsuarioSerMoradorDeCasasDiferentes() throws Exception {
        String casaA = criarCasa("Republica das Flores");
        String casaB = criarCasa("Republica dos Girassois");
        String usuarioId = criarUsuario("Ana");
        adicionarMoradorComUsuario(casaA, usuarioId, "Ana");

        mockMvc.perform(
                        post("/casas/" + casaB + "/moradores")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        "{\"nome\": \"Ana\", \"usuarioId\": \""
                                                + usuarioId
                                                + "\"}"))
                .andExpect(status().isCreated());
    }

    private String adicionarMorador(String casaId, String nome) throws Exception {
        String usuarioId = criarUsuario(nome);
        return adicionarMoradorComUsuario(casaId, usuarioId, nome);
    }

    private String adicionarMoradorComUsuario(String casaId, String usuarioId, String nome)
            throws Exception {
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
