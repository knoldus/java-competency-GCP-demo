package com.nashtech.service;

import com.nashtech.model.Vehicle;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Service interface for publishing vehicle data to the pub/sub topic.
 */
public interface VehiclePublisherService {

    /**
     * Publishes vehicle data to the pub/sub topic.
     *
     * @param vehicles A Flux of Vehicle objects representing the data to be published.
     * @return A Mono representing the completion of the publishing process.
     * @throws IOException If an I/O error occurs during the publishing process.
     */
    Mono<Void> publishVehicleData(
            Flux<Vehicle> vehicles) throws IOException;
}
