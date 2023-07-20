package com.nashtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 * Provides centralized handling of exceptions thrown by controllers.
 */
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Exception handler for DataNotFoundException.
     * Creates an ApiError object with error details and HTTP status code.
     *
     * @param dataNotFoundException The exception representing
     *                             DataNotFoundException.
     * @return A ResponseEntity with the ApiError and HTTP
     * status code 400 (BAD_REQUEST).
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(
            final DataNotFoundException dataNotFoundException) {
        return new ResponseEntity<>(new ApiError(
                dataNotFoundException.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
}
