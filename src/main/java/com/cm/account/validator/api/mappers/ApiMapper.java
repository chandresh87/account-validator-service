package com.cm.account.validator.api.mappers;

import com.cm.account.validator.api.AccountModel;
import com.cm.account.validator.api.ValidationModel;
import com.cm.account.validator.services.AccountBO;
import com.cm.account.validator.services.ValidationBO;
import org.mapstruct.Mapper;

@Mapper
public interface ApiMapper {

  ValidationModel validationBOToModel(ValidationBO validationBO);

  AccountBO accountModelToBO(AccountModel accountModel);
}
