package com.cm.account.validator.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties(SourceEndpointProperty.class)
@Configuration
public class WebClientConfig {

  @Bean
  public Map<String, WebClient> webClientMap(SourceEndpointProperty sourceEndpointProperty) {
    Map<String, WebClient> clientMap = new HashMap<>();
    sourceEndpointProperty
        .getProviders()
        .forEach(
            source -> {
              WebClient webClient = WebClient.create(source.getUrl());
              clientMap.put(source.getName(), webClient);
            });
    return clientMap;
  }
}
