package com.challenge.gft.exceptions.customs;

public class BadRequestException extends Exception {
    public BadRequestException(final String errorMessage) {
        super(errorMessage);
    }
}
