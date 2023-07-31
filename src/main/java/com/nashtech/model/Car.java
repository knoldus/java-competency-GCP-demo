package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Represents a reactive data model for sending
 * details of the Car to the CosmosDB.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    /**
     * The card ID of the car.
     */
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
