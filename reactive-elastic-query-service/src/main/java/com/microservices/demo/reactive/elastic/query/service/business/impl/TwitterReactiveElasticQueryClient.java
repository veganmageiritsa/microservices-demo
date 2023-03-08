package com.microservices.demo.reactive.elastic.query.service.business.impl;

import java.time.Duration;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import com.microservices.demo.reactive.elastic.query.service.repository.ElasticQueryRepository;

@Service
public class TwitterReactiveElasticQueryClient
    implements ReactiveElasticQueryClient<TwitterIndexModel> {
    
    private final ElasticQueryRepository elasticQueryRepository;
    
    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
    
    public TwitterReactiveElasticQueryClient(final ElasticQueryRepository elasticQueryRepository,
                                             final ElasticQueryServiceConfigData elasticQueryServiceConfigData) {
        this.elasticQueryRepository = elasticQueryRepository;
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
    }
    
    
    @Override
    public Flux<TwitterIndexModel> getIndexModelByText(final String text) {
        return elasticQueryRepository
            .findByText(text)
            .delayElements(Duration.ofMillis(elasticQueryServiceConfigData.getBackPressureDelayMs()));
    }
    
}
