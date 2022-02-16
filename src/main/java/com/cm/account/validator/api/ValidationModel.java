package com.cm.account.validator.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationModel {
  private String source;
  private boolean isValid;
}
