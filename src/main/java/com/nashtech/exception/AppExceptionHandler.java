package com.nashtech.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

/**
 * Global exception handler for the application.
 */
@Slf4j
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
                String exceptionMessage = dataNotFoundException.getMessage();

        ApiError errorResponse=new ApiError(
                new Date(),
                exceptionMessage,
                HttpStatus.BAD_REQUEST
        );
        log.error(exceptionMessage);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ioException) {
        String exceptionMessage = ioException.getMessage();

        if(Objects.isNull(ioException.getMessage())){
            exceptionMessage = "IO Exception Occurred";
        }
        ApiError errorResponse = new ApiError(
                new Date(),
                exceptionMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        log.error(exceptionMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> handleInterruptedDataException(InterruptedException interruptedException) {
        String exceptionMessage = interruptedException.getMessage();
        if(Objects.isNull(interruptedException.getMessage())){
            exceptionMessage = "Interrupted Exception Occurred";
        }
        ApiError errorResponse = new ApiError(
                new Date(),
                exceptionMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        log.error(exceptionMessage);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

