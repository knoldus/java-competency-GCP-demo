package com.nashtech.repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.nashtech.model.Vehicle;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * This repository interface provides methods for
 * performing CRUD operations on vehicles in Firestore.
 */
@Repository
public interface VehicleRepository extends
        FirestoreReactiveRepository<Vehicle> {

        /**
         * Retrieves a Flux of Vehicle objects
         * filtered by the specified brand name.
         * @param brand The brand name of the vehicles to retrieve.
         *
         */
        Flux<Vehicle> findByBrand(String brand);
}
