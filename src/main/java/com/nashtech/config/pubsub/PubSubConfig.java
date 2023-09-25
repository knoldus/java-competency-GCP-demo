package com.nashtech.config.pubsub;

import com.google.api.gax.core.CredentialsProvider;
import com.google.auth.oauth2.ServiceAccountCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.core.DefaultGcpProjectIdProvider;
import org.springframework.cloud.gcp.core.GcpProjectIdProvider;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.DefaultPublisherFactory;
import org.springframework.cloud.gcp.pubsub.support.DefaultSubscriberFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class PubSubConfig {

    @Value("${google.pubSub.projectId}")
    private String projectId;

    @Value("${google.pubSub.credentials}")
    private String credentialsPath;

    @Value("${google.pubSub.topic}")
    private String topicName;

    @Bean
    public PubSubTemplate pubSubTemplate() {
        CredentialsProvider credentialsProvider = () -> ServiceAccountCredentials.fromStream(
                new ClassPathResource(credentialsPath).getInputStream());

        GcpProjectIdProvider gcpProjectIdProvider = new DefaultGcpProjectIdProvider() {
            @Override
            public String getProjectId() {
                return projectId;
            }
        };

        DefaultSubscriberFactory subscriberFactory =
                new DefaultSubscriberFactory(gcpProjectIdProvider);
        subscriberFactory.setCredentialsProvider(credentialsProvider);

        DefaultPublisherFactory publisherFactory =
                new DefaultPublisherFactory(gcpProjectIdProvider);
        publisherFactory.setCredentialsProvider(credentialsProvider);

        return new PubSubTemplate(publisherFactory, subscriberFactory);
    }

}