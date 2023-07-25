package com.nashtech.service.impl;

import com.nashtech.model.DataCar;
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
    private KafkaTemplate<String, DataCar> kafkaTemplate;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendData() {
        // Create a sample test data
        DataCar testData = new DataCar(0, "brand", "model", 2020L, "color", 0.0, 0.0);

        // Create the service with the mocked KafkaTemplate
        CosmosDbService cloudDataService = new CosmosDbService(kafkaTemplate);

        // Call the method under test
        cloudDataService.sendData(testData);

        // Verify that kafkaTemplate.send() was called exactly once with the correct arguments
        ArgumentCaptor<Message<DataCar>> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate, times(1)).send(messageCaptor.capture());

        Message<DataCar> capturedMessage = messageCaptor.getValue();
        assertEquals(testData, capturedMessage.getPayload());
        assertEquals("myeventhub", capturedMessage.getHeaders().get(KafkaHeaders.TOPIC));
    }
}
