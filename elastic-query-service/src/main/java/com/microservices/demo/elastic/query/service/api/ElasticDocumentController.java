package com.microservices.demo.elastic.query.service.api;

import java.util.List;
import java.util.function.Function;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microservices.demo.elastic.query.service.business.ElasticQueryService;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.model.ElasticQueryServiceResponseModelV2;

@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticDocumentController.class);
    
    private final ElasticQueryService elasticQueryService;
    
    public ElasticDocumentController(final ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }
    
    @Operation(summary = "Get All Documents")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success",content = {
            @Content(mediaType = "application/vnd.api.v1+json",
                     schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            
        } ),
        @ApiResponse(responseCode = "400", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        return ResponseEntity.ok(elasticQueryService.getAllDocuments());
    }
    
    
    @Operation(summary = "Get Document By Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success",content = {
            @Content(mediaType = "application/vnd.api.v1+json",
                     schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            
        } ),
        @ApiResponse(responseCode = "400", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        return ResponseEntity.ok(elasticQueryService.getDocumentById(id));
    }
    
    
    @Operation(summary = "Get Document By Id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success",content = {
            @Content(mediaType = "application/vnd.api.v2+json",
                     schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            
        } ),
        @ApiResponse(responseCode = "400", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        return ResponseEntity.ok(convertFunction.apply(elasticQueryService.getDocumentById(id)));
    }
    
    @Operation(summary = "Get Document By Text")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Success",content = {
            @Content(mediaType = "application/vnd.api.v1+json",
                     schema = @Schema(implementation = ElasticQueryServiceResponseModel.class))
            
        } ),
        @ApiResponse(responseCode = "400", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/getDocumentByText")
    public ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        return ResponseEntity.ok(elasticQueryService.getDocumentByText(requestModel.getText()));
    }
    
    Function<ElasticQueryServiceResponseModel, ElasticQueryServiceResponseModelV2> convertFunction =
        response -> ElasticQueryServiceResponseModelV2
            .builder()
            .userId(response.getUserId())
            .text(response.getText())
            .id(String.valueOf(response.getId()))
            .build().add(response.getLinks());
}
