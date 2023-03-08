package com.microservices.demo.reactive.elastic.query.service.api;

import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.reactive.elastic.query.service.business.ElasticQueryService;

@RestController
@RequestMapping("/documents")
public class ReactiveElasticDocumentController {

    private final ElasticQueryService elasticQueryService;
    
    public ReactiveElasticDocumentController(final ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }
    
    @PostMapping(value = "/get-doc-by-text",
                 produces = MediaType.TEXT_EVENT_STREAM_VALUE,
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryServiceResponseModel>
    getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel){
        return elasticQueryService.getDocumentByText(requestModel.getText())
            .log();
    }
}
