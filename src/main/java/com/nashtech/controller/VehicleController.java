package com.nashtech.controller;

import com.nashtech.mockaroodata.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling vehicle data operations.
 */
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    /**
     * Service responsible for handling vehicle data operations.
     */
    private final VehicleService dataService;
    /**
     * Constructor to inject the DataService dependency.
     *
     * @param dataService The DataService to be injected.
     */
    @Autowired
    public VehicleController(final VehicleService dataService) {
        this.dataService = dataService;
    }

    /**
     * Endpoint to retrieve data from mockaroo
     * and send vehicle data to the Event Hub.
     *
     * @return ResponseEntity with a success message if data is sent successfully.
     */
    @PostMapping(value = "/data")
    public ResponseEntity<String> sendDataToEventHub() {
        dataService.fetchData();
        dataService.sendData();
        return ResponseEntity.ok("Json Message sent to the topic!");

    }
}
