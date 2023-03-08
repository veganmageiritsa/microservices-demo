package com.microservices.demo.elastic.query.service.model.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.microservices.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservices.demo.elastic.query.service.api.ElasticDocumentController;
import com.microservices.demo.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.microservices.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler
    extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {
    
    private ElasticToResponseModelTransformer transformer;
    
    public ElasticQueryServiceResponseModelAssembler(final ElasticToResponseModelTransformer transformer) {
        super(ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
        this.transformer = transformer;
    }
    
    @Override
    public ElasticQueryServiceResponseModel toModel(final TwitterIndexModel twitterIndexModel) {
        return transformer.getResponseModel(twitterIndexModel)
                          .add(linkTo(methodOn(ElasticDocumentController.class)
                                          .getDocumentById(twitterIndexModel.getId())
                          ).withSelfRel())
                          .add(linkTo(ElasticDocumentController.class)
                                   .withRel("documents"));
        
    }
    
   
    public List<ElasticQueryServiceResponseModel> toModels(final List<TwitterIndexModel> twitterIndexModels) {
        return twitterIndexModels
            .stream()
            .map(this::toModel)
            .collect(Collectors.toList());
        
    }
}
