package com.cm.account.validator.nonreactive.services;

import com.cm.account.validator.nonreactive.remote.AccountValidationRemoteService;
import com.cm.account.validator.nonreactive.services.mappers.AccountServiceMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
  public List<ValidationBO> validateAccount(AccountBO accountBO) {
    return accountServiceMapper.validationDTOToBOValidationBoList(
        accountValidationRemoteService.validateAccountNumber(
            accountServiceMapper.accountBOToAccountDTO(accountBO)));
  }
}
