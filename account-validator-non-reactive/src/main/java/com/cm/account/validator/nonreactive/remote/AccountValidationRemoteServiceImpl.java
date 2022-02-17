package com.cm.account.validator.nonreactive.remote;

import com.cm.account.validator.nonreactive.remote.exception.RemoteDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountValidationRemoteServiceImpl implements AccountValidationRemoteService {

  private final Map<String, RestTemplate> restTemplateMap;
  private final TaskExecutor taskExecutor;

  public AccountValidationRemoteServiceImpl(
      Map<String, RestTemplate> restTemplateMap,
      @Qualifier("sourceExecutor") TaskExecutor taskExecutor) {
    this.restTemplateMap = restTemplateMap;
    this.taskExecutor = taskExecutor;
  }

  @Override
  public List<ValidationDTO> validateAccountNumber(AccountDTO accountDTO) {

    List<CompletableFuture<ValidationDTO>> completableFutures;

    if (accountDTO.getAccountNumber() == null) {
      throw new RemoteDataException("Account number is mandatory");
    }
    // sending request to all sources
    if (CollectionUtils.isEmpty(accountDTO.getSources())) {

      log.info("Sending request to all sources");
      completableFutures =
          restTemplateMap.entrySet().stream()
              .map(
                  entry -> {
                    ValidationRequestDTO validationRequestDTO =
                        new ValidationRequestDTO(accountDTO.getAccountNumber());

                    return CompletableFuture.supplyAsync(
                        () ->
                            callExternalSource(
                                entry.getValue(), validationRequestDTO, entry.getKey()),
                        taskExecutor);
                  })
              .collect(Collectors.toList());

    }
    // sending request to selected sources
    else {
      log.info("Sending request to selected sources {}", accountDTO.getSources());
      completableFutures =
          accountDTO.getSources().stream()
              .filter(restTemplateMap::containsKey)
              .map(
                  source -> {
                    ValidationRequestDTO validationRequestDTO =
                        new ValidationRequestDTO(accountDTO.getAccountNumber());
                    return CompletableFuture.supplyAsync(
                        () ->
                            callExternalSource(
                                restTemplateMap.get(source), validationRequestDTO, source),
                        taskExecutor);
                  })
              .collect(Collectors.toList());
    }
    return completableFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
  }

  private ValidationDTO callExternalSource(
      RestTemplate restTemplate, ValidationRequestDTO validationRequestDTO, String source) {
    // Post requests to the sources

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<?> httpEntity = new HttpEntity<Object>(validationRequestDTO, headers);
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    ResponseEntity<ValidationResponseDTO> response =
        restTemplate.exchange("", HttpMethod.POST, httpEntity, ValidationResponseDTO.class);
    stopWatch.stop();
    log.info(
        "Time taken by the service {} is {} with response {}",
        source,
        stopWatch.getTotalTimeMillis(),
        response.getBody());

    return new ValidationDTO(source, response.getBody().getIsValid());
  }
}
