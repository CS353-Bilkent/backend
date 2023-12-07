package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AuthRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public AuthRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
