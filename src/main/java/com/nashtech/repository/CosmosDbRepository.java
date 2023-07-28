package com.nashtech.repository;

import com.azure.spring.data.cosmos.repository.Query;
import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.nashtech.entity.AzureCarEntity;
import com.nashtech.model.CarBrand;
import com.nashtech.model.Car;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Reactive data repository interface for performing CRUD operations
 * on {@link AzureCarEntity} entities in Cosmos DB.
 * This interface extends {@link ReactiveCosmosRepository} and inherits
 * common CRUD methods for reactive data access.
 *
 * @see AzureCarEntity
 * @see ReactiveCosmosRepository
 */
@Repository
@Profile("cosmos")
public interface CosmosDbRepository extends ReactiveCosmosRepository
        <AzureCarEntity, String> {

    /**
     * Custom query to get the brands from CosmosDB.
     * document from database which
     * @return the CarBrand with brands.
     */
    @Query(value = "SELECT DISTINCT c.brand FROM c")
    Flux<CarBrand> findDistinctBrands();

    /**
     * Custom query to get the details from CosmosDB.
     * document from database which
     * @return the Car with
     * @param brand for the specific brand.
     */
    @Query(value = "SELECT * FROM c WHERE c.brand = @brand")
    Flux<Car> getAllCarsByBrand(String brand);
}
