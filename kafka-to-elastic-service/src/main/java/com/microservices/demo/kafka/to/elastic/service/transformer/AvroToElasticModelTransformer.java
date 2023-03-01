package com.microservices.demo.kafka.to.elastic.service.transformer;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;

@Component
public class AvroToElasticModelTransformer {
    
    public List<TwitterIndexModel> getElasticModels(List<TwitterAvroModel> avroModels) {
        return avroModels
            .stream()
            .map(twitterAvroModel -> TwitterIndexModel
                .builder()
                .userId(twitterAvroModel.getUserId())
                .text(twitterAvroModel.getText())
                .id(String.valueOf(twitterAvroModel.getId()))
                .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(twitterAvroModel.getCreatedAt()),
                                                   ZoneId.systemDefault()))
                .build()).collect(Collectors.toList());
    }
    
}
