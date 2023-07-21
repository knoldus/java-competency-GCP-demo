package com.nashtech.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    /**
     * The message associated with the API error.
     */
    private final String message;

    /**
     * The HTTP status associated with the API error.
     */
    private final HttpStatus status;

    /**
     * The local date and time when the API error occurred.
     */
    private final LocalDateTime localDateTime;
}
