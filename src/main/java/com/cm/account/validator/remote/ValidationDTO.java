package com.cm.account.validator.remote;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationDTO {
  private String source;
  private boolean isValid;
}
