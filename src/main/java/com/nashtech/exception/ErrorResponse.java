package com.nashtech.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 The ErrorResponse class represents a generic response object that is
 used to encapsulate the response data
 and metadata returned by an API.
 */
@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {

    /**
     * A message that describes the response.
     */
    private String message;

    /**
     * An HTTP status code that describes the result of the operation.
     */
    private HttpStatus statusCode;

    /**
     * Represents a date and time without time zone information.
     */
    private LocalDateTime localDateTime;

}
