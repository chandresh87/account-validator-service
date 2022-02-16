package com.cm.account.validator.services;

import com.cm.account.validator.remote.AccountValidationRemoteService;
import com.cm.account.validator.services.mappers.AccountServiceMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ValidationServiceImpl implements ValidationService {

  private final AccountValidationRemoteService accountValidationRemoteService;
  private final AccountServiceMapper accountServiceMapper;

  public ValidationServiceImpl(
      AccountValidationRemoteService accountValidationRemoteService,
      AccountServiceMapper accountServiceMapper) {
    this.accountValidationRemoteService = accountValidationRemoteService;
    this.accountServiceMapper = accountServiceMapper;
  }

  @Override
  public Flux<ValidationBO> validateAccount(AccountBO accountBO) {
    return accountValidationRemoteService
        .validateAccountNumber(accountServiceMapper.accountBOToAccountDTO(accountBO))
        .map(accountServiceMapper::validationDTOToBO);
  }
}
