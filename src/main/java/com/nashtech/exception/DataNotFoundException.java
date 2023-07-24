package com.nashtech.exception;

/**
 * Custom exception class for representing data not found situations.
 * This exception should be thrown
 * when a requested resource or data is not found in the system.
 * It extends the RuntimeException class.
 */
public class DataNotFoundException extends RuntimeException {

    /**
     * Constructs a new DataNotFoundException with the specified error message.
     */
    public DataNotFoundException() {
        super("Data Not Found Exception");
    }
}
