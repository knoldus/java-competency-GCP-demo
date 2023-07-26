package com.nashtech.service.impl;

import com.azure.cosmos.CosmosException;
import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.CosmosDbRepository;
import com.nashtech.service.CloudDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
@Slf4j
@Profile("cosmos")
public class CosmosDbService implements CloudDataService {


    /**
     * The reactive repository for {@link Car} entities
     * in Cosmos DB.
     * Used for performing CRUD operations and reactive data access.
     */
    @Autowired
    private CosmosDbRepository cosmosDbRepository;

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
        Flux<Car> allCarsOfBrand =
                cosmosDbRepository.getAllCarsByBrand(brand);
        return allCarsOfBrand
                .doOnError(error ->
                        log.error("Request Timeout"))
                .doOnComplete(() ->
                        log.info("Received Data Successfully"))
                .switchIfEmpty(Flux.error(new DataNotFoundException()));
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
        Flux<CarBrand> BrandsFlux =
                cosmosDbRepository.findDistinctBrands();
        return BrandsFlux
                .doOnError(error ->
                        log.error("Request Timeout"))
                .doOnComplete(() ->
                        log.info("Data processing completed."))
                .switchIfEmpty(Flux.error(new DataNotFoundException()));
    }
}
