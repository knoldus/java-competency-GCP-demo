package com.nashtech.service;

import static org.mockito.BDDMockito.*;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.impl.ReactiveDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(MockitoExtension.class)
public class ReactiveDataServiceImplTest {

    @Mock
    private CloudDataService cloudDataService;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private ReactiveDataServiceImpl reactiveDataService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testGetCarsByBrand() {
        // Mock the CloudDataService behavior
        String brand = "Toyota";
        Car car1 = new Car(/* Car properties with brand Toyota */);
        Car car2 = new Car(/* Car properties with brand Toyota */);
        Flux<Car> carFlux = Flux.just(car1, car2);
        when(cloudDataService.getCarsByBrand(brand)).thenReturn(carFlux);

        // Invoke the method to test
        Flux<Car> resultFlux = reactiveDataService.getCarsByBrand(brand);

        // StepVerifier to verify the Flux subscription
        StepVerifier.create(resultFlux)
                .expectNext(car1)
                .expectNext(car2)
                .verifyComplete();
    }

    @Test
    void testGetAllBrands() {
        // Mock the CloudDataService behavior
        CarBrand brand1 = new CarBrand("Toyota");
        CarBrand brand2 = new CarBrand("Honda");
        Flux<CarBrand> carBrandFlux = Flux.just(brand1, brand2);
        when(cloudDataService.getAllBrands()).thenReturn(carBrandFlux);

        // Invoke the method to test
        Flux<CarBrand> resultFlux = reactiveDataService.getAllBrands();

        // StepVerifier to verify the Flux subscription
        StepVerifier.create(resultFlux)
                .expectNext(brand1)
                .expectNext(brand2)
                .verifyComplete();
    }









}


