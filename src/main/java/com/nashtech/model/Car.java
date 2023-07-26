package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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


    /**
     * Sets the ID of the car.
     *
     * @param id The ID of the car.
     * @return This Car instance.
     */
    public Car setCarId(final Long id) {
        this.carId = id;
        return this;
    }

    /**
     * Sets the model of the car.
     *
     * @param model The model of the car.
     * @return This Car instance.
     */
    public Car setCarModel(final String model) {
        this.carModel = model;
        return this;
    }

    /**
     * Sets the brand of the car.
     *
     * @param carBrand The brand of the car.
     * @return This Car instance.
     */
    public Car setBrand(final String carBrand) {
        this.brand = carBrand;
        return this;
    }

    /**
     * Sets the manufacturing year of the car.
     *
     * @param carYear The manufacturing year of the car.
     * @return This Car instance.
     */
    public Car setYear(final Integer carYear) {
        this.year = carYear;
        return this;
    }

    /**
     * Sets the color of the car.
     *
     * @param carColor The color of the car.
     * @return This Car instance.
     */
    public Car setColor(final String carColor) {
        this.color = carColor;
        return this;
    }

    /**
     * Sets the mileage of the car.
     *
     * @param carMileage The mileage of the car.
     * @return This Car instance.
     */
    public Car setMileage(final Double carMileage) {
        this.mileage = carMileage;
        return this;
    }

    /**
     * Sets the price of the car.
     *
     * @param carPrice The price of the car.
     * @return This Car instance.
     */
    public Car setPrice(final Double carPrice) {
        this.price = carPrice;
        return this;
    }

    /**
     * Builds and returns the Car instance with the provided attributes.
     *
     * @return This Car instance.
     */
    public Car build() {
        return this;
    }
}
