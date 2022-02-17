package com.cm.account.validator.config;

import com.cm.account.validator.remote.exception.RemoteDataClientException;
import com.cm.account.validator.remote.exception.RemoteDataServerException;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(SourceEndpointProperty.class)
@Configuration
@Slf4j
public class WebClientConfig {

  @Bean
  public Map<String, WebClient> webClientMap(
      SourceEndpointProperty sourceEndpointProperty,
      @Value("${config.readTimeout}") int readTimeout,
      @Value("${config.ConnectTimeout}") int ConnectTimeout) {
    Map<String, WebClient> clientMap = new HashMap<>();
    sourceEndpointProperty
        .getProviders()
        .forEach(
            source -> {
              WebClient webClient =
                  getBuilder(
                          HttpClient.create(),
                          ConnectTimeout,
                          readTimeout,
                          source.getUrl(),
                          source.getName())
                      .build();

              clientMap.put(source.getName(), webClient);
            });
    return clientMap;
  }

  private void logTraceResponse(ClientResponse response, String serviceName) {

    log.error("server {} response status: {}", serviceName, response.statusCode());
    log.error("server response headers: {} {}", serviceName, response.headers().asHttpHeaders());
    response
        .bodyToMono(String.class)
        .subscribe(
            body ->
                log.error(
                    "Error while getting data from remote client {} with response body: {}",
                    serviceName,
                    body));
  }

  private ExchangeFilterFunction logResponse(String serviceName) {
    return ExchangeFilterFunction.ofResponseProcessor(
        clientResponse -> {
          final HttpStatus httpStatus = clientResponse.statusCode();
          if (httpStatus.isError()) {
            logTraceResponse(clientResponse, serviceName);
            if (httpStatus.is5xxServerError()) {
              return Mono.error(
                  new RemoteDataServerException(
                      String.format("Server Error from remote service %s", serviceName),
                      httpStatus));
            } else
              return Mono.error(
                  new RemoteDataClientException(
                      String.format("Client Error from remote service %s", serviceName),
                      httpStatus));
          }
          return Mono.just(clientResponse);
        });
  }

  private ExchangeFilterFunction logRequest(String serviceName) {
    return ExchangeFilterFunction.ofRequestProcessor(
        clientRequest -> {
          log.info(
              "calling remote service {} {} method {}",
              serviceName,
              clientRequest.url(),
              clientRequest.method());

          clientRequest
              .headers()
              .forEach(
                  (name, values) ->
                      log.info("Key {} and value {} for service {}", name, values, serviceName));

          return Mono.just(clientRequest);
        });
  }

  private WebClient.Builder getBuilder(
      HttpClient httpClient,
      int connectTimeout,
      int readTimeout,
      String baseUrl,
      String serviceName) {
    return WebClient.builder()
        .baseUrl(baseUrl)
        .clientConnector(
            new ReactorClientHttpConnector(
                httpClient
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                    .doOnConnected(
                        c ->
                            c.addHandlerLast(
                                    new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(
                                    new WriteTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS)))))
        .filters(
            exchangeFilterFunctions -> {
              exchangeFilterFunctions.add(logResponse(serviceName));
              exchangeFilterFunctions.add(logRequest(serviceName));
            });
  }
}
