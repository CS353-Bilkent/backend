package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class ArtworkException extends RuntimeException {

    private final HttpStatus httpStatus;
    private String message;

    public ArtworkException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
