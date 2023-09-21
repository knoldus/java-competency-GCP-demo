package com.nashtech.controller;

import com.nashtech.entity.GCPElasticCarEntity;
import com.nashtech.repository.GCPCarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class GCPElasticSearchController {

    @Autowired
    private GCPCarRepository gcpCarRepository;

    @PostMapping("/saveOrUpdateCarDetails")
    public ResponseEntity<Object> saveOrUpdateCarDetails(@RequestBody List<GCPElasticCarEntity> gcpElasticCarEntityList) throws IOException {
        List<String> response = gcpCarRepository.saveOrUpdateCarDetails(gcpElasticCarEntityList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getCarDetailsById/{id}")
    public ResponseEntity<Object> getCarDetailsById(@PathVariable String id) throws IOException {
        GCPElasticCarEntity gcpCarEntity = gcpCarRepository.getCarDetailsById(id);
        if (gcpCarEntity != null) {
            return new ResponseEntity<>(gcpCarEntity, HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Car details with id : " + id + " does not exist.");
    }

    @GetMapping("/getAllCarDetails")
    public ResponseEntity<Object> getAllCarDetails() throws IOException {
        List<GCPElasticCarEntity> gcpCarEntityList = gcpCarRepository.getAllCarDetails();
        return new ResponseEntity<>(gcpCarEntityList, HttpStatus.OK);
    }

    @DeleteMapping("/deleteCarDetailsById/{id}")
    public ResponseEntity<Object> deleteCarDetailsById(@PathVariable String id) throws IOException {
        String response = gcpCarRepository.deleteCarDetailsById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
