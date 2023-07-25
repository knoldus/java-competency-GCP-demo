package com.nashtech.service.impl;

import com.nashtech.exception.ResourceNotFoundException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.CosmosDbRepository;
import com.nashtech.service.CosmosDbService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CloudDataServiceTest {

    @Mock
    private CosmosDbRepository cosmosDbRepository;

    @InjectMocks
    private CosmosDbService cosmosDbService;

    @Test
    void testGetCarsByBrand() {
        final Flux<Car> reactiveCarDetailsDtoFlux = Flux.just(
                new Car(0, "brand", "model", 2020L, "color", 0.0, 0.0));

        Mockito.when(cosmosDbRepository.getAllCarsByBrand(ArgumentMatchers.anyString())).thenReturn(reactiveCarDetailsDtoFlux);
        // Run the test
        final Flux<Car> result = cosmosDbService.getCarsByBrand("brand");
        StepVerifier.create(result)
                .expectNextMatches(car -> car.getBrand().equals("brand")) // Add more checks here if needed
                .verifyComplete();
        Mockito.verify(cosmosDbRepository,Mockito.times(1)).getAllCarsByBrand("brand");
    }

    @Test
    void testGetCarsByBrand_ReactiveDataRepositoryReturnsError() {
        // Setup
        // Configure CosmosDbRepository.getAllCars(...).
        final Flux<Car> reactiveCarDetailsDtoFlux = Flux.error(new Exception("message"));
        Mockito.when(cosmosDbRepository.getAllCarsByBrand("brand")).thenReturn(Flux.error(new ResourceNotFoundException()));

        // Run the test
        final Flux<Car> result = cosmosDbService.getCarsByBrand("brand");

        // Assert the error
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void testGetCarsByBrand_ReactiveDataRepositoryReturnsNoItem() {
        // Setup
        Mockito.when(cosmosDbRepository.getAllCarsByBrand("brand")).thenReturn(Flux.empty());

        // Run the test
        final Flux<Car> result = cosmosDbService.getCarsByBrand("brand");

        // Assert the error
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }


    @Test
    void testGetAllBrand() {
        // Setup
        final List<CarBrand> expectedBrands = Arrays.asList(new CarBrand("brand1"), new CarBrand("brand2"));
        final Flux<CarBrand> reactiveDataBrandsFlux = Flux.fromIterable(expectedBrands);
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(reactiveDataBrandsFlux);

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrand();

        // Verify the results
        StepVerifier.create(result)
                .expectNextSequence(expectedBrands) // Verify if the elements match the expected list
                .verifyComplete();
    }

    @Test
    void testGetAllBrand_ReactiveDataRepositoryReturnsError() {
        // Setup
        // Configure CosmosDbRepository.findDistinctBrands(...).
        final Flux<CarBrand> reactiveDataBrandsFlux = Flux.error(new ResourceNotFoundException());
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(reactiveDataBrandsFlux);

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrand();

        // Verify the results
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class) // Verify if the expected error is thrown
                .verify();
    }

    @Test
    void testGetAllBrand_ReactiveDataRepositoryReturnsNoItem() {
        // Setup
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(Flux.empty());

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrand();

        // Verify the results
        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

}
