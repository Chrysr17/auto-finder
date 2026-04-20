package com.example.comparador_service.exception;

import com.example.comparador_service.dto.ErrorResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleInvalidComparisonRequest_deberiaRetornarBadRequest() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleInvalidComparisonRequest(
                new InvalidComparisonRequestException("Debe enviar al menos dos autos para comparar")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Solicitud invalida", response.getBody().getError());
        assertEquals("Debe enviar al menos dos autos para comparar", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleRelatedResourceNotFound_deberiaRetornarNotFound() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleRelatedResourceNotFound(
                new RelatedResourceNotFoundException("No se encontro el auto con id: 2")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Recurso no encontrado", response.getBody().getError());
        assertEquals("No se encontro el auto con id: 2", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleGenericException_deberiaRetornarInternalServerError() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleGenericException(
                new RuntimeException("Fallo inesperado")
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Error interno del servidor", response.getBody().getError());
        assertEquals("Fallo inesperado", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }
}
