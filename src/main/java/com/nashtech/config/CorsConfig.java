package com.nashtech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * Configuration class for CORS (Cross-Origin Resource Sharing)
 * settings in a Spring WebFlux application.
 */
@Configuration
public class CorsConfig implements WebFluxConfigurer {

    /**
     * static Max Age for Cors.
     */
    private static final Integer MAX_AGE = 3628800;

    /**
     * Configure CORS settings for the application.
     *
     * @param registry The CorsRegistry to configure.
     */
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("origin",
                        "x-requested-with", "accept")
                .allowedMethods("GET", "POST",
                        "PUT", "DELETE")
                .maxAge(MAX_AGE);
    }
}
