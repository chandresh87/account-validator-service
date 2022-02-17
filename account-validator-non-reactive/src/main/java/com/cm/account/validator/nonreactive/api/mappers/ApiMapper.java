package com.cm.account.validator.nonreactive.api.mappers;

import com.cm.account.validator.nonreactive.api.AccountRequestModel;
import com.cm.account.validator.nonreactive.api.ValidationResponseModel;
import com.cm.account.validator.nonreactive.services.AccountBO;
import com.cm.account.validator.nonreactive.services.ValidationBO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ApiMapper {

  ValidationResponseModel validationBOToModel(ValidationBO validationBO);

  AccountBO accountModelToBO(AccountRequestModel accountRequestModel);

  List<ValidationResponseModel> validationResponseModelListBOToModel(
      List<ValidationBO> validationBO);
}
