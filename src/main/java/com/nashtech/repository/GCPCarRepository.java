package com.nashtech.repository;

import com.nashtech.entity.GCPElasticCarEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GCPCarRepository extends ElasticsearchRepository<GCPElasticCarEntity, String> {
    Optional<GCPElasticCarEntity> findByCarId(String carId);
}
