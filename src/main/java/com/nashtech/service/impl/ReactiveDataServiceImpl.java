package com.nashtech.service.impl;

import com.nashtech.model.ReactiveDataCars;
import com.nashtech.service.CloudDataService;
import com.nashtech.service.ReactiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Service class for handling vehicle-related operations.
 */
@Slf4j
@Service
public class ReactiveDataServiceImpl implements
        ReactiveDataService {

    /**
     * WebClient instance for making HTTP requests to the external API.
     */
    @Autowired
    private WebClient webClient;

    /**
     * URL of the external API for retrieving vehicle data.
     */
    @Value("${apiUrl}")
    private String apiUrl;

    /**
     * Autowired instance of CloudDataService
     * used for publishing vehicle data to Google Cloud Pub/Sub.
     */
    @Autowired
    private CloudDataService cloudDataService;


    /**
     * Retrieves vehicle data from an external API.
     *
     * @param dataCount The number of vehicle data items to fetch (default: 50).
     * @throws WebClientException If an error occurs during
     * data retrieval from the external API.
     */
    public void getCarData(final Integer dataCount) {
        webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToFlux(ReactiveDataCars.class)
                .take(dataCount)
                .switchIfEmpty(Flux.defer(() -> {
                 log.info("No data found");
                 return Flux.empty();
                }))
                .onErrorResume(throwable -> {
                    log.error("Error occurred during data retrieval: "
                    + throwable.getMessage());
                    return Flux.error (
                            new WebClientException (
                                    "Failed to retrieve car data", throwable) { });
                })
               .subscribe(
                        data -> {
                            try {
                                cloudDataService.publishCarData(data);
                            } catch (IOException
                                     | InterruptedException
                                     | ExecutionException error) {
                                throw new RuntimeException(error);
                            }
                        }
                );
    }
}
