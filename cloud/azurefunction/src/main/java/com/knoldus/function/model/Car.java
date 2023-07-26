package com.knoldus.function.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class VehicleDetail {
    Integer carId;
    String brand;
    String model;
    Long year;
    String color;
    Double mileage;
    Double price;

}
