package com.nashtech.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.nashtech.entity.GCPElasticCarEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@Slf4j
public class GCPCarRepository {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Value("${google.elastic.indexName}")
    private String indexName;

    public List<String> saveOrUpdateCarDetails(List<GCPElasticCarEntity> carEntities) throws IOException {
        List<String> responseMessages = new ArrayList<>();
        for (GCPElasticCarEntity carEntity : carEntities) {
            carEntity.setId(UUID.randomUUID().toString());
            IndexResponse response = elasticsearchClient.index(
                    i -> i.index(indexName)
                            .id(carEntity.getId())
                            .document(carEntity)
            );
            if (response.result().name().equals("Created")) {
                responseMessages.add("Car details saved successfully with id: " + response.id());
            } else if (response.result().name().equals("Updated")) {
                responseMessages.add("Car details have been updated successfully with id: " + response.id());
            } else {
                responseMessages.add("Error while performing the operation.");
            }
        }
        return responseMessages;
    }

    public GCPElasticCarEntity getCarDetailsById(String id) throws IOException {
        GCPElasticCarEntity gcpCarEntity = null;
        GetResponse<GCPElasticCarEntity> response = elasticsearchClient.get(
                g -> g.index(indexName)
                        .id(String.valueOf(id)),
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
        List<GCPElasticCarEntity> invoices = new ArrayList<>();
        for (Hit<GCPElasticCarEntity> object : hits) {
            invoices.add(object.source());
        }
        return invoices;
    }

    public String deleteCarDetailsById(String id) throws IOException {
        DeleteRequest request = DeleteRequest.of(d -> d.index(indexName).id(id));
        DeleteResponse deleteResponse = elasticsearchClient.delete(request);
        if (Objects.nonNull(deleteResponse.result()) && !deleteResponse.result().name().equals("NotFound")) {
            return new StringBuilder("Car details with id : " + deleteResponse.id() + " has been deleted successfully !.").toString();
        }
        log.info("Car details not found");
        return new StringBuilder("Car details with id : " + deleteResponse.id() + " does not exist.").toString();
    }
}
