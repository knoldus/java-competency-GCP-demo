package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a reactive data model for sending
 * details of the Car to the CosmosDB.
 */
@Data
@AllArgsConstructor
public class ReactiveDataBrands {

    /**
     * The brand of the car.
     */
    private String brand;
}
