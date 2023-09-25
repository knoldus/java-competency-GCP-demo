package com.nashtech.controller;

import com.nashtech.entity.GCPElasticCarEntity;
import com.nashtech.repository.GCPCarRepositoryImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class GCPElasticSearchController {

    private final GCPCarRepositoryImpl gcpCarRepositoryImpl;

    public GCPElasticSearchController(GCPCarRepositoryImpl gcpCarRepositoryImpl) {
        this.gcpCarRepositoryImpl = gcpCarRepositoryImpl;
    }

    @PostMapping("/publishMessageToPubSub")
    public ResponseEntity<Object> publishMessageToPubSub(@RequestBody List<GCPElasticCarEntity> gcpElasticCarEntityList) throws IOException, InterruptedException, ExecutionException {
        gcpCarRepositoryImpl.publishCarDetails(gcpElasticCarEntityList);
        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    @GetMapping("/getCarDetailsById/{carId}")
    public ResponseEntity<Object> getCarDetailsById(@PathVariable String carId) throws IOException {
        GCPElasticCarEntity gcpCarEntity = gcpCarRepositoryImpl.getCarDetailsById(carId);
        if (gcpCarEntity != null) {
            return new ResponseEntity<>(gcpCarEntity, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Car details with id : " + carId + " does not exist.");
    }

    @GetMapping("/getAllCarDetails")
    public ResponseEntity<Object> getAllCarDetails() throws IOException {
        List<GCPElasticCarEntity> gcpCarEntityList = gcpCarRepositoryImpl.getAllCarDetails();
        return new ResponseEntity<>(gcpCarEntityList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCarDetailsById/{carId}")
    public ResponseEntity<Object> deleteCarDetailsById(@PathVariable String carId) throws IOException {
        String response = gcpCarRepositoryImpl.deleteCarDetailsById(carId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
