package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class RuntimeFileException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RuntimeFileException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
