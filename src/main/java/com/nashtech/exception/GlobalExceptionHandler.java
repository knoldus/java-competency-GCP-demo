package com.nashtech.exception;

import com.azure.cosmos.CosmosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import org.apache.kafka.common.KafkaException;
import org.springframework.web.reactive.function.client.WebClientException;

/**
 * Global exception handler for the application.
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends RuntimeException {

	
	/**
 	  * Exception handler method to handle {@link KafkaException}.
 	  * This method handles any exceptions of type {@link KafkaException} that might occur
 	  * during message sending to Kafka and returns an HTTP response with status code 400 (Bad Request).
 	  *
          * @param kafkaException The {@link KafkaException} that occurred during message sending.
	  * @return An HTTP response entity with status code 400 (Bad Request).
 	*/

        @ExceptionHandler(value = KafkaException.class)
        public ResponseEntity<String> KafkaExceptionHandler(KafkaException kafkaException)  {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred while pushing data to cloud");
        }

    /**
     * Exception handler method to handle {@link WebClientException}.
     * This method handles any exceptions of type {@link WebClientException} that might occur
     * when making requests using WebClient and returns an HTTP response with status code 400 (Bad Request).
     *
     * @param webClientException The {@link WebClientException} that occurred during a WebClient request.
     * @return An HTTP response entity with status code 400 (Bad Request).
     */
        @ExceptionHandler(value = WebClientException.class)
        public ResponseEntity<String> WebClientExceptionHandler(WebClientException webClientException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred while pulling data from mockaroo");
        }


    /**
     * Handles the DataNotFoundException globally.
     * @param dataNotFoundException the DataNotFoundException object
     * @return a ResponseEntity object with an error message
     * and HTTP status code
     */
    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerDataNotFoundException (
            final DataNotFoundException dataNotFoundException) {
        String message = dataNotFoundException.getMessage();
        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .statusCode(HttpStatus.NOT_FOUND)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the CosmosException and generates a custom error response.
     *
     * @param cosmosException The CosmosException that occurred.
     * @return A ResponseEntity with a customized error response containing the exception message,
     * HTTP status code 408 (Request Timeout), and the current LocalDateTime.
     */
    @ExceptionHandler(value = CosmosException.class)
    public ResponseEntity<ErrorResponse> handlerCosmosExceptionException(
            final CosmosException cosmosException) {
        String message = cosmosException.getMessage();
        ErrorResponse response = ErrorResponse.builder()
                .message(message)
                .statusCode(HttpStatus.REQUEST_TIMEOUT)
                .localDateTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, HttpStatus.REQUEST_TIMEOUT);
    }

}
