package com.nashtech.service.impl;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.CloudDataService;
import com.nashtech.service.ReactiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service implementation class for performing reactive data access
 * operations on cars.
 * This service interacts with the {@link CloudDataService}
 * to retrieve car data in a reactive manner.
 */
@Service
@Slf4j
public class ReactiveDataServiceImpl implements ReactiveDataService {

    /**
     * The reactive Service for {@link Car} entities
     * in Cosmos DB.
     * Used for performing CRUD operations and reactive data access.
     */
    @Autowired
    private CloudDataService cloudDataService;

    /**
     * Retrieves a Flux of cars with specified brand in reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of Car representing cars with the
     * specified brand.
     */
    public Flux<Car> getCarsByBrand(final String brand) {
            return cloudDataService.getCarsByBrand(brand);
    }

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     * This method also prints the distinct brands to the console for
     * demonstration purposes.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    public Flux<CarBrand> getAllBrands() {
        return cloudDataService.getAllBrands();
    }

}
