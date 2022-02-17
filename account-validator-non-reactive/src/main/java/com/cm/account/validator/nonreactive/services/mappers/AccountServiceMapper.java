package com.cm.account.validator.nonreactive.services.mappers;

import com.cm.account.validator.nonreactive.remote.AccountDTO;
import com.cm.account.validator.nonreactive.remote.ValidationDTO;
import com.cm.account.validator.nonreactive.services.AccountBO;
import com.cm.account.validator.nonreactive.services.ValidationBO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface AccountServiceMapper {

  AccountDTO accountBOToAccountDTO(AccountBO accountBO);

  ValidationBO validationDTOToBO(ValidationDTO validationDTO);

  List<ValidationBO> validationDTOToBOValidationBoList(List<ValidationDTO> validationDTO);
}
