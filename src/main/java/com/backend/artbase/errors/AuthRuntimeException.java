package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AuthRuntimeException extends BaseRuntimeException {

    public AuthRuntimeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
