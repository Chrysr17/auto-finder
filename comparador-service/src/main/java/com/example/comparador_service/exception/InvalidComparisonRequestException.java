package com.example.comparador_service.exception;

public class InvalidComparisonRequestException extends RuntimeException {

    public InvalidComparisonRequestException(String message) {
        super(message);
    }
}
