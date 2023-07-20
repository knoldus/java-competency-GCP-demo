package com.nashtech.repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.nashtech.model.Vehicle;
import org.springframework.stereotype.Repository;

/**
 * This repository interface provides methods for
 * performing CRUD operations on vehicles in Firestore.
 */
@Repository
public interface VehicleRepository extends
        FirestoreReactiveRepository<Vehicle> {

}
