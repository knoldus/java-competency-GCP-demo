package com.nashtech.vehicleapplication.exception;

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
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Exception handler for handling {@link WebClientCustomException}.
     *
     * @param webClientCustomException
     * The {@link WebClientCustomException} instance.
     * @return A {@link ResponseEntity} containing the error response.
     */
    @ExceptionHandler(WebClientCustomException.class)
    public ResponseEntity<Object> handleWebClientCustomException(
            final WebClientCustomException webClientCustomException) {
        return new ResponseEntity<>(new ApiError(webClientCustomException
                .getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()),
                HttpStatus.BAD_REQUEST);
    }
}
