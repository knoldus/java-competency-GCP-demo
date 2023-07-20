package com.nashtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends
        ResponseEntityExceptionHandler {

    /**
     * Global exception handler for handling {@link ResourceNotFound}.
     *
     * @return A ResponseEntity
     * containing an {@link ApiError} object with the error details.
     */
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<Object> handleDataNotFoundException(
            final ResourceNotFound dataNotFoundException) {
        ApiError apiError = new ApiError(
                dataNotFoundException.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
