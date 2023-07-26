package com.nashtech.entity;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;

/**
 * Represents a CarEntity.
 */
@Data
@Document(collectionName = "vehicle")
public class CarEntity {
    /**
     * The unique identifier of the car.
     */
    @DocumentId
    private String id;

    /**
     * The id of the car.
     */
    private Long carId;

    /**
     * The model of the car.
     */
    private String carModel;

    /**
     * The brand of the car.
     */
    private String brand;

    /**
     * The manufacturing year of the car.
     */
    private Integer year;

    /**
     * The color of the car.
     */
    private String color;

    /**
     * The mileage of the car.
     */
    private Double mileage;

    /**
     * The price of the car.
     */
    private Double price;

    /**
     * The location of the car.
     */
    private String location;
}
