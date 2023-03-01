package com.microservices.demo.kafka.to.elastic.service.consumer.impl;

import java.util.List;

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
import com.microservices.demo.kafka.admin.client.KafkaAdminClient;
import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.to.elastic.service.consumer.KafkaConsumer;

@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {
    
    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaConsumer.class);
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    
    private final KafkaAdminClient kafkaAdminClient;
    
    private final KafkaConfigData kafkaConfigData;
    
    public TwitterKafkaConsumer(
        final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
        final KafkaAdminClient kafkaAdminClient,
        final KafkaConfigData kafkaConfigData) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
    }
    
    @EventListener
    public void onAppStart(ApplicationStartedEvent event){
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics created : {}", kafkaConfigData.getTopicNamesToCreate());
        kafkaListenerEndpointRegistry.getListenerContainer("twitterTopicListener").start();
    }
    
    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name:twitter-topic}")
    public void receive(@Payload final List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) final List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) final List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) final List<Long> offsets) {
        log.info("{} number of messages received with keys {} , partitions {} and offsets {}, sending to Thread Id : {}",
                 messages.size(), keys, partitions, offsets, Thread.currentThread().getId());
    }
    
}
