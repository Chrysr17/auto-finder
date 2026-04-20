package com.example.comparador_service.exception;

import com.example.comparador_service.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidComparisonRequestException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidComparisonRequest(InvalidComparisonRequestException ex) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Solicitud invalida",
                ex.getMessage()
        );
    }

    @ExceptionHandler(RelatedResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRelatedResourceNotFound(RelatedResourceNotFoundException ex) {
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                "Recurso no encontrado",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                ex.getMessage()
        );
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(errorResponse(status, error, message));
    }

    private ErrorResponseDTO errorResponse(HttpStatus status, String error, String message) {
        return ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .build();
    }
}
