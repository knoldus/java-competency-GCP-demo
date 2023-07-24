package com.nashtech.service.impl;

import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.ReactiveDataCars;
import com.nashtech.service.ReactiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

/**
 * Service class for handling vehicle-related operations.
 */
@Service
public class ReactiveDataServiceImpl implements ReactiveDataService {

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
     * Retrieves vehicle data from an external API.
     *
     * @param dataCount The number of vehicle data items to fetch (default: 50).
     * @return A Flux of Vehicle objects representing the fetched data.
     * @throws WebClientException If an error occurs during
     * data retrieval from the external API.
     */
    public Flux<ReactiveDataCars> getCarData(
            final Integer dataCount) throws WebClientException {
        try {
            return webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToFlux(ReactiveDataCars.class)
                    .take(dataCount);
        } catch (DataNotFoundException dataNotFoundException) {
            throw new DataNotFoundException();
        } catch (WebClientResponseException webClientResponseException) {
            throw new WebClientException(webClientResponseException.getMessage(),
                    webClientResponseException) {
            };
        }
    }

}
