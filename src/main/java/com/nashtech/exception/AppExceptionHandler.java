package com.nashtech.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
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
     * Exception handler for handling WebClientExceptions that
     * may occur during communication with external web services.
     *
     * @param webClientException The WebClientException that was thrown.
     * @return A ResponseEntity with an error response and HTTP
     * status code 500 (Internal Server Error).
     */
    @ExceptionHandler(WebClientException.class)
    public ResponseEntity<Object> handleIOException(
            WebClientException webClientException) {
        String exceptionMessage = webClientException.getMessage();

        if(Objects.isNull(webClientException.getMessage())){
            exceptionMessage = "WebClient Exception Occurred";
        }
        ApiError errorResponse = new ApiError(
                new Date(),
                exceptionMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        log.error(exceptionMessage);
        return new ResponseEntity<>(errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Exception handler for handling InterruptedExceptions
     * that may occur during data processing.
     *
     * @param interruptedException The InterruptedException that was thrown.
     * @return A ResponseEntity with an error response
     * and HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> handleInterruptedDataException(
            InterruptedException interruptedException) {
        String exceptionMessage = interruptedException.getMessage();
        if (Objects.isNull(interruptedException.getMessage())) {
            exceptionMessage = "Interrupted Exception Occurred";
        }
        ApiError errorResponse = new ApiError(
                new Date(),
                exceptionMessage,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        log.error(exceptionMessage);
        return new ResponseEntity<>(errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

