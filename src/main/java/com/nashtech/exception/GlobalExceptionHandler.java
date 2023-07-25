package com.nashtech.exception;


import org.apache.kafka.common.KafkaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientException;

/**
 * Global exception handler for the application.
 */

@ControllerAdvice
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

}