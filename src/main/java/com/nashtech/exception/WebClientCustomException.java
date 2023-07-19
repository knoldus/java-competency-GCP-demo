package com.nashtech.vehicleapplication.exception;

/**
 * Exception class to represent errors that
 * occur during WebClient operations.
 */
public class WebClientCustomException extends
        RuntimeException {

    /**
     * Constructs a new WebClientException
     * with the specified error message.
     *
     * @param message the error message
     */
    public WebClientCustomException(
            final String message) {
        super(message);
    }

    /**
     * Constructs a new WebClientException
     * with the specified error message and cause.
     *
     * @param message the error message
     * @param cause   the cause of the exception
     */
    public WebClientCustomException(
            final String message, final Throwable cause) {
        super(message, cause);
    }
}
