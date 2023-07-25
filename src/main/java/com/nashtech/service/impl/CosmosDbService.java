package com.nashtech.service.impl;


import com.nashtech.entity.CarEntity;
import com.nashtech.service.CloudDataService;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class CosmosDbService implements CloudDataService {

    /**
     * The KafkaTemplate for sending vehicle data to Kafka topics.
     */
    private final KafkaTemplate<String, CarEntity> kafkaTemplate;


    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate.
     */
    public CosmosDbService(KafkaTemplate<String, CarEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends the given {@link CarEntity} object to the Kafka topic "myeventhub".
     * The method constructs a Kafka message from the provided {@link CarEntity} payload
     * and sends it using the configured {@link KafkaTemplate}.
     *
     * @param reactiveDataCar The {@link CarEntity} object to be sent to Kafka.
     * @throws KafkaException If an error occurs while sending the message to Kafka.
     */
    @Override
    public void pushData(CarEntity reactiveDataCar) throws KafkaException {
        Message<CarEntity> message = MessageBuilder
                .withPayload(reactiveDataCar)
                .setHeader(KafkaHeaders.TOPIC, "myeventhub")
                .build();
        kafkaTemplate.send(message);
    }
}
