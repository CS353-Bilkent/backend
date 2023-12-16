package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class RuntimeFileException extends BaseRuntimeException {

    public RuntimeFileException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
