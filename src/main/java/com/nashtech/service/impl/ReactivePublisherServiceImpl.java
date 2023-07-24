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
import com.nashtech.model.ReactiveDataCars;
import com.nashtech.service.ReactivePublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

/**
 * Service implementation class for publishing vehicle data to Google Cloud Pub/Sub.
 */
@Slf4j
@Service
public class ReactivePublisherServiceImpl implements ReactivePublisherService {
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
     * The static Publisher instance
     * for publishing vehicle data to the Pub/Sub topic.
     */
    public static Publisher publisher;


    /**
     * Publishes vehicle data to Google Cloud Pub/Sub.
     *
     * @param cars The Flux of Vehicle objects
     * representing the vehicle data to be published.
     * @return A Mono<Void>
     * representing the completion of the publishing process.
     * @throws IOException If an error occurs
     * while converting Vehicle objects to JSON format.
     */
    public Mono<Void> publishCarData(
            Flux<ReactiveDataCars> cars) throws IOException {
        TopicName topicName = TopicName.of(projectId, topicId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        publisher = Publisher.newBuilder(topicName).build();
        return cars.flatMap(vehicle -> {
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
                                        public void onFailure(Throwable error) {
                                            monoSink.error(error);
                                        }
                                        @Override
                                        public void onSuccess(String messageId) {
                                            monoSink.success();
                                        }
                                    },
                                    MoreExecutors.directExecutor()
                            );
                        });
                    } catch (Exception exception) {
                        return Mono.error(exception);
                    }
                })
                .doOnNext(result -> log.info("Message published successfully."))
                .then()
                .doFinally(signalType -> {
                    publisher.shutdown();
                    try {
                        publisher.awaitTermination(1,TimeUnit.MINUTES);
                    } catch (InterruptedException interruptedException) {
                        throw new RuntimeException(interruptedException);
                    }
                });
    }
}
