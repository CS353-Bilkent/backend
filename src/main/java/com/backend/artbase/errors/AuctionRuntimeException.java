package com.backend.artbase.errors;

import org.springframework.http.HttpStatus;

public class AuctionRuntimeException extends BaseRuntimeException {

    public AuctionRuntimeException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

}
