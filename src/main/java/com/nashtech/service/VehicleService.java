package com.nashtech.service;

import com.google.cloud.firestore.FirestoreException;
import com.google.cloud.spring.data.firestore.FirestoreDataException;
import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Vehicle;
import com.nashtech.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * This service class provides methods to retrieve information
 * related to vehicles.
 */
@Service
public class VehicleService {

    /**
     * The VehicleRepository instance used to retrieve vehicle information.
     */
    @Autowired
    private VehicleRepository vehicleRepository;

    /**
     * Retrieves a flux of distinct brand names of vehicles.
     *
     * @return a Flux of String representing the distinct
     * brand names of vehicles
     */
    public Flux<String> getAllBrandNames() {
        try {
            return vehicleRepository.findAll()
                    .filter(vehicle -> vehicle.getBrand() != null)
                    .map(Vehicle::getBrand)
                    .distinct();
        } catch (DataNotFoundException dataNotFoundException) {
            throw  new DataNotFoundException (
                    "Data Not Found", dataNotFoundException);
        } catch (FirestoreException firestoreException) {
            throw new FirestoreDataException(
                    "Some error occured, unable to fetch the data",
                    firestoreException
            );
        }

    }
}
