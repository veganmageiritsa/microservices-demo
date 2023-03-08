package com.microservices.demo.reactive.elastic.query.service.business;

import reactor.core.publisher.Flux;

import com.microservices.demo.elastic.model.index.IndexModel;

public interface ReactiveElasticQueryClient<T extends IndexModel> {
    Flux<T> getIndexModelByText(String text);
}
