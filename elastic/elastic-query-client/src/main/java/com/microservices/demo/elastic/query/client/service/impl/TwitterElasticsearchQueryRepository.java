package com.microservices.demo.elastic.query.client.service.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.microservices.demo.common.util.CollectionsUtil;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.repository.TwitterElasticSearchQueryRepository;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;

@Primary
@Service
public class TwitterElasticsearchQueryRepository implements ElasticQueryClient<TwitterIndexModel> {
    
    private final TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository;
    
    public TwitterElasticsearchQueryRepository(
        final TwitterElasticSearchQueryRepository twitterElasticSearchQueryRepository) {
        this.twitterElasticSearchQueryRepository = twitterElasticSearchQueryRepository;
    }
    
    @Override
    public TwitterIndexModel getIndexModelById(final String id) {
        return twitterElasticSearchQueryRepository.findById(id)
                                                  .orElseThrow(() -> new ElasticQueryClientException("No Document Found for id: " + id));
    }
    
    @Override
    public List<TwitterIndexModel> getIndexModelByText(final String text) {
        return twitterElasticSearchQueryRepository.findByText(text);
    }
    
    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        return CollectionsUtil
            .getInstance()
            .getListFromIterable(twitterElasticSearchQueryRepository.findAll());
    }
    
}
