package com.nashtech.controller;

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
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.error(new Exception("message")));

        // Run the test
        final Flux<CarBrand> result = reactiveDataController.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("message"))
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
        when(reactiveDataService.getCarsByBrand("brand")).thenReturn(Flux.error(new Exception("message")));

        // Run the test
        final Flux<Car> result = reactiveDataController.getCarsByBrand("brand");

        // Verify the results
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("message"))
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
        String brand = "Toyota";
        when(reactiveDataService.getCarsByBrand(brand)).thenReturn(Flux.just(new Car(), new Car()));

        Flux<Car> carsFlux = reactiveDataController.getCarsByBrand(brand);

        StepVerifier.create(carsFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void testGetAllBrands() {
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.just(new CarBrand("Toyota"), new CarBrand("BMW")));

        Flux<CarBrand> brandsFlux = reactiveDataController.getAllBrands();

        StepVerifier.create(brandsFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}
