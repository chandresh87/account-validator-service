package com.cm.account.validator.api;

import com.cm.account.validator.api.mappers.ApiMapper;
import com.cm.account.validator.services.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(
    path = "/api/v1/validate",
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Slf4j
public class AccountController {

  private final ApiMapper apiMapper;
  private final ValidationService validationService;

  public AccountController(ApiMapper apiMapper, ValidationService validationService) {
    this.apiMapper = apiMapper;
    this.validationService = validationService;
  }

  @PostMapping(path = "/account")
  public Flux<ValidationResponseModel> validateAccount(
      @Valid @RequestBody Mono<AccountRequestModel> accountRequestModel) {

    return accountRequestModel
        .doOnNext(
            accountRequestModel1 -> log.info("Validation request for {}", accountRequestModel1))
        .map(apiMapper::accountModelToBO)
        .flatMapMany(validationService::validateAccount)
        .map(apiMapper::validationBOToModel);
  }
}
