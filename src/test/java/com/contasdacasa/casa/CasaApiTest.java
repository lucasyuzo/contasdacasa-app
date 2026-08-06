package com.contasdacasa.casa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.contasdacasa.TestcontainersConfiguration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
}
