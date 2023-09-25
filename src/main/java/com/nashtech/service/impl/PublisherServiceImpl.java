package com.nashtech.service.impl;

import com.nashtech.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Slf4j
@Service
public class PublisherServiceImpl implements PublisherService {

    private final PubSubTemplate pubSubTemplate;

    @Value("${google.pubSub.topic}")
    public String topicName;

    public PublisherServiceImpl(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }

    @Override
    public void publish(String message) {
        try {
            ListenableFuture<String> listenableFuture = pubSubTemplate.publish(this.topicName, message);
            String messageId = listenableFuture.get();
            log.info("Successfully published message: {} Message Id:  {}", message, messageId);
        } catch (Exception e) {
            log.error("Publishing message to GCP Pub Sub failed. {}", e.getMessage());
        }
    }
}
