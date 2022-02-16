package com.cm.account.validator.remote;

import reactor.core.publisher.Flux;

public interface AccountValidationRemoteService {

  Flux<ValidationDTO> validateAccountNumber(AccountDTO accountDTO);
}
