package com.nashtech;

import com.nashtech.mockaroodata.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavaCompetencyDemoApplication {
/**
 * This method is the entry point for the JavaCompetencyDemoApplication.
 * It calls the springApplication.run method to start the application.
 *
 * @param args an array of command-line arguments passed to
 *                the application
 */
public static void main(final String[] args) {
Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
SpringApplication.run(JavaCompetencyDemoApplication.class, args);
}

}
