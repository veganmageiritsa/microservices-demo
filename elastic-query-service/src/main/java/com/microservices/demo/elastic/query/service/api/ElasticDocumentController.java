package com.microservices.demo.elastic.query.service.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticDocumentController.class);
    
    private final ElasticQueryService elasticQueryService;
    
    public ElasticDocumentController(final ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }
    
    @GetMapping("/v1")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        return ResponseEntity.ok(elasticQueryService.getAllDocuments());
    }
    
    @GetMapping("v1/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        return ResponseEntity.ok(elasticQueryService.getDocumentById(id));
    }
    
    @PostMapping("v1//getDocumentByText")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        return ResponseEntity.ok(elasticQueryService.getDocumentByText(requestModel.getText()));
    }
    
    @GetMapping("/v2")
    public ResponseEntity<List<ElasticQueryServiceResponseModelV2>> getAllDocumentsV2() {
        List<ElasticQueryServiceResponseModelV2> elasticQueryServiceResponseModelV2s =
            elasticQueryService.getAllDocuments()
                               .stream()
                               .map(convertFunction)
                               .collect(Collectors.toList());
        return ResponseEntity.ok(elasticQueryServiceResponseModelV2s);
    }
    
    
    @GetMapping("v2/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        return ResponseEntity.ok(convertFunction.apply(elasticQueryService.getDocumentById(id)));
    }
    
    @PostMapping("v2//getDocumentByText")
    public ResponseEntity<List<ElasticQueryServiceResponseModelV2>> getDocumentByTextV2(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        List<ElasticQueryServiceResponseModelV2> elasticQueryServiceResponseModelV2s =
            elasticQueryService.getDocumentByText(requestModel.getText())
                               .stream()
                               .map(convertFunction)
                               .collect(Collectors.toList());
    
        return ResponseEntity.ok(elasticQueryServiceResponseModelV2s);
    
    }
    
    
    Function<ElasticQueryServiceResponseModel, ElasticQueryServiceResponseModelV2> convertFunction =
        response -> ElasticQueryServiceResponseModelV2
            .builder()
            .userId(response.getUserId())
            .text(response.getText())
            .id(String.valueOf(response.getId()))
            .createdAt(response.getCreatedAt())
            .build().add(response.getLinks());
    
}

