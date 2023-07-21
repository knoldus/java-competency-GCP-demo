package com.nashtech.service;

import com.nashtech.model.Vehicle;
import reactor.core.publisher.Flux;

/**
 * Service interface for retrieving vehicle data from External API.
 */
public interface VehicleService {

    /**
     * Retrieves vehicle data from an external data source.
     *
     * @param dataCount The number of vehicle data items to fetch.
     * @return A Flux of Vehicle objects representing the fetched data.
     */
    Flux<Vehicle> getVehicleData(Integer dataCount);
}
