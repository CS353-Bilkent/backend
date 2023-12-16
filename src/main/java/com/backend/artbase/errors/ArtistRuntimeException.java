package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class ArtistRuntimeException extends BaseRuntimeException {

    public ArtistRuntimeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}
