package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class ArtistRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private String message;

    public ArtistRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
