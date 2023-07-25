package com.nashtech.service.impl;


import com.nashtech.model.ReactiveDataCar;
import com.nashtech.service.CloudDataService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class CloudDataServiceImpl implements CloudDataService {

    /**
     * The KafkaTemplate for sending vehicle data to Kafka topics.
     */
    private final KafkaTemplate<String, ReactiveDataCar> kafkaTemplate;


    /**
     * Constructor to initialize the DataService
     * with KafkaTemplate.
     */
    public CloudDataServiceImpl(KafkaTemplate<String, ReactiveDataCar> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendData(ReactiveDataCar reactiveDataCar) {
        Message<ReactiveDataCar> message = MessageBuilder
                .withPayload(reactiveDataCar)
                .setHeader(KafkaHeaders.TOPIC, "myeventhub")
                .build();
        kafkaTemplate.send(message);
    }
}
