package com.microservices.demo.elastic.index.client.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microservices.demo.elastic.index.client.repository.TwitterElasticSearchRepository;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;

@Primary
@Service
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    
    private static final Logger log = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);
    
    private final TwitterElasticSearchRepository twitterElasticSearchRepository;
    
    public TwitterElasticRepositoryIndexClient(final TwitterElasticSearchRepository twitterElasticSearchRepository) {
        this.twitterElasticSearchRepository = twitterElasticSearchRepository;
    }
    
    @Override
    public List<String> save(final List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> twitterIndexModels =(List<TwitterIndexModel>) twitterElasticSearchRepository.saveAll(documents);
        
        return twitterIndexModels
            .stream()
            .map(TwitterIndexModel::getId)
            .collect(Collectors.toList());
    }
    
}
