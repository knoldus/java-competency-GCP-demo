package com.nashtech.service.impl;


import com.nashtech.entity.CarEntity;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.CloudDataService;
import com.nashtech.service.ReactiveDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;



/**
 * Service implementation class for performing reactive data access
 * operations on cars.
 * This service interacts with the {@link CloudDataService}
 * to retrieve car data in a reactive manner.
 */
@Service
@Slf4j
public class ReactiveDataServiceImpl implements ReactiveDataService {

    /**
     * The WebClient for making HTTP requests to the external API.
     */
    private WebClient webClient;


    @Autowired
    private CloudDataService cloudDataService;

    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate and WebClient.
     */
    public ReactiveDataServiceImpl() {
        this.webClient = WebClient.create("https://my.api.mockaroo.com/");
    }

    /**
     * Fetches vehicle data from an external API.
     *
     * @return A Flux of VehicleDetails representing the fetched data.
     * Fetches vehicle data and sends it to the Kafka topic.
     * The data is sent with a delay of one second between
     * each element to simulate real-time behavior.
     */
    public void fetchAndSendData() {
        String apiUrl = "/vehicle.json?key=e60438e0";

        webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToFlux(Car.class)
                .switchIfEmpty(Flux.error(new WebClientException("Data Not Found") {
                }))
                .subscribe(
                        dataCar -> cloudDataService.pushData(dataCar)
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
    public Flux<CarBrand> getAllBrands() {
        return cloudDataService.getAllBrands();
    }

}
