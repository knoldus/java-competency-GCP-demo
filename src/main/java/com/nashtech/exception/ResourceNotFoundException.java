package com.nashtech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 The ResourceNotFoundException class is a custom exception
 that is thrown when a requested resource is not found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Record not found")
public class ResourceNotFoundException extends RuntimeException {

    /**
     Constructs a new ResourceNotFoundException
     with the default error message "Resource not Found".
     */
    public ResourceNotFoundException() {
        super("Record not found");
    }
}
