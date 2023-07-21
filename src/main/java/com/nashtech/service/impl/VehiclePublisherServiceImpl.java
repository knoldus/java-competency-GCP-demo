package com.nashtech.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import com.nashtech.model.Vehicle;
import com.nashtech.service.VehiclePublisherService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Service implementation class for publishing vehicle data to Google Cloud Pub/Sub.
 */
@Slf4j
@Service
public class VehiclePublisherServiceImpl implements VehiclePublisherService {
    @Value("${spring.cloud.gcp.project-id}")
    private String projectId;
    @Value("${google.pubSub.topic}")
    private String topicId;

    /**
     * Publishes vehicle data to Google Cloud Pub/Sub.
     *
     * @param vehicles The Flux of Vehicle objects representing the vehicle data to be published.
     * @return A Mono<Void> that represents the completion of the publishing process.
     * @throws IOException If an error occurs while converting Vehicle objects to JSON format.
     */
    public Mono<Void> publishVehicleData(
            Flux<Vehicle> vehicles) throws IOException {
        TopicName topicName = TopicName.of(projectId, topicId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Publisher publisher = Publisher.newBuilder(topicName).build();

        return vehicles.flatMap(vehicle -> {
                    try {
                        String vehicleJson = objectMapper.
                                writeValueAsString(vehicle);
                        ByteString data = ByteString.copyFromUtf8(vehicleJson);
                        PubsubMessage pubsubMessage = PubsubMessage.
                                newBuilder().setData(data).build();

                        return Mono.create(monoSink -> {
                            ApiFutures.addCallback(
                                    publisher.publish(pubsubMessage),
                                    new ApiFutureCallback<String>() {
                                        @Override
                                        public void onFailure(Throwable t) {
                                            monoSink.error(t);
                                        }
                                        @Override
                                        public void onSuccess(String messageId) {
                                            monoSink.success();
                                        }
                                    },
                                    MoreExecutors.directExecutor()
                            );
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        return Mono.error(e);
                    }
                })
                .doOnNext(result -> log.info("Message published successfully."))
                .then()
                .doFinally(signalType -> {
                    publisher.shutdown();
                    try {
                        publisher.awaitTermination(1, TimeUnit.MINUTES);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }
}
