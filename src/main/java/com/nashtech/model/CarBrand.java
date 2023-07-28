package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Represents a reactive data model for sending
 * details of the Car to the CosmosDB.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarBrand {

    /**
     * The brand of the car.
     */
    private String brand;
}
