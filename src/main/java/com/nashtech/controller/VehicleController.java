package com.nashtech.controller;

import com.nashtech.model.Vehicle;
import com.nashtech.service.impl.VehiclePublisherServiceImpl;
import com.nashtech.service.impl.VehicleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class VehicleController {

    @Autowired
    private VehicleServiceImpl vehicleService;

    @Autowired
    private VehiclePublisherServiceImpl publisher;

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
    public ResponseEntity<Flux<Vehicle>> getVehicleData(@RequestParam(
            name = "count", defaultValue =
            DEFAULT_DATA_COUNT) Integer dataCount) throws IOException {
        Flux<Vehicle> vehicleData = vehicleService.getVehicleData(dataCount);
        publisher.publishVehicleData(vehicleData).subscribe();
        return ResponseEntity.ok(vehicleData);
    }
}
