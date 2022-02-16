package com.cm.account.validator.services;

import reactor.core.publisher.Flux;

public interface ValidationService {

  Flux<ValidationBO> validateAccount(AccountBO accountBO);
}
