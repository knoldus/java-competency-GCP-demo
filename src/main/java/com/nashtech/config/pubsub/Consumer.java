package com.nashtech.config.pubsub;

import com.google.cloud.pubsub.v1.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;

public abstract class Consumer<B> {

    @Autowired
    private PubSubTemplate pubSubTemplate;

    public abstract String subscription();

    protected abstract void consume(BasicAcknowledgeablePubsubMessage message);

    public java.util.function.Consumer<BasicAcknowledgeablePubsubMessage> messageConsumer() {
        return this::consume;
    }

    public Subscriber consumeMessage() {
        return this.pubSubTemplate.subscribe(this.subscription(), this.messageConsumer());
    }
}