package com.cm.account.validator.nonreactive.services;

import java.util.List;

public interface ValidationService {

  List<ValidationBO> validateAccount(AccountBO accountBO);
}
