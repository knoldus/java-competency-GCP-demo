package com.nashtech.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration class to provide a WebClient bean
 * for making HTTP requests to an external API.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates and configures a WebClient bean with the
     * base URL to interact with an external API.
     *
     * @return The configured WebClient bean.
     */
    @Bean
    public WebClient webClient() {
        return WebClient.create("https://my.api.mockaroo.com");
    }
}
