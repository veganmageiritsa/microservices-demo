package com.microservices.demo.elastic.query.web.client.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.apache.http.HttpHeaders;

import com.microservices.demo.config.ElasticQueryWebClientConfigData;
import com.microservices.demo.config.UserConfigData;

@Configuration
@LoadBalancerClient(name = "elastic-query-service", configuration = ElasticQueryServiceInstanceListSupplierConfig.class)
public class WebClientConfig {

    private final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;

    private final UserConfigData userConfigData;

    public WebClientConfig(ElasticQueryWebClientConfigData webClientConfigData, UserConfigData userData) {
        this.elasticQueryWebClientConfigData = webClientConfigData.getWebClient();
        this.userConfigData = userData;
    }

    @LoadBalanced
    @Bean("webClientBuilder")
    WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                        .filter(ExchangeFilterFunctions
                                    .basicAuthentication(userConfigData.getUsername(), userConfigData.getPassword()))
                        .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticQueryWebClientConfigData.getContentType())
                        .defaultHeader(HttpHeaders.ACCEPT, elasticQueryWebClientConfigData.getAcceptType())
                        .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
                        .codecs(clientCodecConfigurer ->
                                    clientCodecConfigurer
                                        .defaultCodecs()
                                        .maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()));
 
    }
    
    private HttpClient getHttpClient() {
        return HttpClient.create()
                         .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticQueryWebClientConfigData.getConnectTimeoutMs())
                         .doOnConnected(connection -> {
                             connection.addHandlerLast(new ReadTimeoutHandler(elasticQueryWebClientConfigData.getReadTimeoutMs(),
                                                                              TimeUnit.MILLISECONDS));
                             connection.addHandlerLast(
                                 new WriteTimeoutHandler(elasticQueryWebClientConfigData.getWriteTimeoutMs(),
                                                         TimeUnit.MILLISECONDS));
                         });
    }
    
//    @Bean("webClient")
//    WebClient webClient() {
//        return WebClient.builder()
//                        .baseUrl(webClientConfig.getBaseUrl())
//                        .defaultHeader(HttpHeaders.CONTENT_TYPE, webClientConfig.getContentType())
//                        .clientConnector(new ReactorClientHttpConnector(getHttpClient()))
//                        .codecs(configurer -> configurer
//                            .defaultCodecs()
//                            .maxInMemorySize(webClientConfig.getMaxInMemorySize()))
//                        .build();
//    }
//
//    private HttpClient getHttpClient() {
//        return HttpClient.create()
//                         .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientConfig.getConnectTimeoutMs())
//                         .doOnConnected(connection -> {
//                             connection.addHandlerLast(new ReadTimeoutHandler(webClientConfig.getReadTimeoutMs(),
//                                                                              TimeUnit.MILLISECONDS));
//                             connection.addHandlerLast(new WriteTimeoutHandler(webClientConfig.getWriteTimeoutMs(),
//                                                                               TimeUnit.MILLISECONDS));
//                         });
//    }

}
