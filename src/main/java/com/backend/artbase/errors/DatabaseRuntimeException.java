package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.databind.JsonSerializable.Base;

public class DatabaseRuntimeException extends BaseRuntimeException {

    public DatabaseRuntimeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
