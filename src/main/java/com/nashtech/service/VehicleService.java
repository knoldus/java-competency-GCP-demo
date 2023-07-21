package com.nashtech.service;

import com.nashtech.model.VehicleDTO;
import reactor.core.publisher.Flux;

public interface VehicleService {
    Flux<String> getBrands();
    Flux<VehicleDTO> findCarInformation(final String brand);
}
