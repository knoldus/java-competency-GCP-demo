package com.nashtech.service;

import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.CosmosDbRepository;
import com.nashtech.service.impl.CosmosDbService;
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
public class CosmosDbServiceTest {

    @Mock
    private CosmosDbRepository cosmosDbRepository;

    @InjectMocks
    private CosmosDbService cosmosDbService;

    @Test
    void testGetCarsByBrand() {
        final Flux<Car> carFlux = Flux.just(
                new Car(0L, "brand", "model", 2020, "color", 0.0, 0.0));

        Mockito.when(cosmosDbRepository.getAllCarsByBrand(ArgumentMatchers.anyString())).thenReturn(carFlux);
        // Run the test
        final Flux<Car> result = cosmosDbService.getCarsByBrand("brand");
        StepVerifier.create(result)
                .expectNextMatches(car -> car.getBrand().equals("brand")) 
                .verifyComplete();
        Mockito.verify(cosmosDbRepository,Mockito.times(1)).getAllCarsByBrand("brand");
    }

    @Test
    void testGetCarsByBrand_ReactiveDataRepositoryReturnsError() {
        // Setup
        Mockito.when(cosmosDbRepository.getAllCarsByBrand("brand")).thenReturn(Flux.error(new DataNotFoundException()));

        // Run the test
        final Flux<Car> result = cosmosDbService.getCarsByBrand("brand");

        // Assert the error
        StepVerifier.create(result)
                .expectError(DataNotFoundException.class)
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
                .expectError(DataNotFoundException.class)
                .verify();
    }


    @Test
    void testGetAllBrands() {
        // Setup
        final List<CarBrand> expectedBrands = Arrays.asList(new CarBrand("brand1"), new CarBrand("brand2"));
        final Flux<CarBrand> BrandsFlux = Flux.fromIterable(expectedBrands);
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(BrandsFlux);

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectNextSequence(expectedBrands) 
                .verifyComplete();
    }

    @Test
    void testGetAllBrands_ReactiveDataRepositoryReturnsError() {
        // Setup
        final Flux<CarBrand> BrandsFlux = Flux.error(new DataNotFoundException());
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(BrandsFlux);

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectError(DataNotFoundException.class) 
                .verify();
    }

    @Test
    void testGetAllBrands_ReactiveDataRepositoryReturnsNoItem() {
        // Setup
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(Flux.empty());

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectError(DataNotFoundException.class)
                .verify();
    }

}

