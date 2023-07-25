package com.nashtech.service.impl;

import com.nashtech.model.ReactiveDataCar;
import com.nashtech.service.CloudDataService;
import com.nashtech.service.ReactiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;

/**
 * Implementation of Service class responsible for fetching and sending vehicle data.
 */
@Service
@Slf4j
public class ReactiveDataServiceImpl implements ReactiveDataService {

    /**
     * The WebClient for making HTTP requests to the external API.
     */
    private WebClient webClient;

    @Autowired
    private CloudDataService cloudDataService;

    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate and WebClient.
     */
    public ReactiveDataServiceImpl() {
        this.webClient = WebClient.create("https://my.api.mockaroo.com/");
    }

    /**
     * Fetches vehicle data from an external API.
     *
     * @return A Flux of VehicleDetails representing the fetched data.
     * Fetches vehicle data and sends it to the Kafka topic.
     * The data is sent with a delay of one second between
     * each element to simulate real-time behavior.
     */
    public void fetchAndSendData() {
        String apiUrl = "/vehicle.json?key=e60438e0";

        webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToFlux(ReactiveDataCar.class)
                .switchIfEmpty(Flux.error(new WebClientException("Error Occurred") {
                }))
                .subscribe(
                        s -> {
                            cloudDataService.sendData(s);
                        }
                );
    }
}
