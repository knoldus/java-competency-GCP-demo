package com.nashtech.service;

import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Vehicle;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

/**
 * Service class for handling vehicle-related operations.
 */
@Service
public class VehicleService {

    /**
     * The WebClient used for making API calls to retrieve vehicle data.
     */
    private final WebClient webClient;

    /**
     * Constructs a new VehicleService with the provided WebClient.
     *
     * @param webClientInstance The WebClient to be used for making API calls.
     */
    public VehicleService(final WebClient webClientInstance) {
        this.webClient = webClientInstance;
    }

    /**
     * Retrieves vehicle data from an external API.
     *
     * @param dataCount The number of vehicle data items to fetch (default: 50).
     * @return A Flux of Vehicle objects representing the fetched data.
     * @throws WebClientException If an error occurs during data
     * retrieval from the external API.
     */
    public Flux<Vehicle> getVehicleData(final Integer dataCount)
            throws WebClientException {
        String apiUrl = "/vehicle.json?key=0090e7f0";
        try {
            return webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToFlux(Vehicle.class)
                    .take(dataCount);
        } catch (DataNotFoundException dataNotFoundException) {
            throw new DataNotFoundException(
                    "data not found", dataNotFoundException);
        } catch (WebClientResponseException webClientResponseException) {
            throw new WebClientException(
                    "An error occurred during data retrieval.",
                    webClientResponseException) {
            };
        }
    }
}
