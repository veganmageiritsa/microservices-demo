package com.microservices.demo.elastic.query.service.business.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {
    
    private final ElasticQueryServiceResponseModelAssembler assembler;
    
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    
    public TwitterElasticQueryService(
        final ElasticQueryServiceResponseModelAssembler assembler,
        final ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.assembler = assembler;
        this.elasticQueryClient = elasticQueryClient;
    }
    
    @Override
    public ElasticQueryServiceResponseModel getDocumentById(final String id) {
        return assembler.toModel(elasticQueryClient.getIndexModelById(id));
        
    }
    
    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(final String text) {
        return assembler.toModels(elasticQueryClient.getIndexModelByText(text));
    }
    
    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        return assembler.toModels(elasticQueryClient.getAllIndexModels());
    
    }
    
}
