package com.nashtech.service;

import com.nashtech.entity.CarEntity;
import org.springframework.kafka.KafkaException;

public interface CloudDataService {
    void pushData(CarEntity reactiveDataCar) throws KafkaException;
}
