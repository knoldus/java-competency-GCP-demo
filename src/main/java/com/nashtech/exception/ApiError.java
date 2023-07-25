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
    /**
     * The timestamp when the response was generated.
     */
    private Date timestamp;

    /**
     * The message associated with the API response.
     */
    private String message;

    /**
     * The HTTP status code of the API response.
     */
    private HttpStatusCode responseCode;
}
