package com.example.autoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSearchFilterException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidSearchFilter(InvalidSearchFilterException ex) {
        return ResponseEntity.badRequest().body(errorResponse(
                HttpStatus.BAD_REQUEST,
                "Filtros de busqueda invalidos",
                ex.getMessage()
        ));
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
