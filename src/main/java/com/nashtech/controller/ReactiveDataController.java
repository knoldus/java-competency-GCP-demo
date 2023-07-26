package com.nashtech.controller;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.ReactiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * Rest Controller class
 * which handles reactive data access for cars.
 * This controller provides endpoints for retrieving car data based on
 * the brand and getting distinct car brands.
 */
@RestController
public class ReactiveDataController {

    /**
     * The service implementation for reactive data access.
     * This service provides methods for retrieving and processing car data
     * in a reactive manner.
     * It is marked as 'final' to ensure immutability after initialization.
     */
    @Autowired
    private ReactiveDataService reactiveDataService;

    /**
     * Retrieves a stream of cars with the given brand at regular
     * intervals of 5 seconds.
     * The data is obtained using the reactive service and duplicates
     * are filtered out.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of Car representing cars with the
     * specified brand.
     */
    @GetMapping(value = "/cars/{brand}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Flux<Car> getCarsByBrand(
            @PathVariable final String brand) {
        return reactiveDataService.getCarsByBrand(brand);
    }

    /**
     * Retrieves a stream of distinct car brands at regular intervals of
     * 5 seconds.
     * The data is obtained using the reactive service and duplicates are
     * filtered out.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    @GetMapping(value = "/brands", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Flux<CarBrand> getAllBrands() {
        return reactiveDataService.getAllBrands();
    }

    /**
     * Endpoint to retrieve data from mockaroo
     * and send vehicle data to the Event Hub.
     *
     * @return ResponseEntity with a success message if data is sent successfully.
     */
    @PostMapping
    public ResponseEntity<Object> pushDataToCloud() {
        reactiveDataService.fetchAndSendData();
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}

