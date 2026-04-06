package org.example.favoritoservice.exception;

public class InvalidFavoriteRequestException extends RuntimeException {

    public InvalidFavoriteRequestException(String message) {
        super(message);
    }
}
