package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
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

}
