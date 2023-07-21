package com.nashtech.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * Global exception handler for the application.
 */
@Slf4j
@ControllerAdvice
public class AppExceptionHandler extends
        ResponseEntityExceptionHandler {

    /**
     * Global exception handler for handling {@link DataNotFoundException}.
     *
     * @param dataNotFoundException
     * The {@link DataNotFoundException} to be handled.
     * @return A ResponseEntity
     * containing an {@link ApiError} object with the error details.
     */
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleDataNotFoundException(
            Exception exception, final DataNotFoundException dataNotFoundException) {
                String exceptionMessage = dataNotFoundException.getMessage();
                if(Objects.isNull(dataNotFoundException.getMessage())){
                exceptionMessage = exception.getMessage();
        }
        ApiError errorResponse=new ApiError(
                new Date(),
                exceptionMessage,
                dataNotFoundException.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        log.error(exceptionMessage,exception);
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
}

