package com.nashtech.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.spring.data.firestore.FirestoreDataException;
import com.nashtech.entity.GCPCarEntity;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.FirestoreDbRepository;
import com.nashtech.service.impl.FirestoreDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestoreDbServiceTest {

    @Mock
    private Publisher publisher;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private FirestoreDbRepository firestoreDbRepository;

    @InjectMocks
    private FirestoreDbService firestoreDbService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPushData() throws Exception {
        Car testCar = new Car(22, "Toyota", "Camry", 2023L, "red", 2223.0, 22253.12, 1, 1.2);

        Mono<Void> result = firestoreDbService.pushData(testCar);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }



    @Test
    public void testGetAllBrands() {
        // Setup
        // Configure FirestoreDbRepository.findAll(...).
        final GCPCarEntity gcpCarEntity = new GCPCarEntity();
        gcpCarEntity.setCarId(0);
        gcpCarEntity.setModel("carModel");
        gcpCarEntity.setBrand("brand");
        gcpCarEntity.setYear(2020L);
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
                .expectError(FirestoreDataException.class)
                .verify();
    }

    @Test
    public void testGetCarsByBrand() {
        // Setup
        // Configure FirestoreDbRepository.findByBrand(...).
        final GCPCarEntity gcpCarEntity = new GCPCarEntity();
        gcpCarEntity.setCarId(0);
        gcpCarEntity.setModel("carModel");
        gcpCarEntity.setBrand("brand");
        gcpCarEntity.setYear(2020L);
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
        Mockito.when(firestoreDbRepository.findByBrand("brand")).thenReturn(Flux.error(new FirestoreDataException("Something Went Wrong !!, Data not found")));

        // Run the test
        final Flux<Car> result = firestoreDbService.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectError(FirestoreDataException.class)
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
                .expectError(FirestoreDataException.class)
                .verify();
    }

}
