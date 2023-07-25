package com.nashtech.service.impl;

import com.nashtech.model.CarBrand;
import com.nashtech.model.Car;
import com.nashtech.service.CloudDataService;
import com.nashtech.service.ReactiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service implementation for handling car-related operations.
 */
@Service
public class ReactiveDataServiceImpl implements ReactiveDataService {

    /**
     * The CloudDataService instance used to retrieve car information.
     */
    @Autowired
    private CloudDataService cloudDataService;

    /**
     * Retrieves all car brands.
     *
     * @return A Flux emitting CarBrand objects
     * representing all available car brands.
     */
    @Override
    public Flux<CarBrand> getAllBrands() {
        return cloudDataService.getAllBrands();
    }
    /**
     * Retrieves cars belonging to the specified brand.
     *
     * @param brand The brand name for which to fetch the cars.
     * @return A Flux emitting Car objects
     * representing the cars belonging to the specified brand.
     */
    @Override
    public Flux<Car> getCarsByBrand(final String brand) {
        return cloudDataService.getCarsByBrand(brand);
    }
}
