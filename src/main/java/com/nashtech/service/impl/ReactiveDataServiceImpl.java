package com.nashtech.service.impl;

import com.nashtech.exception.ResourceNotFoundException;
import com.nashtech.model.ReactiveCarDetailsDto;
import com.nashtech.model.ReactiveDataBrands;
import com.nashtech.model.ReactiveDataCars;
import com.nashtech.repository.ReactiveDataRepository;
import com.nashtech.service.ReactiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Service implementation class for performing reactive data access
 * operations on cars.
 * This service interacts with the {@link ReactiveDataRepository}
 * to retrieve car data in a reactive manner.
 */
@Service
@Slf4j
public class ReactiveDataServiceImpl implements ReactiveDataService {

    /**
     * The reactive repository for {@link ReactiveDataCars} entities
     * in Cosmos DB.
     * Used for performing CRUD operations and reactive data access.
     */
    private final ReactiveDataRepository reactiveDataRepository;

    /**
     * A logger instance for logging messages and events in the class.
     */
    private static Logger logger =
            LoggerFactory.getLogger(ReactiveDataServiceImpl.class);

    /**
     * Constructs a new ReactiveDataServiceImpl with the specified
     * ReactiveDataRepository.
     *
     * @param reactiveDataRepositoryImpl The ReactiveDataRepository to
     * be used for data access.
     */
    @Autowired
    public ReactiveDataServiceImpl(final
            ReactiveDataRepository reactiveDataRepositoryImpl) {
        this.reactiveDataRepository = reactiveDataRepositoryImpl;
    }

    /**
     * Retrieves a Flux of cars with specified brand in reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of ReactiveCarDetailsDto representing cars with the
     * specified brand.
     */
    public Flux<ReactiveCarDetailsDto> getCarByBrand(final String brand) {
            Flux<ReactiveCarDetailsDto> allCarsOfBrand =
                    reactiveDataRepository.getAllCars(brand);
        return allCarsOfBrand
                .doOnComplete(() -> logger.info("Received Data Successfully"))
                .switchIfEmpty(Flux.error(new ResourceNotFoundException()));
    }

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     * This method also prints the distinct brands to the console for
     * demonstration purposes.
     *
     * @return A Flux of ReactiveDataBrands representing distinct car brands.
     */
    public Flux<ReactiveDataBrands> getDistinctBrands() {
        Flux<ReactiveDataBrands> distinctBrandsFlux =
                reactiveDataRepository.findDistinctBrands();
            return distinctBrandsFlux
                    .doOnNext(brand ->
                            logger.info("Distinct Brand: " + brand))
                    .doOnError(error ->
                            logger.error("Error occurred: " + error))
                    .doOnComplete(() ->
                            logger.info("Data processing completed."))
                    .switchIfEmpty(Flux.error(new ResourceNotFoundException()));
    }

}
