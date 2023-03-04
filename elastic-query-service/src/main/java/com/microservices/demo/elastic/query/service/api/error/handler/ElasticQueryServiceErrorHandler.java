package com.microservices.demo.elastic.query.service.api.error.handler;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class ElasticQueryServiceErrorHandler {
    
    private static final Logger log = LoggerFactory.getLogger(ElasticQueryServiceErrorHandler.class);
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException e) {
        log.error("Access Denied Exception", e);
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body("You are not authorized to access this resource");
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e) {
        log.error("Illegal Argument Exception", e);
        return ResponseEntity
            .badRequest()
            .body("Illegal Argument Exception" + e.getMessage());
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException e) {
        log.error("Service Runtime Exception", e);
        return ResponseEntity
            .badRequest()
            .body("Service Runtime Exception" + e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e) {
        log.error("Internal Server Error", e);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("A server error occurred" + e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException e) {
        log.error("Validation Error ", e);
        Map<String, String> errorsMap = e.getBindingResult().getAllErrors()
                                         .stream()
                                         .collect(Collectors.toMap(error -> ((FieldError) error).getField(),
                                                                   DefaultMessageSourceResolvable::getDefaultMessage));
        
        return ResponseEntity
            .badRequest()
            .body(errorsMap);
    }
    
}
