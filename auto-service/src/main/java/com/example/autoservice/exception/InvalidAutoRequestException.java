package com.example.autoservice.exception;

public class InvalidAutoRequestException extends RuntimeException {

    public InvalidAutoRequestException(String message) {
        super(message);
    }
}
