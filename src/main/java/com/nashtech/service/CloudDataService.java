package com.nashtech.service;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface CloudDataService {

    /**
     * Publishes vehicle data to the pub/sub topic.
     *
     * @param carData A Flux of Car objects representing
     *                the data to be published.
     * @return A Mono representing
     * the completion of the publishing process.
     * @throws Exception If an error occurs during the publishing process.
     */
    Mono<Void> pushData(
            Car carData);

    /**
     * Retrieves a Flux of cars with the specified brand in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of Car representing cars with the
     * specified brand.
     */
    Flux<Car> getCarsByBrand(String brand);

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to
     * for continuous updates.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    Flux<CarBrand> getAllBrands();

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to
     * for continuous updates.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    Flux<ServerSentEvent<Map<String, String>>> getAllBrandsSse();



}
