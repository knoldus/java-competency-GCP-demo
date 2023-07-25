package com.nashtech.exception;

import com.google.cloud.spring.data.firestore.FirestoreDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import java.util.Date;
import java.util.Objects;

/**
 * Global exception handler for the application.
 * Provides centralized handling of exceptions thrown by controllers.
 */
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles FirestoreDataException
     * and creates an ApiResponse with error details.
     *
     * @param firestoreDataException
     * The FirestoreDataException to handle.
     * @return A ResponseEntity containing an ApiResponse with error details
     * and a HTTP 400 status code.
     */
    @ExceptionHandler(FirestoreDataException.class)
    public ResponseEntity<Object> handleFirestoreDataException(
            final FirestoreDataException firestoreDataException) {
        String exceptionMessage = firestoreDataException.getMessage();
        if (Objects.isNull(firestoreDataException.getMessage())) {
            exceptionMessage = "Something went wrong "
                    + ", Unable to retrieve the data";
        }
        ApiError errorResponse = new ApiError(
                new Date(),
                firestoreDataException.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
