package com.nashtech.repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.nashtech.entity.GCPCarEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * This repository interface provides methods for
 * performing CRUD operations on vehicles in Firestore.
 */
@Repository
@Profile("firestore")
public interface FirestoreDbRepository extends
        FirestoreReactiveRepository<GCPCarEntity> {

        /**
         * Retrieves a Flux of CarEntity objects
         * filtered by the specified brand name.
         * @param brand The brand name of the vehicles to retrieve.
         * @return A Flux emitting CarEntity objects
         * matching the specified brand name.
         */
        Flux<GCPCarEntity> findByBrand(String brand);
}
