package com.microservices.demo.reactive.elastic.query.service.business;

import reactor.core.publisher.Flux;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;

public interface ElasticQueryService {
    Flux<ElasticQueryServiceResponseModel> getDocumentByText(String text);
}
