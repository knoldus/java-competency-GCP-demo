package com.nashtech.controller;

import com.nashtech.service.ReactiveDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling vehicle data operations.
 */
@RestController
@RequestMapping("/v1/data")
public class ReactiveDataController {
    /**
     * Service responsible for handling vehicle data operations.
     */
    private final ReactiveDataService dataService;
    /**
     * Constructor to inject the DataService dependency.
     *
     * @param dataService The DataService to be injected.
     */

    @Autowired
    public ReactiveDataController(final ReactiveDataService dataService) {
        this.dataService = dataService;
    }

    /**
     * Endpoint to retrieve data from mockaroo
     * and send vehicle data to the Event Hub.
     *
     * @return ResponseEntity with a success message if data is sent successfully.
     */
    @PostMapping
    public ResponseEntity<Object> sendDataToEventHub() {
        dataService.fetchAndSendData();
        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
