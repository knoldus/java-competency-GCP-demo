package com.nashtech.config.pubsub;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nashtech.entity.GCPElasticCarEntity;
import com.nashtech.repository.GCPCarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gcp.pubsub.support.BasicAcknowledgeablePubsubMessage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class PubSubMessageProcessor {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    @Autowired
    GCPCarRepository gcpCarRepository;

    @Value("${google.elastic.indexName}")
    private String indexName;

    private final ObjectMapper objectMapper;

    public PubSubMessageProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void processMessage(BasicAcknowledgeablePubsubMessage message) {
        GCPElasticCarEntity[] gcHubMessageArray;
        String eventMsgString = "";
        try {
            eventMsgString = message.getPubsubMessage().getData().toStringUtf8();
            gcHubMessageArray = objectMapper.readValue(eventMsgString, GCPElasticCarEntity[].class);
            List<GCPElasticCarEntity> gcpElasticCarEntityList = Arrays.asList(gcHubMessageArray);
            feedMessageToElasticSearch(gcpElasticCarEntityList);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            log.error("Json exception in parsing message from GC Hub: {}", eventMsgString, e);
            message.ack();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void feedMessageToElasticSearch(List<GCPElasticCarEntity> carEntities) throws IOException {
        List<String> responseMessages = new ArrayList<>();
        for (GCPElasticCarEntity carEntity : carEntities) {
            Optional<GCPElasticCarEntity> existingCarId = gcpCarRepository.findByCarId(String.valueOf(carEntity.getCarId()));
            if (existingCarId.isEmpty()) {
                carEntity.setId(UUID.randomUUID().toString());
                IndexResponse response = elasticsearchClient.index(
                        i -> i.index(indexName)
                                .id(String.valueOf(carEntity.getCarId()))
                                .document(carEntity)
                );
                if (response.result().name().equals("Created")) {
                    responseMessages.add("Car details saved successfully with id: " + response.id());
                    log.info(responseMessages.toString());
                } else if (response.result().name().equals("Updated")) {
                    responseMessages.add("Car details have been updated successfully with id: " + response.id());
                    log.info(responseMessages.toString());
                } else {
                    responseMessages.add("Error while performing the operation.");
                    log.error(responseMessages.toString());
                }
            } else {
                log.error("Car Id already exists, hence skipping this Id: {}", existingCarId.get().getCarId());
            }
        }
    }
}
