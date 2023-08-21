package com.nashtech.model;

import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.google.cloud.spring.data.firestore.Document;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.stereotype.Service;

/**
 * Represents a reactive data model for sending
 * details of the Car to the CosmosDB.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    /**
     * The card ID of the car.
     */
    @Id
    private Integer carId;

    /**
     * The brand of the car.
     */
    private String brand;

    /**
     * The model of the car.
     */
    private String model;

    /**
     * The year of manufacture of the car.
     */
    private Long year;

    /**
     * The color of the car.
     */
    private String color;

    /**
     * The mileage of the car in kilometers.
     */
    private Double mileage;

    /**
     * The price of the car.
     */
    private Double price;
}
