package com.nashtech.controller;

import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.ReactiveDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReactiveDataControllerTest {

    @Mock
    private ReactiveDataService reactiveDataService;

    @InjectMocks
    private ReactiveDataController reactiveDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBrands_ReactiveDataServiceReturnsError() {
        // Setup
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.error(new DataNotFoundException()));

        // Run the test
        final Flux<CarBrand> result = reactiveDataController.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Record not found"))
                .verify();
    }

    @Test
    void testGetAllBrands_ReactiveDataServiceReturnsNoItem() {
        // Setup
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.empty());

        // Run the test
        final Flux<CarBrand> result = reactiveDataController.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testGetCarsByBrand_ReactiveDataServiceReturnsError() {
        // Setup
        when(reactiveDataService.getCarsByBrand("brand")).thenReturn(Flux.error(new DataNotFoundException()));

        // Run the test
        final Flux<Car> result = reactiveDataController.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable.getMessage().equals("Record not found"))
                .verify();
    }

    @Test
    void testGetCarsByBrand_ReactiveDataServiceReturnsNoItem() {
        // Setup
        when(reactiveDataService.getCarsByBrand("brand")).thenReturn(Flux.empty());

        // Run the test
        final Flux<Car> result = reactiveDataController.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }


    @Test
    void testPushDataToCloud() {
        ResponseEntity<Object> response = reactiveDataController.pushDataToCloud();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(reactiveDataService).fetchAndSendData();
    }

    @Test
    void testGetCarsByBrand() {
        final Flux<Car> carFlux = Flux.just(
                new Car(0, "Toyota", "model", 2020L, "color", 0.0, 0.0, 0,0.0));
        when(reactiveDataService.getCarsByBrand("Toyota")).thenReturn(carFlux);

        Flux<Car> carsFlux = reactiveDataController.getCarsByBrand("Toyota");

        StepVerifier.create(carsFlux)
                .expectNextMatches(car -> car.getBrand().equals("Toyota"))
                .verifyComplete();
    }

    @Test
    void testGetAllBrands() {
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.just(new CarBrand("BMW")));

        Flux<CarBrand> brandsFlux = reactiveDataController.getAllBrands();

        StepVerifier.create(brandsFlux)
                .expectNextMatches(carBrand -> carBrand.getBrand().equals("BMW"))
                .verifyComplete();
    }

    @Test
    void testGetAllBrands_NoDuplicateBrandsReturned() {
        // Prepare mock data
        CarBrand brand1 = new CarBrand("BMW");
        CarBrand brand2 = new CarBrand("Toyota");
        CarBrand brand3 = new CarBrand("Mercedes");

        // Mock the reactiveDataService to return Flux with duplicate brands
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.just(brand1, brand2, brand1, brand3, brand2));

        // Run the test
        final Flux<CarBrand> result = reactiveDataController.getAllBrands();

        // Convert Flux to List
        List<CarBrand> brandList = result.collectList().block();

        // Verify the results
        StepVerifier.create(result)
                .recordWith(ArrayList::new) // Record all elements in an ArrayList
                .expectNextCount(5) // Expecting 5 items in the Flux
                .consumeRecordedWith(brands -> {
                    // Convert the list to a Set to check for duplicates
                    Set<CarBrand> uniqueBrands = new HashSet<>(brands);
                    assertThat(uniqueBrands.size()).isEqualTo(3).isSameAs(brandList);
                });

    }

}