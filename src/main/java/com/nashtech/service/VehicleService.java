package com.nashtech.service;

import com.nashtech.model.VehicleDTO;
import reactor.core.publisher.Flux;

public interface VehicleService {
    Flux<String> getAllBrandNames();
    Flux<VehicleDTO> getDetailsByBrandName(final String brand);
}
