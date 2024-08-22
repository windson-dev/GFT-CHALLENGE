package com.challenge.gft.exceptions.customs;

public class DocumentAlreadyExistException extends Exception {
    public DocumentAlreadyExistException(final String errorMessage) {
        super(errorMessage);
    }
}
