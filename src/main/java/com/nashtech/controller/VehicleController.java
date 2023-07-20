package com.nashtech.controller;

import com.nashtech.model.Vehicle;
import com.nashtech.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Controller class for handling vehicle-related API requests.
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    /**
     * The VehicleService used for handling vehicle-related operations.
     */
    private final VehicleService vehicleService;

    /**
     * Constructs a new VehicleController with the provided VehicleService.
     *
     * @param service The VehicleService to be used for
     *                      handling vehicle-related operations.
     */
    public VehicleController(final VehicleService service) {
        this.vehicleService = service;
    }

    /**
     * Retrieves vehicle data from an external API.
     *
     * @param dataCount The number of vehicle data
     *                 items to fetch (default: 50).
     * @return A ResponseEntity containing a Flux of Vehicle objects
     * representing the fetched data.
     */
    @GetMapping("/data")
    public ResponseEntity<Flux<Vehicle>> getVehicleData(
            @RequestParam(name = "count",
                    defaultValue = "50") final Integer dataCount) {
        Flux<Vehicle> vehicleData = vehicleService
                .getVehicleData(dataCount);
        return ResponseEntity.ok(vehicleData);
    }
}
