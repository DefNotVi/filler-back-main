package cl.back.filler.controller;

import cl.back.filler.model.WaifuHusbando;
import cl.back.filler.repository.PerfilRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Nueva ruta oficial en Spring Boot moderno
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerfilController.class)
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Reemplaza al @MockBean
    private PerfilRepository perfilRepository;

    @Test
    @DisplayName("GET /api/perfiles - Debería listar todos los perfiles otakus")
    void listarPerfilesDeberiaRetornarLista() throws Exception {
        WaifuHusbando waifu = new WaifuHusbando(1L, "Speedwagon", "JoJo", "La mejor waifu", "url", 0);
        Mockito.when(perfilRepository.findAll()).thenReturn(List.of(waifu));

        mockMvc.perform(get("/api/perfiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is("Speedwagon")))
                .andExpect(jsonPath("$[0].nivelDeOlor", is(0)));
    }

    @Test
    @DisplayName("POST /api/perfiles/reaccion - Caso DISLIKE responde shiiiiiiiizaaaaaa")
    void reaccionarAPerfilDislike() throws Exception {
        String payloadJson = "{\"perfilId\": 1, \"accion\": \"DISLIKE\"}";

        mockMvc.perform(post("/api/perfiles/reaccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.mensaje", is("shiiiiiiiizaaaaaa")));
    }

    @Test
    @DisplayName("POST /api/perfiles/reaccion - Caso LIKE responde ok con algún string")
    void reaccionarAPerfilLike() throws Exception {
        String payloadJson = "{\"perfilId\": 2, \"accion\": \"LIKE\"}";

        mockMvc.perform(post("/api/perfiles/reaccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payloadJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("ok")))
                .andExpect(jsonPath("$.mensaje", anyOf(
                        containsString("compulsivo"), 
                        containsString("visto")
                )));
    }
}