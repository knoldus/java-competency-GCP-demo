package com.nashtech.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.entity.GCPElasticCarEntity;
import com.nashtech.service.PublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class GCPCarRepositoryImpl {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Value("${google.elastic.indexName}")
    private String indexName;

    @Value("${google.pubSub.topic}")
    private String topicId;

    @Value("${google.pubSub.projectId}")
    private String projectId;

    private final PublisherService publisherService;

    public GCPCarRepositoryImpl(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    public void publishCarDetails(List<GCPElasticCarEntity> carEntities) {
        String message = convertCarEntityToString(carEntities);
        log.info("Publishing message : {}", message);
        publisherService.publish(message);
    }

    public GCPElasticCarEntity getCarDetailsById(String carId) throws IOException {
        GCPElasticCarEntity gcpCarEntity = null;
        GetResponse<GCPElasticCarEntity> response = elasticsearchClient.get(
                g -> g.index(indexName)
                        .id(String.valueOf(carId)),
                GCPElasticCarEntity.class
        );

        if (response.found()) {
            gcpCarEntity = response.source();
            if (gcpCarEntity != null && gcpCarEntity.getModel() != null) {
                log.info("Car model name is: " + gcpCarEntity.getModel());
            }
        } else {
            log.info("Car details not found");
        }
        return gcpCarEntity;
    }

    public List<GCPElasticCarEntity> getAllCarDetails() throws IOException {
        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse<GCPElasticCarEntity> searchResponse = elasticsearchClient.search(searchRequest, GCPElasticCarEntity.class);
        List<Hit<GCPElasticCarEntity>> hits = searchResponse.hits().hits();
        List<GCPElasticCarEntity> gcpElasticCarEntityList = new ArrayList<>();
        for (Hit<GCPElasticCarEntity> object : hits) {
            gcpElasticCarEntityList.add(object.source());
        }
        return gcpElasticCarEntityList;
    }

    public String deleteCarDetailsById(String carId) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(carId));
        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Car details with id : " + deleteResponse.id() + " has been deleted successfully !.").toString();
        }
        log.info("Car details not found");
        return new StringBuilder("Car details with id : " + deleteResponse.id() + " does not exist.").toString();
    }

    private String convertCarEntityToString(List<GCPElasticCarEntity> carEntityList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(carEntityList);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
