package com.microservices.demo.elastic.query.service.model;


import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryServiceRequestModel {
    private Long id;
    
    @NotEmpty
    private String text;
}
