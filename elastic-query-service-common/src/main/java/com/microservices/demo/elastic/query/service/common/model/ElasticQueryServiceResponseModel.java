package com.microservices.demo.elastic.query.service.common.model;

import java.time.ZonedDateTime;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ElasticQueryServiceResponseModel extends RepresentationModel<ElasticQueryServiceResponseModel> {
    private Long id;
    private Long userId;
    private String text;
    
    private ZonedDateTime createdAt;
}
