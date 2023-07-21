package com.nashtech.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

/**
 * Represents a reactive data model for storing information about
 * cars in a Cosmos DB container.
 * This class is used for mapping car data to a container named
 * "DbContainer" in Cosmos DB.
 */
@Data
@Container(containerName = "DbContainer")
@Builder
@AllArgsConstructor
public class ReactiveDataCars {

    /**
     * The unique identifier of the car.
     */
    @Id
    private String id;

    /**
     * The card ID of the car.
     */
    private Integer cardId;

    /**
     * The brand of the car.
     */
    private String brand;

    /**
     * The model of the car.
     */
    @Version
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
