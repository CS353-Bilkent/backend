package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class CollectionRuntimeException extends BaseRuntimeException {

    public CollectionRuntimeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
