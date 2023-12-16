package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;
    private String message;

    public BaseRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
