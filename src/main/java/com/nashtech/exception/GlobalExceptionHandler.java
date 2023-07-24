package com.nashtech.exception;

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println("Unhandled exception caught: " + e.getMessage());
        // Perform any necessary cleanup or logging here
    }
}

