package com.nashtech.service.impl;

import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.nashtech.service.CloudDataService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.CosmosDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;


@Service
@Slf4j
@Profile("cosmos")
public class CosmosDbService implements CloudDataService {
    /**
     * The reactive repository for {@link Car} entities
     * in Cosmos DB.
     * Used for performing CRUD operations and reactive data access.
     */
    @Autowired
    private CosmosDbRepository cosmosDbRepository;

    /**
     * The KafkaTemplate for sending vehicle data to Kafka topics.
     */
    @Autowired
    private  KafkaTemplate<String, Car> kafkaTemplate;

    /**
     * Event hub topic name.
     */
    @Value("${eventhub.name}")
    private String eventHubName;


    /**
     * Sends the given {@link Car} object to the Kafka topic
     * The method constructs a Kafka message
     * from the provided {@link Car} payload
     * and sends it using the configured {@link KafkaTemplate}.
     *
     * @param reactiveDataCar The {@link Car} object to be sent to Kafka.
     * @throws KafkaException
     * If an error occurs while sending the message to Kafka.
     */
    @Override
    public Mono<Void> pushData(final Car reactiveDataCar)  {
        try {
            Message<Car> message = MessageBuilder
                    .withPayload(reactiveDataCar)
                    .setHeader(KafkaHeaders.TOPIC, eventHubName)
                    .build();
            kafkaTemplate.send(message);
        } catch (KafkaException kafkaException) {
            throw kafkaException;
        }
        return Mono.empty();
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
        Flux<Car> allCarsOfBrand = cosmosDbRepository.getAllCarsByBrand(brand);
        return allCarsOfBrand
                .doOnComplete(() -> log.info("Received Data Successfully"))
                .switchIfEmpty(Flux.error(new DataNotFoundException()))
                .onErrorResume(CosmosAccessException.class, error -> {
                    log.error("Error while retrieving data: {}",
                            error.getMessage());
                    return Flux.error(
                            new CosmosAccessException("Failed to retrieve data",
                                    error)
                    );
                });

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
        Flux<CarBrand> brandFlux =
                cosmosDbRepository.findDistinctBrands();
        return brandFlux
                .doOnComplete(() -> log.info("Received Brands Successfully"))
                .switchIfEmpty(Flux.error(new DataNotFoundException()))
                .onErrorResume(CosmosAccessException.class, error -> {
                    log.error("Error while retrieving data: {}",
                            error.getMessage());
                    return Flux.error(
                            new CosmosAccessException("Failed to retrieve data",
                                    error)
                    );
                });
    }

    @Override
    public Flux<ServerSentEvent<Map<String, String>>> getAllBrandsSse() {
        return null;
    }

}
