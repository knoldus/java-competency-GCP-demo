package com.nashtech.service;

import com.google.cloud.spring.data.firestore.FirestoreDataException;
import com.nashtech.entity.GCPCarEntity;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.FirestoreDbRepository;
import com.nashtech.service.impl.FirestoreDbService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest
public class FirestoreDbServiceTest {
    @Mock
    private FirestoreDbRepository firestoreDbRepository;

    @InjectMocks
    private FirestoreDbService firestoreDbService;

    @Test
    public void testGetAllBrands() {
        // Setup
        // Configure FirestoreDbRepository.findAll(...).
        final GCPCarEntity gcpCarEntity = new GCPCarEntity();
        gcpCarEntity.setCarId(0L);
        gcpCarEntity.setCarModel("carModel");
        gcpCarEntity.setBrand("brand");
        gcpCarEntity.setYear(2020);
        gcpCarEntity.setColor("color");
        gcpCarEntity.setMileage(0.0);
        gcpCarEntity.setPrice(0.0);
        final Flux<GCPCarEntity> gcpCarEntityFlux = Flux.just(gcpCarEntity);
        when(firestoreDbRepository.findAll()).thenReturn(gcpCarEntityFlux);

        // Run the test
        final Flux<CarBrand> result = firestoreDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectNextMatches(carBrand -> carBrand.getBrand().equals("brand"))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetAllBrands_FirestoreDbRepositoryReturnsError() {
        // Setup
        // Configure FirestoreDbRepository.findAll(...).
        final Flux<GCPCarEntity> gcpCarEntityFlux = Flux.error(new FirestoreDataException("message"));
        when(firestoreDbRepository.findAll()).thenReturn(gcpCarEntityFlux);

        // Run the test
        final Flux<CarBrand> result = firestoreDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
//                .expectErrorMatches(throwable -> throwable.getMessage().equals("message"))
                .expectError(FirestoreDataException.class)
                .verify();
    }

    @Test
    public void testGetAllBrands_FirestoreDbRepositoryReturnsNoItem() {
        // Setup
        when(firestoreDbRepository.findAll()).thenReturn(Flux.empty());

        // Run the test
        final Flux<CarBrand> result = firestoreDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetCarsByBrand() {
        // Setup
        // Configure FirestoreDbRepository.findByBrand(...).
        final GCPCarEntity gcpCarEntity = new GCPCarEntity();
        gcpCarEntity.setCarId(0L);
        gcpCarEntity.setCarModel("carModel");
        gcpCarEntity.setBrand("brand");
        gcpCarEntity.setYear(2020);
        gcpCarEntity.setColor("color");
        gcpCarEntity.setMileage(0.0);
        gcpCarEntity.setPrice(0.0);
        final Flux<GCPCarEntity> gcpCarEntityFlux = Flux.just(gcpCarEntity);
        when(firestoreDbRepository.findByBrand("brand")).thenReturn(gcpCarEntityFlux);

        // Run the test
        final Flux<Car> result = firestoreDbService.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectNextMatches(car -> car.getBrand().equals("brand"))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetCarsByBrand_FirestoreDbRepositoryReturnsError() {
        // Setup
        // Configure FirestoreDbRepository.findByBrand(...).
        final Flux<GCPCarEntity> gcpCarEntityFlux = Flux.error(new Exception("message"));
        when(firestoreDbRepository.findByBrand("brand")).thenReturn(gcpCarEntityFlux);

        // Run the test
        final Flux<Car> result = firestoreDbService.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectError(Exception.class)
                .verify();
    }

    @Test
    public void testGetCarsByBrand_FirestoreDbRepositoryReturnsNoItem() {
        // Setup
        when(firestoreDbRepository.findByBrand("brand")).thenReturn(Flux.empty());

        // Run the test
        final Flux<Car> result = firestoreDbService.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }
}
