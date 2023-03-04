package com.microservices.demo.elastic.query.service.business.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {
    
    private final ElasticToResponseModelTransformer transformer;
    
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    
    public TwitterElasticQueryService(final ElasticToResponseModelTransformer transformer,
                                      final ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.transformer = transformer;
        this.elasticQueryClient = elasticQueryClient;
    }
    
    @Override
    public ElasticQueryServiceResponseModel getDocumentById(final String id) {
        return transformer.getResponseModel(elasticQueryClient.getIndexModelById(id));
        
    }
    
    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(final String text) {
        return transformer.getResponseModels(elasticQueryClient.getIndexModelByText(text));
    }
    
    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        return transformer.getResponseModels(elasticQueryClient.getAllIndexModels());
    
    }
    
}
