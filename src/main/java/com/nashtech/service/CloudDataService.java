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
     * Gets vehicle details for the specified brand.
     * @param brand The brand name for
     *              which vehicle details are requested.
     * @return A Flux emitting Car objects with details.
     */
    Flux<Car> getCarsByBrand(String brand);
}
