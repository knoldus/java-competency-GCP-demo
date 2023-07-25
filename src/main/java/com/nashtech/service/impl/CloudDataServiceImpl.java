package com.nashtech.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.nashtech.model.ReactiveDataCars;
import com.nashtech.service.CloudDataService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

/**
 * Service implementation class for
 * publishing vehicle data to Google Cloud Pub/Sub.
 */
@Slf4j
@Service
public class CloudDataServiceImpl implements CloudDataService {

    /**
     * The Google Cloud Platform project ID
     * used for publishing vehicle data to Pub/Sub.
     */
    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;

    /**
     * The ID of the Pub/Sub topic to
     * which the vehicle data will be published.
     */
    @Value("${google.pubSub.topic}")
    private String topicId;

    /**
     * Static Publisher instance for asynchronous vehicle
     * data publishing to the Google Cloud Pub/Sub topic.
     */
    private static Publisher publisher;

    /**
     * Initializes the static Publisher instance for
     * vehicle data publishing to the Google Cloud Pub/Sub topic.
     *
     * This method is annotated with @PostConstruct
     * and is automatically called after the bean is constructed,
     * ensuring that the Publisher is ready for use.
     *
     * @throws IOException If an error occurs during the
     * initialization of the Publisher.
     */
    @PostConstruct
    public void init() throws IOException {
        TopicName topicName = TopicName.of(projectId, topicId);
        publisher = Publisher.newBuilder(topicName).build();
    }

    /**
     * Cleans up and shuts down the static Publisher instance
     * used for vehicle data publishing.
     * This method is annotated with @PreDestroy
     * and is automatically called before the bean is destroyed,
     * ensuring that the Publisher is properly shutdown and
     * resources are released.
     */
    @PreDestroy
    public void cleanup() {
        try {
            if (publisher != null) {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            }
        } catch (InterruptedException interruptedException) {
            log.error("Error while shutting down Publisher: {}",
                    interruptedException.getMessage());
        }
    }


    /**
     * Publishes vehicle data to the Google Cloud Pub/Sub topic.
     *
     * @param cars The ReactiveDataCars representing
     *             the vehicle data to be published.
     * @return A Mono<Void> representing the completion of the
     * publishing process.
     * @throws RuntimeException If an error occurs during the
     * publishing process.
     */
    public Mono<Void> publishCarData(final ReactiveDataCars cars) {
        TopicName topicName = TopicName.of(projectId, topicId);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            String vehicleJson = objectMapper.writeValueAsString(cars);
            ByteString data = ByteString.copyFromUtf8(vehicleJson);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().
                    setData(data).build();

            return Mono.just(publisher.publish(pubsubMessage))
                    .doOnError(error -> {
                        throw new RuntimeException(error.getMessage());
                    }).then();
        } catch (Exception exception) {
            return Mono.error(exception);
        }
    }
}
