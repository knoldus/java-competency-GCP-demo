package com.nashtech.config.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PubSubConsumer extends Consumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${google.pubSub.subscriptionId}")
    private String subscription;

    @Autowired
    private PubSubTemplate pubSubTemplate;

    private final PubSubMessageProcessor pubSubMessageProcessor;

    public PubSubConsumer(PubSubMessageProcessor pubSubMessageProcessor) {
        this.pubSubMessageProcessor = pubSubMessageProcessor;
    }

    @Override
    public String subscription() {
        return this.subscription;
    }

    @Override
    protected void consume(BasicAcknowledgeablePubsubMessage basicAcknowledgeablePubsubMessage) {
        PubsubMessage message = basicAcknowledgeablePubsubMessage.getPubsubMessage();
        log.info("Message received from {}", basicAcknowledgeablePubsubMessage.getProjectSubscriptionName());
        try {
            log.info("Message: {}", message.getData().toStringUtf8());
            pubSubMessageProcessor.processMessage(basicAcknowledgeablePubsubMessage);
        } catch (Exception ex) {
            log.error("Error Occurred while receiving pubsub message:::::", ex);
        }
        basicAcknowledgeablePubsubMessage.ack();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void subscribe() {
        log.info("Subscribing {} to {} ", this.getClass().getSimpleName(), this.subscription());
        pubSubTemplate.subscribe(this.subscription(), this.messageConsumer());
    }
}