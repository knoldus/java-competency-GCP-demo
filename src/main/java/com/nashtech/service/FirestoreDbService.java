package com.nashtech.service;

import com.google.cloud.spring.data.firestore.FirestoreDataException;
import com.nashtech.model.Car;
import com.nashtech.model.CarBrand;
import com.nashtech.repository.FirestoreDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * FirestoreDbService is an implementation
 * of the CloudDataService interface that interacts
 * with Firestore database to retrieve Car and CarBrand data.
 */
@Slf4j
@Service
public class FirestoreDbService implements CloudDataService {

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
                .filter(vehicle -> vehicle.getBrand() != null)
                .map(vehicle -> new CarBrand(vehicle.getBrand()))
                .distinct()
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
                .filter(vehicle -> vehicle != null)
                .map(vehicle -> new Car(
                        vehicle.getCarId(),
                        vehicle.getCarModel(),
                        vehicle.getBrand(),
                        vehicle.getYear(),
                        vehicle.getColor(),
                        vehicle.getMileage(),
                        vehicle.getPrice()))
                .distinct()
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
