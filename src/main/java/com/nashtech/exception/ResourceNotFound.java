package com.nashtech.exception;

/**
 * Custom exception class for representing data not found situations.
 * This exception should be thrown
 * when a requested resource or data is not found in the system.
 * It extends the RuntimeException class.
 */
public class ResourceNotFound extends RuntimeException {

    /**
     * Constructs a new ResourceNotFound with the specified error message.
     *
     * @param message The error message describing the reason for the exception.
     */
    public ResourceNotFound(final String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException
     * with the specified error message and cause.
     *
     * @param message The error message describing the reason for the exception.
     * @param cause   The cause of the exception, which can be
     *               another throwable that triggered this one.
     */
    public ResourceNotFound(
            final String message, final Throwable cause) {
        super(message, cause);
    }
}
