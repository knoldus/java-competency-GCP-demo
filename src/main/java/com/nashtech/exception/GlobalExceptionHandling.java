package com.nashtech.exception;

import com.azure.core.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * A class that handles global exception handling for all
 * REST controllers in the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandling {

    /**
     * Handles the ResourceNotFoundException globally.
     * @param resourceNotFoundException the ResourceNotFoundException object
     * @return a ResponseEntity object with an error message
     * and HTTP status code
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handlerResourceNotFoundException(
            final ResourceNotFoundException resourceNotFoundException) {
        String message = resourceNotFoundException.getMessage();
        ApiResponse response = ApiResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
