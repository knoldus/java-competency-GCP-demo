package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleDTO {
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
    @Override
    public  final int hashCode() {
        // Implement a custom hashCode based on the fields that uniquely identify a VehicleDTO
        return Objects.hash(carId, carModel, brand, year, color, mileage, price);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        VehicleDTO other = (VehicleDTO) obj;
        return Objects.equals(carId, other.carId) &&
                Objects.equals(carModel, other.carModel) &&
                Objects.equals(brand, other.brand) &&
                Objects.equals(year, other.year) &&
                Objects.equals(color, other.color) &&
                Objects.equals(mileage, other.mileage) &&
                Objects.equals(price, other.price);
    }

}
