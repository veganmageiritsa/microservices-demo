package com.microservices.demo.elastic.query.service.api;

import java.util.ArrayList;
import java.util.List;

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

@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticDocumentController.class);
    
    private final ElasticQueryService elasticQueryService;
    
    public ElasticDocumentController(final ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }
    
    @GetMapping("/")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        return ResponseEntity.ok(elasticQueryService.getAllDocuments());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable String id) {
        return ResponseEntity.ok(elasticQueryService.getDocumentById(id));
    }
    
    @PostMapping("/getDocumentByText")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody ElasticQueryServiceRequestModel requestModel) {
        return ResponseEntity.ok(elasticQueryService.getDocumentByText(requestModel.getText()));
    }
    
}
