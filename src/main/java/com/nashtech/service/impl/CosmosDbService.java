package com.nashtech.service.impl;

import com.nashtech.service.CloudDataService;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
@Slf4j
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
    private final KafkaTemplate<String, Car> kafkaTemplate;


    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate.
     */
    public CosmosDbService(KafkaTemplate<String, Car> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends the given {@link Car} object to the Kafka topic "myeventhub".
     * The method constructs a Kafka message from the provided {@link Car} payload
     * and sends it using the configured {@link KafkaTemplate}.
     *
     * @param reactiveDataCar The {@link Car} object to be sent to Kafka.
     * @throws KafkaException If an error occurs while sending the message to Kafka.
     */
    @Override
    public Mono<Void> pushData(Car reactiveDataCar) throws KafkaException {
        Message<Car> message = MessageBuilder
                .withPayload(reactiveDataCar)
                .setHeader(KafkaHeaders.TOPIC, "myeventhub")
                .build();
        kafkaTemplate.send(message);
        return null;
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
     public Flux<Car> getCarsByBrand ( final String brand){
         Flux<Car> allCarsOfBrand = cosmosDbRepository.getAllCarsByBrand(brand);
         return allCarsOfBrand
                 .doOnError(error ->
                         log.error("Request Timeout"))
                 .doOnComplete(() ->
                         log.info("Received Data Successfully"))
                 .switchIfEmpty(Flux.error(new DataNotFoundException()));
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
     public Flux<CarBrand> getAllBrands () {
         Flux<CarBrand> BrandsFlux =
                 cosmosDbRepository.findDistinctBrands();
         return BrandsFlux
                 .doOnError(error ->
                         log.error("Request Timeout"))
                 .doOnComplete(() ->
                         log.info("Data processing completed."))
                 .switchIfEmpty(Flux.error(new DataNotFoundException()));
        }
    }
