package com.nashtech.service.impl;

import com.nashtech.model.ReactiveDataCar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloudDataServiceImplTests {

    @Mock
    private KafkaTemplate<String, ReactiveDataCar> kafkaTemplate;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendData() {
        // Create a sample test data
        ReactiveDataCar testData = new ReactiveDataCar(0, "brand", "model", 2020L, "color", 0.0, 0.0);

        // Create the service with the mocked KafkaTemplate
        CloudDataServiceImpl cloudDataService = new CloudDataServiceImpl(kafkaTemplate);

        // Call the method under test
        cloudDataService.sendData(testData);

        // Verify that kafkaTemplate.send() was called exactly once with the correct arguments
        ArgumentCaptor<Message<ReactiveDataCar>> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate, times(1)).send(messageCaptor.capture());

        Message<ReactiveDataCar> capturedMessage = messageCaptor.getValue();
        assertEquals(testData, capturedMessage.getPayload());
        assertEquals("myeventhub", capturedMessage.getHeaders().get(KafkaHeaders.TOPIC));
    }
}
