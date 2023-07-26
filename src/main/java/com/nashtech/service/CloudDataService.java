package com.nashtech.service;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public interface CloudDataService {

    /**
     * Gets all brand names available for vehicles.
     * @return A Flux emitting brand names as strings.
     */
    Flux<CarBrand> getAllBrands();

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
}
