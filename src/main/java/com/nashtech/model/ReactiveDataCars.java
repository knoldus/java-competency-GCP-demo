package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * A class representing the details of a vehicle.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReactiveDataCars {
    /**
     * The unique identifier of the vehicle.
     */
    private Integer cardId;
    /**
     * The brand of the vehicle.
     */
    private String brand;
    /**
     * The model of the vehicle.
     */
    private String model;
    /**
     * The year the vehicle was manufactured.
     */
    private Long year;
    /**
     * The color of the vehicle.
     */
    private String color;
    /**
     * The mileage of the vehicle.
     */
    private Double mileage;
    /**
     * The price of the vehicle.
     */
    private Double price;
}
