package org.example.favoritoservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFavoriteRequestException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFavoriteRequest(InvalidFavoriteRequestException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Solicitud invalida",
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

    @ExceptionHandler(DuplicateFavoriteException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateFavorite(DuplicateFavoriteException ex) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                "Conflicto",
                ex.getMessage()
        );
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", status.value(),
                "error", error,
                "message", message
        ));
    }
}
