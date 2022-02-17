package com.cm.account.validator.nonreactive.remote;

import java.util.List;

public interface AccountValidationRemoteService {

  List<ValidationDTO> validateAccountNumber(AccountDTO accountDTO);
}
