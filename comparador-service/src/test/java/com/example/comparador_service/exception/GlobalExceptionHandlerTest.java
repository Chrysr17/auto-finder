package com.example.comparador_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleInvalidComparisonRequest_deberiaRetornarBadRequest() {
        ResponseEntity<Map<String, Object>> response = handler.handleInvalidComparisonRequest(
                new InvalidComparisonRequestException("Debe enviar al menos dos autos para comparar")
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().get("status"));
        assertEquals("Solicitud invalida", response.getBody().get("error"));
        assertEquals("Debe enviar al menos dos autos para comparar", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void handleRelatedResourceNotFound_deberiaRetornarNotFound() {
        ResponseEntity<Map<String, Object>> response = handler.handleRelatedResourceNotFound(
                new RelatedResourceNotFoundException("No se encontro el auto con id: 2")
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().get("status"));
        assertEquals("Recurso no encontrado", response.getBody().get("error"));
        assertEquals("No se encontro el auto con id: 2", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void handleGenericException_deberiaRetornarInternalServerError() {
        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(
                new RuntimeException("Fallo inesperado")
        );

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().get("status"));
        assertEquals("Error interno del servidor", response.getBody().get("error"));
        assertEquals("Fallo inesperado", response.getBody().get("message"));
        assertNotNull(response.getBody().get("timestamp"));
    }
}
