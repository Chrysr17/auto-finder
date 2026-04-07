package com.example.autoservice.exception;

public class InvalidCatalogRequestException extends RuntimeException {

    public InvalidCatalogRequestException(String message) {
        super(message);
    }
}
