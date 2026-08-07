package com.contasdacasa.casa;

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

import java.util.UUID;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@AutoConfigureMockMvc
class CasaApiTest {

    @Autowired MockMvc mockMvc;

    @Test
    void criaUmaCasa() throws Exception {
        mockMvc.perform(
                        post("/casas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                {"nome": "Republica das Flores"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Republica das Flores"))
                .andExpect(jsonPath("$.rateioPadrao").value("IGUAL"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    void consultaCasaPeloLocationRetornadoNaCriacao() throws Exception {
        String location =
                mockMvc.perform(
                                post("/casas")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(
                                                """
                                {"nome": "Republica das Flores"}
                                """))
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        assert location != null;
        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Republica das Flores"));
    }

    @Test
    void atualizaORateioPadraoDaCasa() throws Exception {
        String casaId = criarCasa("Republica das Flores");

        mockMvc.perform(
                        put("/casas/" + casaId + "/rateio-padrao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"tipoRateio\": \"POR_RENDA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateioPadrao").value("POR_RENDA"));

        mockMvc.perform(get("/casas/" + casaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rateioPadrao").value("POR_RENDA"));
    }

    @Test
    void rejeitaAtualizarRateioPadraoComTipoInvalido() throws Exception {
        String casaId = criarCasa("Republica das Flores");

        mockMvc.perform(
                        put("/casas/" + casaId + "/rateio-padrao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"tipoRateio\": \"INEXISTENTE\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rejeitaAtualizarRateioPadraoDeCasaInexistente() throws Exception {
        mockMvc.perform(
                        put("/casas/" + UUID.randomUUID() + "/rateio-padrao")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"tipoRateio\": \"POR_RENDA\"}"))
                .andExpect(status().isNotFound());
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
