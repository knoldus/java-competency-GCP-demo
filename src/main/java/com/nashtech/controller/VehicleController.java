package com.nashtech.controller;

import com.nashtech.service.VehicleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;

/**
 * This controller class handles the endpoints related to
 * vehicle information retrieval.
 */
@RestController
public class VehicleController {

    /**
     * The VehicleService instance used to retrieve vehicle information.
     */
    private static VehicleService vehicleService;

    /**
     * Constructs a VehicleController
     * instance with the provided VehicleService.
     *
     * @param service the VehicleService to be used
     *                for retrieving vehicle information
     */
    public VehicleController(final VehicleService service) {
        this.vehicleService = service;
    }

     /**
     * The duration of the interval,
      * in seconds, for retrieving unique brand names of vehicles.
     */
    private static final Integer DURATION_OF_INTERVAL = 5;

    /**
     * Retrieves a Flux of unique brand names of vehicles
     * at regular intervals.
     *
     * @return a Flux of String representing the unique
     * brand names of vehicles
     */
    @GetMapping(value = "/brands", produces =
            MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> findAllUniqueBrands() {
        return Flux.interval(Duration.ofSeconds(DURATION_OF_INTERVAL))
                .flatMap(ignore -> vehicleService.getAllBrandNames())
                .distinct();
    }

}
