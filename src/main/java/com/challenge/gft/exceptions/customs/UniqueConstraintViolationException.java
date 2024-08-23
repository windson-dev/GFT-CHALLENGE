package com.challenge.gft.exceptions.customs;

public class UniqueConstraintViolationException extends Exception {
    public UniqueConstraintViolationException(final String errorMessage) {
        super(errorMessage);
    }
}
