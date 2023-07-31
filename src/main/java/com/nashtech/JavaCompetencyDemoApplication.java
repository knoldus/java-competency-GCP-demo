package com.nashtech;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@OpenAPIDefinition(info = @Info(title = "Reactive Cloud Application",
        version = "1.0", description = "Reactive Cloud Application uses"
        + " Azure and GCP Cloud platforms to consume, transform, store and "
        + "fetch data and then display to front end using Reactive."))
public class JavaCompetencyDemoApplication {
/**
 * This method is the entry point for the JavaCompetencyDemoApplication.
 * It calls the springApplication.run method to start the application.
 *
 * @param args an array of command-line arguments passed to
 *                the application
 */
public static void main(final String[] args) {
SpringApplication.run(JavaCompetencyDemoApplication.class, args);
}

}
