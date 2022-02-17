package com.cm.account.validator.api.mappers;

import com.cm.account.validator.api.AccountRequestModel;
import com.cm.account.validator.api.ValidationResponseModel;
import com.cm.account.validator.services.AccountBO;
import com.cm.account.validator.services.ValidationBO;
import org.mapstruct.Mapper;

@Mapper
public interface ApiMapper {

  ValidationResponseModel validationBOToModel(ValidationBO validationBO);

  AccountBO accountModelToBO(AccountRequestModel accountRequestModel);
}
