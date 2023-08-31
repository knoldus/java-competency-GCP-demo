package com.nashtech.controller;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.ReactiveDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;

/**
 * Rest Controller class
 * which handles reactive data access for cars.
 * This controller provides endpoints for retrieving car data based on
 * the brand and getting distinct car brands.
 */
@RestController
@RequestMapping("v1/data")
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
     * Endpoint to retrieve data from mockaroo
     * and send vehicle data to the Event Hub.
     *
     * @return ResponseEntity with a success message
     * if data is sent successfully.
     */
    @PostMapping
    public ResponseEntity<Object> pushDataToCloud() {
        reactiveDataService.fetchAndSendData();
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    /**
     * Retrieves a stream of cars with the given brand.
     * The data is obtained using the reactive service and duplicates
     * are filtered out.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of Car representing cars with the
     * specified brand.
     */
    @Operation(summary = "Retrieves cars filtered by brand.",
            description = "The data is obtained using the reactive service"
                    + " and duplicates are filtered out.")
    @GetMapping(value = "/cars/{brand}", produces =
            MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Car> getCarsByBrand(
            @PathVariable final String brand) {
        return reactiveDataService.getCarsByBrand(brand);
    }

    /**
     * Retrieves a stream of distinct car brands.
     * The data is obtained using the reactive service and duplicates are
     * filtered out.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    @Operation(summary = "Retrieves unique car brands.",
            description = "The data is obtained using the reactive"
                    + " service and duplicates are filtered out.")
    @GetMapping(value = "/brands", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public Flux<CarBrand> getAllBrands() {
        return reactiveDataService.getAllBrands();
    }


    /**
     * Retrieves a stream of distinct car brands.
     *
     * The data is obtained using the reactive service and duplicates are
     * filtered out.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    @Operation(summary = "Retrieves unique car brands.",
            description = "The data is obtained using the reactive"
                    + " service and duplicates are filtered out.")
    @GetMapping(value = "/brands-sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Map<String, String>>> getAllBrandsSSE() {
        return reactiveDataService.getAllBrandsSse()
                .map(eventData -> ServerSentEvent.<Map<String, String>>builder()
                        .data(eventData.data())
                        .build());
    }


}
