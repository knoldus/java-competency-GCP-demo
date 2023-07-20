package com.nashtech.exception;

/**
 The ResourceNotFoundException class is a custom exception
 that is thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     Constructs a new ResourceNotFoundException
     with the default error message "Resource not Found".
     */
    public ResourceNotFoundException() {
        super("Record not found");
    }

    /**
     * Constructs a ResourceNotFoundException with
     * the specified detail message.
     * @param message the message and
     * @param cause the details
     * that provides information about
     * the resource that was not found.
     */
    public ResourceNotFoundException(final String message,
                                     final Throwable cause) {
        super(message, cause);
    }
}
