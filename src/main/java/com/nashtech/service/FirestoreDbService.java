package com.nashtech.service;

import com.google.cloud.spring.data.firestore.FirestoreDataException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.FirestoreDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * FirestoreDbService is an implementation
 * of the CloudDataService interface that interacts
 * with Firestore database to retrieve Car and CarBrand data.
 */
@Slf4j
@Service
public class FirestoreDbService implements CloudDataService {

    /**
     * The duration of the interval,
     * in milliseconds, for retrieving brand and car details.
     */
    private static final Integer DELAY_TIME = 100;
    /**
     * The VehicleRepository instance used to retrieve car information.
     */
    @Autowired
    private FirestoreDbRepository firestoreDbRepository;

    /**
     * Retrieves all CarBrands from Firestore database.
     * @return A Flux of CarBrand objects.
     *
     */
    @Override
    public Flux<CarBrand> getAllBrands() {
        return firestoreDbRepository.findAll()
                .filter(carEntity -> carEntity.getBrand() != null)
                .map(carEntity -> new CarBrand(carEntity.getBrand()))
                .distinct()
                .delayElements(Duration.ofMillis(DELAY_TIME))
                .doOnNext(carBrand -> log.info("Brand: " + carBrand))
                .doOnComplete(() -> log.info("Data retrieved successfully"))
                .switchIfEmpty(Flux.defer(() -> {
                    log.info("No data found");
                    return Flux.empty();
                }))
                .onErrorResume(throwable -> {
                        log.error("Error occurred during data retrieval: "
                                + throwable.getMessage());
                        return Flux.error(
                                new FirestoreDataException(
                                        "Failed to retrieve car brands.",
                                         throwable));
                });
    }

    /**
     * Retrieves all Car objects by a given brand from Firestore database.
     *
     * @param brand The brand of the Car to filter by.
     * @return A Flux of Car objects matching the given brand.
     *
     */
    @Override
    public Flux<Car> getCarsByBrand(final String brand) {
        return firestoreDbRepository.findByBrand(brand)
                .filter(carEntity -> carEntity != null)
                .map(carEntity -> new Car()
                                .setCarId(carEntity.getCarId())
                                .setCarModel(carEntity.getCarModel())
                                .setBrand(carEntity.getBrand())
                                .setYear(carEntity.getYear())
                                .setColor(carEntity.getColor())
                                .setMileage(carEntity.getMileage())
                                .setPrice(carEntity.getPrice())
                                .build())
                .distinct()
                .delayElements(Duration.ofMillis(DELAY_TIME))
                .switchIfEmpty(Flux.defer(() -> {
                    log.info("No data found");
                    return Flux.empty();
                }))
                .doOnComplete(() ->
                        log.info("Received Car Details successfully"))
                .onErrorResume(throwable -> {
                        log.error("Error occurred during data retrieval: "
                                + throwable.getMessage());
                        return Flux.error(
                                new FirestoreDataException(
                                        "Failed to retrieve car Information.",
                                        throwable));
                });

    }
}
