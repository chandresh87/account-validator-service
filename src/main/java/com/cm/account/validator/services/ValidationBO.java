package com.cm.account.validator.services;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationBO {
  private String source;
  private boolean isValid;
}
