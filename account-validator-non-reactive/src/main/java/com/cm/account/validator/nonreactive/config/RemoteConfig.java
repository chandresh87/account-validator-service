package com.cm.account.validator.nonreactive.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties(SourceEndpointProperty.class)
@Configuration
public class RemoteConfig {

  @Bean
  public Map<String, RestTemplate> webClientMap(SourceEndpointProperty sourceEndpointProperty) {
    Map<String, RestTemplate> clientMap = new HashMap<>();
    sourceEndpointProperty
        .getProviders()
        .forEach(
            source -> {
              RestTemplate restTemplate = new RestTemplate();
              restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(source.getUrl()));
              clientMap.put(source.getName(), restTemplate);
            });
    return clientMap;
  }

  @Bean
  public TaskExecutor sourceExecutor(SourceEndpointProperty sourceEndpointProperty) {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(sourceEndpointProperty.getProviders().size());
    executor.setThreadNamePrefix("default_task_executor_thread");
    executor.setDaemon(true);
    executor.initialize();
    return executor;
  }
}
