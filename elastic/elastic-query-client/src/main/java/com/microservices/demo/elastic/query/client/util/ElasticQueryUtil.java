package com.microservices.demo.elastic.query.client.util;

import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import com.microservices.demo.elastic.model.index.IndexModel;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

@Component

public class ElasticQueryUtil<T extends IndexModel>{

    public Query getSearchQueryById(String id){
        return new NativeSearchQueryBuilder()
            .withIds(id)
            .build();
    }
    
    public Query getSearchQueryByFieldText(String field, String text){
        return new NativeSearchQueryBuilder()
            .withQuery(new BoolQueryBuilder()
                           .must(QueryBuilders.matchQuery(field, text)))
            .build();
    }
    
    public Query getSearchQueryForAll(){
        return new NativeSearchQueryBuilder()
            .withQuery(new BoolQueryBuilder()
                           .must(QueryBuilders.matchAllQuery()))
            .build();
    }
}
