package com.nashtech.service;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * Interface representing a service for performing reactive data access
 * operations on cars.
 * It provides methods for obtaining cars with a specific brand and getting
 * distinct car brands in a reactive manner.
 */
public interface ReactiveDataService {

    /**
     * Retrieves car data from an external data source.
     */
    void fetchAndSendData();

/**
     * Retrieves a Flux of cars with the specified brand in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of Car representing cars with the
     * specified brand.
     */
    Flux<Car> getCarsByBrand(String brand);

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to
     * for continuous updates.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    Flux<CarBrand> getAllBrands();

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to
     * for continuous updates.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    Flux<ServerSentEvent<Map<String, String>>> getAllBrandsSse();
}

