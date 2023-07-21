package com.nashtech.controller;

import com.nashtech.model.ReactiveCarDetailsDto;
import com.nashtech.model.ReactiveDataBrands;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Interface representing a reactive data controller for handling car
 * data retrieval.
 * It provides endpoints for obtaining distinct car brands and cars with
 * a specific brand in a reactive manner.
 */
@RestController
public interface ReactiveDataController {

    /**
     * Retrieves a stream of distinct car brands in a reactive manner.
     * The response will be in Server-Sent Events (SSE) format to allow
     * continuous data updates.
     *
     * @return A Flux of ReactiveDataBrands representing distinct
     * car brands.
     */
    @GetMapping(value = "/distinctBrand", produces =
            MediaType.APPLICATION_JSON_VALUE)
    Flux<ReactiveDataBrands> getDistinctBrand();

    /**
     * Retrieves a stream of cars with the specified brand in a
     * reactive manner.
     * The response will be in Server-Sent Events (SSE) format to allow
     * continuous data updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of ReactiveCarDetailsDto representing cars with the
     * specified brand.
     */
    @GetMapping(value = "/carsByBrand/{brand}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    Flux<ReactiveCarDetailsDto> getCarsByBrand(@PathVariable String brand);
}
