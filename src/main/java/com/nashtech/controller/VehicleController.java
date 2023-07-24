package com.nashtech.controller;

import com.nashtech.model.VehicleDTO;
import com.nashtech.service.serviceimpl.VehicleServiceImpl;
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
public class VehicleController {

    /**
     * The VehicleService instance used to retrieve vehicle information.
     */
    @Autowired
    private  VehicleServiceImpl vehicleServiceImpl;

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
        return  vehicleServiceImpl.getAllBrandNames()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           ;
    }

    /**
     * Retrieves vehicle details
     * for a specific brand name in a streaming fashion.
     *
     * @param brand The brand name of the vehicles to retrieve details for.
     * @return A Flux of VehicleDTO
     * representing the details of vehicles with the given brand name.
     */
    @GetMapping(value = "/brands/{brand}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<VehicleDTO> findDetailsByBrandName(
            @PathVariable final String brand) {
        return vehicleServiceImpl.getDetailsByBrandName(brand);
    }

}
