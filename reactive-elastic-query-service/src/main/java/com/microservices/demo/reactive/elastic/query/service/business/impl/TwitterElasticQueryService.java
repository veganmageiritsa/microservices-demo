package com.microservices.demo.reactive.elastic.query.service.business.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;

@Service
public class TwitterElasticQueryService implements ElasticQueryService {
    
    private final ElasticToResponseModelTransformer transformer;
    
    private final ReactiveElasticQueryClient<TwitterIndexModel> twitterReactiveElasticQueryClient;
    
    public TwitterElasticQueryService(
        final ElasticToResponseModelTransformer transformer,
        final ReactiveElasticQueryClient<TwitterIndexModel> twitterReactiveElasticQueryClient) {
        this.transformer = transformer;
        this.twitterReactiveElasticQueryClient = twitterReactiveElasticQueryClient;
    }
    
    
    @Override
    public Flux<ElasticQueryServiceResponseModel> getDocumentByText(final String text) {
        return twitterReactiveElasticQueryClient
            .getIndexModelByText(text)
            .map(transformer::getResponseModel);
    }
    
}
