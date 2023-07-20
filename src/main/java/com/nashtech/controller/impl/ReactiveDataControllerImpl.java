package com.nashtech.controller.impl;

import com.nashtech.controller.ReactiveDataController;
import com.nashtech.model.ReactiveCarDetailsDto;
import com.nashtech.model.ReactiveDataBrands;
import com.nashtech.service.impl.ReactiveDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;

/**
 * Implementation class for the {@link ReactiveDataController} interface,
 * which handles reactive data access for cars.
 * This controller provides endpoints for retrieving car data based on
 * the brand and getting distinct car brands.
 */
@Component
public class ReactiveDataControllerImpl implements ReactiveDataController {

    /**
     * The service implementation for reactive data access.
     * This service provides methods for retrieving and processing car data
     * in a reactive manner.
     * It is marked as 'final' to ensure immutability after initialization.
     */
    private ReactiveDataServiceImpl reactiveDataService;

    /**
     * Time interval in minutes.
     * This constant defines the value for a time interval of 5 minutes.
     * It is marked as 'static' and 'final' to indicate that it is a
     * constant value and cannot be modified.
     */
    private static final Integer TIME_IN_MINUTES = 5;

    /**
     * Constructs a new ReactiveDataControllerImpl with the specified
     * ReactiveDataServiceImpl.
     *
     * @param reactiveDataServiceImpl The ReactiveDataServiceImpl to be
     * used for data access.
     */
    @Autowired
    public ReactiveDataControllerImpl(final ReactiveDataServiceImpl
    reactiveDataServiceImpl) {
        this.reactiveDataService = reactiveDataServiceImpl;
    }

    /**
     * Retrieves a stream of cars with the given brand at regular
     * intervals of 5 seconds.
     * The data is obtained using the reactive service and duplicates
     * are filtered out.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of ReactiveDataCars representing cars with the
     * specified brand.
     */
    public Flux<ReactiveCarDetailsDto> getCarsByBrand(
            @PathVariable final String brand) {
        return reactiveDataService.getCarByBrand(brand);
    }

    /**
     * Retrieves a stream of distinct car brands at regular intervals of
     * 5 seconds.
     * The data is obtained using the reactive service and duplicates are
     * filtered out.
     *
     * @return A Flux of String representing distinct car brands.
     */
    public Flux<ReactiveDataBrands> getDistinctBrand() {
        return reactiveDataService.getDistinctBrands();
    }

}

