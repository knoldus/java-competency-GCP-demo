package com.nashtech.exception;

import com.azure.core.exception.ResourceNotFoundException;
import com.azure.cosmos.CosmosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * A class that handles global exception handling for all
 * REST controllers in the application.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles the DataNotFoundException globally.
     * @param dataNotFoundException the DataNotFoundException object
     * @return a ResponseEntity object with an error message
     * and HTTP status code
     */
    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerDataNotFoundException (
            final DataNotFoundException dataNotFoundException) {
        String message = dataNotFoundException.getMessage();
        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .statusCode(HttpStatus.NOT_FOUND)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the CosmosException and generates a custom error response.
     *
     * @param cosmosException The CosmosException that occurred.
     * @return A ResponseEntity with a customized error response containing the exception message,
     * HTTP status code 408 (Request Timeout), and the current LocalDateTime.
     */
    @ExceptionHandler(value = CosmosException.class)
    public ResponseEntity<ErrorResponse> handlerCosmosExceptionException(
            final CosmosException cosmosException) {
        String message = cosmosException.getMessage();
        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .statusCode(HttpStatus.REQUEST_TIMEOUT)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    /**
     * Exception handler for handling InterruptedExceptions
     * that may occur during data processing.
     *
     * @param interruptedException The InterruptedException that was thrown.
     * @return A ResponseEntity with an error response
     * and HTTP status code 500 (Internal Server Error).
     */
    @ExceptionHandler(value = InterruptedException.class)
    public ResponseEntity<Object> handleInterruptedDataException(
            InterruptedException interruptedException) {
        String exceptionMessage = interruptedException.getMessage();
        if (Objects.isNull(interruptedException.getMessage())) {
            exceptionMessage = "Interrupted Exception Occurred";
        }
        ErrorResponse response = ErrorResponse.builder()
                .message(exceptionMessage)
                .statusCode(HttpStatus.REQUEST_TIMEOUT)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

    /**
     * Exception handler for handling WebClientExceptions that
     * may occur during communication with external web services.
     *
     * @param webClientException The WebClientException that was thrown.
     * @return A ResponseEntity with an error response and HTTP
     * status code 500 (Internal Server Error).
     */
    @ExceptionHandler(value = WebClientException.class)
    public ResponseEntity<Object> handleIOException(
            WebClientException webClientException) {
        String exceptionMessage = webClientException.getMessage();

        if(Objects.isNull(webClientException.getMessage())){
            exceptionMessage = "WebClient Exception Occurred";
        }
        ErrorResponse response = ErrorResponse.builder()
                .message(exceptionMessage)
                .statusCode(HttpStatus.SERVICE_UNAVAILABLE)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }


}
