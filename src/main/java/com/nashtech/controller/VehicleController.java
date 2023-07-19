package com.nashtech.vehicleapplication.controller;

import com.nashtech.vehicleapplication.exception.WebClientCustomException;
import com.nashtech.vehicleapplication.model.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

/**
 * Controller class for handling vehicle-related API requests.
 */
@Slf4j
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    /**
     * Retrieves vehicle data from an external API.
     *
     * @param dataCount The number of vehicle data
     *                   items to fetch (default: 50).
     * @return A Flux of Vehicle objects representing the fetched data.
     */
    @GetMapping("/data")
    public ResponseEntity<Flux<Vehicle>> getVehicleData(
            @RequestParam(name = "count", defaultValue = "50")
            final Integer dataCount) {
        WebClient webClient = WebClient.create();
        String apiUrl = "https://my.api.mockaroo.com/vehicle.json?key=0090e7f0";

        try {
            Flux<Vehicle> vehicleData = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToFlux(Vehicle.class)
                    .take(dataCount);

            return ResponseEntity.ok(vehicleData);
        } catch (WebClientResponseException webClientResponseException) {
            log.info("An error occurred: "
                    + webClientResponseException.getMessage());
            throw new WebClientCustomException(
                    "An error occurred during data retrieval.",
                    webClientResponseException);
        }
    }
}
