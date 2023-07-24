package com.nashtech.service;

import com.nashtech.model.VehicleDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;



/**
 * Service class responsible for fetching and sending vehicle data.
 */
@Service
@Slf4j
public class VehicleService {


    /**
     * The KafkaTemplate for sending vehicle data to Kafka topics.
     */
    private final KafkaTemplate<String, VehicleDetails> kafkaTemplate;

    /**
     * The WebClient for making HTTP requests to the external API.
     */
    private WebClient webClient;

    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate and WebClient.
     */
    public VehicleService(final KafkaTemplate<String,
            VehicleDetails> kafkaSender) {
        this.kafkaTemplate = kafkaSender;
        this.webClient = WebClient.create("https://my.api.mockaroo.com/");
    }

    /**
     * Fetches vehicle data from an external API.
     *
     * @return A Flux of VehicleDetails representing the fetched data.
     * @throws RuntimeException if an error occurs during
     * the API request or data retrieval.
     * Fetches vehicle data and sends it to the Kafka topic.
     * The data is sent with a delay of one second between
     * each element to simulate real-time behavior.
     */
    public void fetchAndSendData() {
        String apiUrl = "/vehicle.json?key=e60438e0";

        webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToFlux(VehicleDetails.class)
                .switchIfEmpty(Flux.error(new WebClientException("Error Occurred") {
                }))
                .subscribe(
                        s -> {
                            try {
                                Message<VehicleDetails> message = MessageBuilder
                                        .withPayload(s)
                                        .setHeader(KafkaHeaders.TOPIC, "myeventhub")
                                        .build();

                                kafkaTemplate.send(message);
                            } catch (KafkaException kafkaException) {
                                log.info("Exception occurred while sending data to topic");
                            }


                        }
                );
    }
}

