package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class CollectionRuntimeException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CollectionRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
