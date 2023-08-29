package com.nashtech.service.impl;

import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.CloudDataService;
import com.nashtech.service.ReactiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * Service class for handling car-related operations.
 */
@Slf4j
@Service
public class ReactiveDataServiceImpl implements
        ReactiveDataService {

    /**
     * WebClient instance for making HTTP requests to the external API.
     */
    @Autowired
    private WebClient webClient;

    /**
     * URL of the external API for retrieving vehicle data.
     */
    @Value("${apiUrl}")
    private String apiUrl;

    /**
     * The CloudDataService instance used to retrieve car information.
     */
    @Autowired
    private CloudDataService cloudDataService;


    /**
     * Retrieves car data from an external API.
     * @throws WebClientException If an error occurs during
     * data retrieval from the external API.
     */
    public void fetchAndSendData() {
        webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToFlux(Car.class)
                .onErrorResume(WebClientException.class, error -> {
                    log.error("Error occurred during data retrieval", error);
                    return Flux.error(
                            new WebClientException(
                                    "Failed to retrieve car data") {
                    });
                })
                .subscribe(
                        data -> cloudDataService.pushData(data)
                );
    }

    /**
     * Retrieves a Flux of cars with specified brand in reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     *
     * @param brand The brand of cars to filter by.
     * @return A Flux of Car representing cars with the
     * specified brand.
     */
    @Override
    public Flux<Car> getCarsByBrand(final String brand) {
            return cloudDataService.getCarsByBrand(brand);
    }

    /**
     * Retrieves a Flux of distinct car brands in a reactive manner.
     * The Flux represents a stream of data that can be subscribed to for
     * continuous updates.
     * This method also prints the distinct brands to the console for
     * demonstration purposes.
     *
     * @return A Flux of CarBrand representing distinct car brands.
     */
    @Override
    public Flux<CarBrand> getAllBrands() {
        return cloudDataService.getAllBrands();
    }

    @Override
    public Flux<ServerSentEvent<Map<String, String>>> getAllBrandsSse() {
        return cloudDataService.getAllBrandsSse();
    }


}
