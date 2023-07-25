package com.nashtech.service;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import reactor.core.publisher.Flux;

/**
 * Interface representing a service for performing reactive data access
 * operations on cars.
 * It provides methods for obtaining cars with a specific brand and getting
 * distinct car brands in a reactive manner.
 */
public interface ReactiveDataService {

    /**
     * Retrieves a Flux of cars with the specified brand in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of CarEntity representing cars with the
     * specified brand.
     */
    Flux<Car> getCarsByBrand(String brand);

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to
     * for continuous updates.
     *
     * @return A Flux of String representing distinct car brands.
     */
    Flux<CarBrand> getAllBrands();
}


