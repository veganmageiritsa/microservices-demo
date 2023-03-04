package com.microservices.demo.elastic.query.service.transformer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;

@Component
public class ElasticToResponseModelTransformer {
    
    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel twitterIndexModel) {
        return ElasticQueryServiceResponseModel
            .builder()
            .text(twitterIndexModel.getText())
            .id(Long.valueOf(twitterIndexModel.getId()))
            .userId(twitterIndexModel.getUserId())
            .createdAt(twitterIndexModel.getCreatedAt())
            .build();
    }
    
    public List<ElasticQueryServiceResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels
            .stream()
            .map(this::getResponseModel)
            .collect(Collectors.toList());
        
    }
    
}
