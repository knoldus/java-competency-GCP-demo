package com.nashtech.controller;

import com.nashtech.model.CarBrand;
import com.nashtech.model.Car;
import com.nashtech.service.ReactiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * This controller class handles the endpoints related to
 * vehicle information retrieval.
 */
@RestController
public class ReactiveDataController {

    /**
     * The ReactiveDataService instance used to retrieve vehicle information.
     */
    @Autowired
    private ReactiveDataService reactiveDataService;

    /**
     * Retrieves a Flux of unique brand names of vehicles
     * at regular intervals.
     *
     * @return a Flux of String representing the unique
     * brand names of vehicles
     */
    @GetMapping(value = "/brands", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Flux<CarBrand> getAllBrands() {
        return reactiveDataService.getAllBrands();
    }

    /**
     * Retrieves vehicle details
     * for a specific brand name in a streaming fashion.
     *
     * @param brand The brand name of the vehicles to retrieve details for.
     * @return A Flux of Car
     * representing the details of vehicles with the given brand name.
     */
    @GetMapping(value = "/brands/{brand}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Car> getCarsByBrand(
            @PathVariable final String brand) {
        return reactiveDataService
                .getCarsByBrand(brand);
    }

}
