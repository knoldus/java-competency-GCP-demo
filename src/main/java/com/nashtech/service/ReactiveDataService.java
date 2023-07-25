package com.nashtech.service;

/**
 * Service interface for retrieving vehicle data from External API.
 */
public interface ReactiveDataService {

    /**
     * Retrieves vehicle data from an external data source.
     *
     * @param dataCount The number of vehicle data items to fetch.
     */
    void getCarData(Integer dataCount);
}
