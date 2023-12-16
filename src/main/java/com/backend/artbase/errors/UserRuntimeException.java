package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UserRuntimeException extends BaseRuntimeException {

    public UserRuntimeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
