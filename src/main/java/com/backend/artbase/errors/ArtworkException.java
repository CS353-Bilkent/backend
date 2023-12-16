package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class ArtworkException extends BaseRuntimeException {

    public ArtworkException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
