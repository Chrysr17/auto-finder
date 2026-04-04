package com.example.autoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> fieldErrors.put(error.getField(), error.getDefaultMessage()));

        Map<String, Object> response = new LinkedHashMap<>(errorResponse(
                HttpStatus.BAD_REQUEST,
                "Solicitud invalida",
                "Hay campos invalidos en la solicitud"
        ));
        response.put("details", fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(InvalidAutoRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidAutoRequest(InvalidAutoRequestException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Solicitud invalida",
                ex.getMessage()
        );
    }

    @ExceptionHandler(InvalidSearchFilterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSearchFilter(InvalidSearchFilterException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Filtros de busqueda invalidos",
                ex.getMessage()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RelatedResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRelatedResourceNotFound(RelatedResourceNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Referencia invalida",
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
