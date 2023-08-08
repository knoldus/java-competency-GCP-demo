package com.nashtech.service;

import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.nashtech.exception.DataNotFoundException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.CosmosDbRepository;
import com.nashtech.service.impl.CosmosDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CosmosDbServiceTest {


    @Mock
    private CosmosDbRepository cosmosDbRepository;

    @Mock
    private KafkaTemplate<String, Car> kafkaTemplate;

    @InjectMocks
    private CosmosDbService cosmosDbService;

    @BeforeEach
    void setUp() {
        // Initialize the mocks before each test method
        reset(kafkaTemplate);
    }

    @Test
    void testGetCarsByBrand() {
        final Flux<Car> carFlux = Flux.just(
                new Car(0, "brand", "model", 2020L, "color", 0.0, 0.0));

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
        Mockito.when(cosmosDbRepository.getAllCarsByBrand("brand")).thenReturn(Flux.error(
                new CosmosAccessException("Failed to retrieve Cars by Brand")));

        // Run the test
        final Flux<Car> result = cosmosDbService.getCarsByBrand("brand");

        // Assert the error
        StepVerifier.create(result)
                .expectError(CosmosAccessException.class)
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
        final Flux<CarBrand> BrandsFlux = Flux.error(new CosmosAccessException("Failed to retrieve All brands"));
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(BrandsFlux);

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrands();

        // Verify the results
        StepVerifier.create(result)
                .expectError(CosmosAccessException.class)
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

    @Test
    void testGetAllBrands_NoDuplicateBrandsReturned() {
        // Prepare mock data
        CarBrand brand1 = new CarBrand("BMW");
        CarBrand brand2 = new CarBrand("Toyota");
        CarBrand brand3 = new CarBrand("Mercedes");

        // Mock the reactiveDataService to return Flux with duplicate brands
        Mockito.when(cosmosDbRepository.findDistinctBrands()).thenReturn(Flux.just(brand1, brand2, brand1, brand3, brand2));

        // Run the test
        final Flux<CarBrand> result = cosmosDbService.getAllBrands();

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



    @Test
    void testPushData() {
        // Arrange
        Car car = new Car(0, "brand", "model", 2020L, "color", 0.0, 0.0);

        // Mock the behavior of kafkaTemplate.send() using doAnswer()
        doAnswer(invocation -> {
            Message<Car> message = invocation.getArgument(0);
            // Perform any additional verification/assertion on the message if needed
            return null; // Return null since the method is void
        }).when(kafkaTemplate).send(any(Message.class));

        // Act
        Mono<Void> result = cosmosDbService.pushData(car);

        // Assert
        // Verify that kafkaTemplate.send() was called with the correct message
        verify(kafkaTemplate, times(1)).send(any(Message.class));

        // Verify that the Mono returned by the pushData method is empty
        StepVerifier.create(result)
                .expectSubscription()
                .expectComplete()
                .verify();
    }
}

