package com.microservices.demo.elastic.query.client.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

@Repository
public interface TwitterElasticSearchQueryRepository
    extends ElasticsearchRepository<TwitterIndexModel, String> {

    List<TwitterIndexModel> findByText(String text);
    
}
