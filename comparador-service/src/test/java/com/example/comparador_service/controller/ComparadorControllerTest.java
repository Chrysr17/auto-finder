package com.example.comparador_service.controller;

import com.example.comparador_service.dto.AutoComparadoDTO;
import com.example.comparador_service.dto.ComparacionDTO;
import com.example.comparador_service.exception.InvalidComparisonRequestException;
import com.example.comparador_service.exception.RelatedResourceNotFoundException;
import com.example.comparador_service.service.ComparadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ComparadorController.class,
        properties = {
                "AUTO_SERVICE_URL=http://localhost",
                "jwt.secret=12345678901234567890123456789012"
        }
)
@AutoConfigureMockMvc(addFilters = false)
class ComparadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ComparadorService comparadorService;

    @Test
    void comparar_deberiaRetornarOk() throws Exception {
        ComparacionDTO respuesta = ComparacionDTO.builder()
                .criterio("general")
                .tipoComparacion("simple")
                .autosComparados(List.of(
                        AutoComparadoDTO.builder()
                                .id(1L)
                                .marcaNombre("Toyota")
                                .modeloNombre("Corolla")
                                .precio(22000.0)
                                .build()
                ))
                .build();

        when(comparadorService.compararAutos(List.of(1L, 2L), "general")).thenReturn(respuesta);

        mockMvc.perform(post("/api/comparar")
                        .param("criterio", "general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L, 2L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.criterio").value("general"))
                .andExpect(jsonPath("$.tipoComparacion").value("simple"))
                .andExpect(jsonPath("$.autosComparados[0].id").value(1L))
                .andExpect(jsonPath("$.autosComparados[0].marcaNombre").value("Toyota"));
    }

    @Test
    void comparar_deberiaUsarCriterioGeneralPorDefecto() throws Exception {
        ComparacionDTO respuesta = ComparacionDTO.builder()
                .criterio("general")
                .autosComparados(List.of())
                .build();

        when(comparadorService.compararAutos(List.of(1L, 2L), "general")).thenReturn(respuesta);

        mockMvc.perform(post("/api/comparar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L, 2L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.criterio").value("general"));
    }

    @Test
    void comparar_deberiaRetornarBadRequestCuandoSolicitudEsInvalida() throws Exception {
        when(comparadorService.compararAutos(List.of(1L), "general"))
                .thenThrow(new InvalidComparisonRequestException("Debe enviar al menos dos autos para comparar"));

        mockMvc.perform(post("/api/comparar")
                        .param("criterio", "general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Solicitud invalida"))
                .andExpect(jsonPath("$.message").value("Debe enviar al menos dos autos para comparar"));
    }

    @Test
    void comparar_deberiaRetornarNotFoundCuandoAutoNoExiste() throws Exception {
        when(comparadorService.compararAutos(List.of(1L, 2L), "general"))
                .thenThrow(new RelatedResourceNotFoundException("No se encontro el auto con id: 2"));

        mockMvc.perform(post("/api/comparar")
                        .param("criterio", "general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L, 2L))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Recurso no encontrado"))
                .andExpect(jsonPath("$.message").value("No se encontro el auto con id: 2"));
    }

    @Test
    void comparar_deberiaRetornarInternalServerErrorCuandoOcurreErrorInesperado() throws Exception {
        when(comparadorService.compararAutos(List.of(1L, 2L), "general"))
                .thenThrow(new RuntimeException("Fallo inesperado"));

        mockMvc.perform(post("/api/comparar")
                        .param("criterio", "general")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L, 2L))))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Error interno del servidor"))
                .andExpect(jsonPath("$.message").value("Fallo inesperado"));
    }
}
