package com.nashtech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class for creating a WebClient bean
 * used to interact with the Mockaroo API.
 */
@Configuration
public class WebClientConfig {

    /**
     * The base URL of the Mockaroo API,
     * injected from the application properties.
     */
    @Value("${mockaroo.api.url}")
    private String mockarooBaseUrl;

    /**
     * Creates and configures a WebClient bean to
     * interact with the Mockaroo API.
     * @return The configured WebClient bean.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.create(mockarooBaseUrl);
    }
}
