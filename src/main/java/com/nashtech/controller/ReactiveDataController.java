package com.nashtech.controller;

import com.nashtech.service.ReactiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class to handle Vehicle related endpoints.
 */
@RestController
@RequestMapping("/vehicle")
public class ReactiveDataController {

    /**
     * Service responsible for handling car data operations.
     *
     */
    @Autowired
    private ReactiveDataService reactiveDataService;

    /**
     * Default count value for getting vehicle data
     * if count parameter is not provided.
     */
    private static final String DEFAULT_DATA_COUNT = "30";

    /**
     * Get vehicle data from the VehicleService and
     * publish it to the VehiclePublisherService.
     *
     * @param dataCount The number of vehicle data items to retrieve.
     * containing Flux of Vehicle data.
     * in reading data from external source.
     */
    @GetMapping("/data")
    public void getVehicleData(@RequestParam(
            name = "count", defaultValue =
            DEFAULT_DATA_COUNT) final Integer dataCount) {
        reactiveDataService.getCarData(dataCount);

    }
}
