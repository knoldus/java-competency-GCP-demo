package com.nashtech.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.*;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.service.impl.ReactiveDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
        Car car1 = new Car(0, "Toyota", "model", 2020L, "color", 0.0, 0.0);
        Car car2 = new Car(1, "Toyota", "model", 2020L, "color", 0.0, 0.0);
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

    @Test
    void testGetAllBrands_NoDuplicateBrandsReturned() {
        // Prepare mock data
        CarBrand brand1 = new CarBrand("BMW");
        CarBrand brand2 = new CarBrand("Toyota");
        CarBrand brand3 = new CarBrand("Mercedes");

        // Mock the reactiveDataService to return Flux with duplicate brands
        when(reactiveDataService.getAllBrands()).thenReturn(Flux.just(brand1, brand2, brand1, brand3, brand2));

        // Run the test
        final Flux<CarBrand> result = reactiveDataService.getAllBrands();

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
