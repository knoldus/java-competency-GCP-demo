package com.nashtech.controller;

import com.nashtech.model.ReactiveDataCars;
import com.nashtech.service.ReactiveDataService;
import com.nashtech.service.ReactivePublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.io.IOException;

/**
 * Controller class to handle Vehicle related endpoints.
 */
@RestController
@RequestMapping("/vehicle")
public class ReactiveDataController {

    @Autowired
    private ReactiveDataService reactiveDataService;

    @Autowired
    private ReactivePublisherService publisher;

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
     * @return ResponseEntity containing Flux of Vehicle data.
     * @throws IOException if there is an error in reading data from external source.
     */
    @GetMapping("/data")
    public Flux<ReactiveDataCars> getCarData(
            @RequestParam(name = "count", defaultValue = DEFAULT_DATA_COUNT)
            Integer dataCount) throws IOException {
        Flux<ReactiveDataCars> carData = reactiveDataService.getCarData(dataCount);
        publisher.publishCarData(carData).subscribe();
        return carData;
    }
}
