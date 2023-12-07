package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UserRuntimeException extends RuntimeException {

    private final HttpStatus httpStatus;

    public UserRuntimeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
