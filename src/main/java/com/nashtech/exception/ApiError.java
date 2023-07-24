package com.nashtech.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.util.Date;

/**
 * Represents an API error response.
 */
@Data
@AllArgsConstructor
public class ApiError {
    Date timestamp;
    String message;
    HttpStatusCode responseCode;
}
