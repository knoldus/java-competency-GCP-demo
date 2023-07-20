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
public class AppExceptionHandler extends
        ResponseEntityExceptionHandler {

    /**
     * Global exception handler for handling {@link DataNotFoundException}.
     *
     * @param dataNotFoundException
     * The {@link DataNotFoundException} to be handled.
     * @return A ResponseEntity
     * containing an {@link ApiError} object with the error details.
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(
            final DataNotFoundException dataNotFoundException) {
                ApiError apiError = new ApiError(
                dataNotFoundException.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


}
