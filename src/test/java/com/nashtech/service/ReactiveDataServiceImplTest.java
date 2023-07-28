package com.nashtech.service;

import com.nashtech.model.Car;
import com.nashtech.service.impl.ReactiveDataServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.net.URI;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReactiveDataServiceImplTest {


    @Mock
    private WebClient webClient;

    @Mock
    private CloudDataService cloudDataService;

    @InjectMocks
    private ReactiveDataServiceImpl reactiveDataService;

    @Test
    void testFetchAndSendData() throws Exception {
        // Setup
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri((URI) isNull());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Flux.empty()).when(responseSpec).bodyToFlux(Car.class);

        reactiveDataService.fetchAndSendData();


        verify(cloudDataService).pushData(Car.builder().build());
        verifyNoMoreInteractions(cloudDataService);
    }

    @Test
    void testFetchAndSendData_CloudDataServiceThrowsException() throws Exception {
        // Setup
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);
        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestHeadersSpec).retrieve();
        doReturn(Flux.empty()).when(responseSpec).bodyToFlux(Car.class);
        when(cloudDataService.pushData(Car.builder().build())).thenThrow(Exception.class);

        // Run the test
        reactiveDataService.fetchAndSendData();

        // Verify the results
        verify(cloudDataService).pushData(Car.builder().build());
        verifyNoMoreInteractions(cloudDataService);
    }

}