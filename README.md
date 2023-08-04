# Spring Boot Reactive Application for Azure/GCP Cloud Services

This is a sample Spring Boot reactive application that demonstrates how to interact with the cloud services of Azure/GCP to push data into a Pub-Sub system and fetch data from the cloud-managed reactive MongoDB repositories. This application is built using Spring WebFlux, Spring Data Reactive MongoDB, and the respective cloud SDKs for Azure/GCP.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Application Structure](#application-structure)
- [Configuration](#configuration)
- [REST Endpoints](#rest-endpoints)
- [Dependencies](#dependencies)

## Prerequisites

Before running the application, make sure you have the following installed:

- Java Development Kit (JDK) 11 or higher
- Maven (for building and managing dependencies)
- Azure/GCP Cloud account with appropriate credentials and access to Pub-Sub and Reactive MongoDB services.

## Getting Started

1. Clone the repository:

```bash
https://github.com/knoldus/java-competency-demo
```
2. Open the project in your favorite IDE or code editor.

3. Configure the Azure/GCP credentials and other necessary configurations in the `application.yml`, `application-cosmos.yml`, `application-firestore.yml` file.

4. Build the application using Maven:
```bash
mvn clean package
```
5. Run the application 
```bash
mvn spring-boot:run -Dspring.profile.active=cosmos
```

## Application Structure

### High-level diagram
![HLD.png](..%2F..%2FPictures%2FHLD.png)

### Sequence diagram
![sequencediagram.png](..%2F..%2FPictures%2Fsequencediagram.png)

### Class diagram
![classDiagram.png](..%2F..%2FPictures%2FclassDiagram.png)

## Configuration

### Azure config
- cosmosdb.key = <COSMOS_KEY>
- cosmosdb.uri = <COSMOS_URI>
- kafka.bootstrap-servers= ${KAFKA_SERVER}
- kafka.properties.security.protocol = <KAFKA_SECURITY_PROTOCOL>
- kafka.properties.sasl.mechanism = <SASL_MECHANISM>
- kafka.properties.sasl.jaas.config = <JASS_CONFIG>
- kafka.producer.key-serializer = <KEY_SERIALIZER>
- kafka.producer.value-serializer = <VALUE_SERIALIZER>

### GCP config
- google.application.credentials  = <GOOGLE_APPLICATION_CREDENTIALS}>
- google.application.pubSub.topic = <TOPIC-NAME>
- spring.cloud.gcp:.project-id = <PROJECT-ID>

## REST endpoints

- Push data to cloud Pub-sub
  - Endpoint: `/v1/data`
  - HTTP method: POST
- Get brands data from reactive mogo repository
    - Endpoint: `/v1/data/brands`
    - HTTP method: GET
- Get data from reactive mogo repository
  - Endpoint: `/v1/data/cars/{brand}`
  - HTTP method: GET

## Dependencies
The main dependencies used in this application are:

- Spring Boot Starter WebFlux: Provides the necessary components for building reactive web applications.

- Spring Boot Starter Data MongoDB Reactive: Allows the application to interact with the reactive MongoDB repositories.

- Azure SDK or GCP SDK: The SDK for the respective cloud provider is used to interact with the Pub-Sub service.

For a complete list of dependencies, please refer to the pom.xml file in the project.
