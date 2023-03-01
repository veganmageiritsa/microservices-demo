package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microservices.demo.config.KafkaConfigData;
import com.microservices.demo.config.KafkaConsumerConfigData;
import com.microservices.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.microservices.demo.kafka.to.elastic.service.transformer.AvroToElasticModelTransformer;

@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {
    
    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaConsumer.class);
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    
    private final KafkaAdminClient kafkaAdminClient;
    
    private final KafkaConfigData kafkaConfigData;
    
    private final AvroToElasticModelTransformer avroToElasticModelTransformer;
    
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;
    
    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    
    public TwitterKafkaConsumer(
        final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
        final KafkaAdminClient kafkaAdminClient,
        final KafkaConfigData kafkaConfigData,
        final AvroToElasticModelTransformer avroToElasticModelTransformer,
        final ElasticIndexClient<TwitterIndexModel> elasticIndexClient,
        final KafkaConsumerConfigData kafkaConsumerConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    }
    
    @EventListener
    public void onAppStart(ApplicationStartedEvent event){
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics created : {}", kafkaConfigData.getTopicNamesToCreate());
        Objects.requireNonNull(kafkaListenerEndpointRegistry
                                   .getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId()))
               .start();
    }
    
    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name:twitter-topic}")
    public void receive(@Payload final List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) final List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) final List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) final List<Long> offsets) {
        log.info("{} number of messages received with keys {} , partitions {} and offsets {}, sending to Thread Id : {}",
                 messages.size(), keys, partitions, offsets, Thread.currentThread().getId());
        List<TwitterIndexModel> elasticModels = avroToElasticModelTransformer.getElasticModels(messages);
        List<String> ids = elasticIndexClient.save(elasticModels);
        log.info("Documents saved to Elastic Search with ids : {}", ids);
    }
    
}
