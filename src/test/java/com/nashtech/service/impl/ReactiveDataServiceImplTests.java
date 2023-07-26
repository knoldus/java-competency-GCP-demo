package com.nashtech.service.impl;

import com.nashtech.model.Car;
import com.nashtech.service.CloudDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReactiveDataServiceImplTests {


    @Mock
    private WebClient webClient;

    @Mock
    private CloudDataService cloudDataService; // Make sure it's properly mocked

    @InjectMocks
    private ReactiveDataServiceImpl reactiveDataService; // This should be the actual class under test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = Mockito.mock(WebClient.RequestHeadersUriSpec.class);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);

        WebClient.RequestHeadersSpec requestHeadersSpec = Mockito.mock(WebClient.RequestHeadersSpec.class);
        when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);


    }

    @Test
    void testFetchAndSendData_Success() throws Exception {
        WebClient.ResponseSpec responseSpec = Mockito.mock(WebClient.ResponseSpec.class);
        // Create a sample test data
        Car testData = new Car(0, "brand", "model", 2020L, "color", 0.0, 0.0);

        // Mock the WebClient response with the sample data
        when(webClient.get().uri(anyString()).retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(Car.class)).thenReturn(Flux.just(testData));

        // Call the method under test
        reactiveDataService.fetchAndSendData();

        // Verify that cloudDataService.sendData() was called exactly once with the testData as an argument
        verify(cloudDataService, times(1)).pushData(testData);
    }

}
