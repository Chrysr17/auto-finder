package com.example.autoservice.exception;

public class RelatedResourceNotFoundException extends RuntimeException {

    public RelatedResourceNotFoundException(String message) {
        super(message);
    }
}
