package com.nashtech.service.impl;


import com.nashtech.model.DataCar;
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
    private final KafkaTemplate<String, DataCar> kafkaTemplate;


    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate.
     */
    public CosmosDbService(KafkaTemplate<String, DataCar> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    /**
     * Sends the given {@link DataCar} object to the Kafka topic "myeventhub".
     * The method constructs a Kafka message from the provided {@link DataCar} payload
     * and sends it using the configured {@link KafkaTemplate}.
     *
     * @param reactiveDataCar The {@link DataCar} object to be sent to Kafka.
     * @throws KafkaException If an error occurs while sending the message to Kafka.
     */
    @Override
    public void sendData(DataCar reactiveDataCar) {
        Message<DataCar> message = MessageBuilder
                .withPayload(reactiveDataCar)
                .setHeader(KafkaHeaders.TOPIC, "myeventhub")
                .build();
        kafkaTemplate.send(message);
    }
}
