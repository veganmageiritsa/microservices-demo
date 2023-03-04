package com.microservices.demo.elastic.query.service.model;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryServiceResponseModel {
    private Long id;
    private Long userId;
    private String text;
    
    private ZonedDateTime createdAt;
}