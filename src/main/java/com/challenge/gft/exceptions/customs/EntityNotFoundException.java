package com.challenge.gft.exceptions.customs;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(final String errorMessage) {
        super(errorMessage);
    }
}
