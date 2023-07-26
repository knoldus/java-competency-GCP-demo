package com.nashtech.exception;

/**
The DataNotFoundException class is a custom exception
that is thrown when a requested resource is not found.
*/
public class DataNotFoundException extends RuntimeException {

        /**
         Constructs a new DataNotFoundException
         with the default error message "Resource not Found".
         */
        public DataNotFoundException() {
            super("Record not found");
        }
    }
