package com.cm.account.validator.services.mappers;

import com.cm.account.validator.remote.AccountDTO;
import com.cm.account.validator.remote.ValidationDTO;
import com.cm.account.validator.services.AccountBO;
import com.cm.account.validator.services.ValidationBO;
import org.mapstruct.Mapper;

@Mapper
public interface AccountServiceMapper {

  AccountDTO accountBOToAccountDTO(AccountBO accountBO);

  ValidationBO validationDTOToBO(ValidationDTO validationDTO);
}
