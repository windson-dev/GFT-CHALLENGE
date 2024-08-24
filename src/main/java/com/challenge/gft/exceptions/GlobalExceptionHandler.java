package com.challenge.gft.exceptions;

import com.challenge.gft.exceptions.customs.BadRequestException;
import com.challenge.gft.exceptions.customs.NotFoundException;
import com.challenge.gft.exceptions.customs.UniqueConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(final MethodArgumentNotValidException ex) {
        final var errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        final var errorDTO = ErrorResponse.builder()
                .message(MessageError.VALIDATION_ERROR.name())
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundErrors(final NotFoundException ex) {
        final var errorDTO = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> uniqueConstraintViolationException(final UniqueConstraintViolationException ex) {
        final var errorDTO = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(final BadRequestException ex) {
        final var errorDTO = ErrorResponse.builder()
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}