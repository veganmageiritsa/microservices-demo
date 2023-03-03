package com.microservices.demo.elastic.query.client.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microservices.demo.config.ElasticConfigData;
import com.microservices.demo.config.ElasticQueryConfigData;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.client.exception.ElasticQueryClientException;
import com.microservices.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservices.demo.elastic.query.client.util.ElasticQueryUtil;

@Service
public class TiwtterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {
    
    private final Logger log = LoggerFactory.getLogger(TiwtterElasticQueryClient.class);
    
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;
    
    private final ElasticConfigData elasticConfigData;
    
    private final ElasticQueryConfigData elasticQueryConfigData;
    
    private final ElasticsearchOperations elasticsearchOperations;
    
    public TiwtterElasticQueryClient(
        final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil,
        final ElasticConfigData elasticConfigData,
        final ElasticQueryConfigData elasticQueryConfigData,
        final ElasticsearchOperations elasticsearchOperations) {
        this.elasticQueryUtil = elasticQueryUtil;
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
    }
    
    @Override
    public TwitterIndexModel getIndexModelById(final String id) {
        Query query = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchHit = elasticsearchOperations.searchOne(query,
                                                                                   TwitterIndexModel.class,
                                                                                   IndexCoordinates.of(elasticConfigData.getIndexName()));
        return Optional.ofNullable(searchHit)
            .map(SearchHit::getContent)
            .orElseThrow(()->new ElasticQueryClientException("No Document found with id " + id));
    }
    
    @Override
    public List<TwitterIndexModel> getIndexModelByText(final String text) {
        Query query = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        return search(query);
    }
    
    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query query = elasticQueryUtil.getSearchQueryForAll();
        return search(query);
    }
    
    private List<TwitterIndexModel> search(final Query query) {
        SearchHits<TwitterIndexModel> searchHits = elasticsearchOperations.search(query, TwitterIndexModel.class,
                                                                                  IndexCoordinates.of(elasticConfigData.getIndexName()));
        
        return searchHits.get()
                         .map(SearchHit::getContent)
                         .collect(Collectors.toList());
    }
    
}
