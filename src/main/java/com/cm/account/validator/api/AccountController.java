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
  public Flux<ValidationModel> validateAccount(@Valid @RequestBody AccountModel accountModel) {
    log.info("Validation request for {}", accountModel);
    return validationService
        .validateAccount(apiMapper.accountModelToBO(accountModel))
        .map(apiMapper::validationBOToModel);
  }
}
