package com.cm.account.validator.nonreactive.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationResponseModel {
  private String source;
  private boolean isValid;
}
