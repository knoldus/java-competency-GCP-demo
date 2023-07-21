package com.nashtech.exception;

import com.azure.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * A class that handles global exception handling for all
 * REST controllers in the application.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandling implements Thread.UncaughtExceptionHandler {

    /**
     * A logger instance for logging messages and events in the class.
     */
    private static Logger logger =
            LoggerFactory.getLogger(GlobalExceptionHandling.class);

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        System.err.println("Unhandled exception caught: " + throwable.getMessage());
        logger.info("Data Not found Exception");
    }

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
                .status(HttpStatus.NOT_FOUND)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
