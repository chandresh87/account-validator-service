package com.cm.account.validator.remote;

import com.cm.account.validator.remote.exception.RemoteDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AccountValidationRemoteServiceImpl implements AccountValidationRemoteService {

  private final Map<String, WebClient> webClientMap;

  public AccountValidationRemoteServiceImpl(Map<String, WebClient> webClientMap) {
    this.webClientMap = webClientMap;
  }

  @Override
  public Flux<ValidationDTO> validateAccountNumber(AccountDTO accountDTO) {

    Flux<ValidationDTO> validationDTOFlux = null;

    if (accountDTO.getAccountNumber() == null) {
      throw new RemoteDataException("Account number is mandatory");
    }
    // sending request to all sources
    if (CollectionUtils.isEmpty(accountDTO.getSources())) {
      List<Mono<ValidationDTO>> monoList =
          webClientMap.entrySet().stream()
              .map(
                  entry -> {
                    ValidationRequestDTO validationRequestDTO =
                        new ValidationRequestDTO(accountDTO.getAccountNumber());
                    return callExternalSource(
                        entry.getValue(), validationRequestDTO, entry.getKey());
                  })
              .collect(Collectors.toList());

      validationDTOFlux = Flux.merge(monoList);

    }
    // sending request to selected sources
    else {
      List<Mono<ValidationDTO>> monoList =
          accountDTO.getSources().stream()
              .filter(webClientMap::containsKey)
              .map(
                  source -> {
                    ValidationRequestDTO validationRequestDTO =
                        new ValidationRequestDTO(accountDTO.getAccountNumber());
                    return callExternalSource(
                        webClientMap.get(source), validationRequestDTO, source);
                  })
              .collect(Collectors.toList());
      validationDTOFlux = Flux.merge(monoList);
    }
    return validationDTOFlux;
  }

  private Mono<ValidationDTO> callExternalSource(
      WebClient webClient, ValidationRequestDTO validationRequestDTO, String source) {
    return webClient
        .post()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(validationRequestDTO))
        .exchangeToMono(clientResponse -> clientResponse.bodyToMono(ValidationResponseDTO.class))
        .elapsed()
        .doOnNext(
            tuple ->
                log.info(
                    "Time taken by the service {} is {} with response {}",
                    source,
                    tuple.getT1(),
                    tuple.getT2().getIsValid()))
        .map(tuple -> new ValidationDTO(source, tuple.getT2().getIsValid()));
  }
}
