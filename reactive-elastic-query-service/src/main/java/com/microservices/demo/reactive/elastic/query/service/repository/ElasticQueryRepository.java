package com.microservices.demo.reactive.elastic.query.service.repository;

import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

@Repository
public interface ElasticQueryRepository
    extends ReactiveElasticsearchRepository<TwitterIndexModel, String> {
    
    Flux<TwitterIndexModel> findByText(String text);
}
