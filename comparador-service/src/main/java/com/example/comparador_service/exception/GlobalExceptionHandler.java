package com.example.comparador_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidComparisonRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidComparisonRequest(InvalidComparisonRequestException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Solicitud invalida",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RelatedResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRelatedResourceNotFound(RelatedResourceNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                ex.getMessage()
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(errorResponse(status, error, message));
    }

    private Map<String, Object> errorResponse(HttpStatus status, String error, String message) {
        return Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", error,
                "message", message
        );
    }
}
