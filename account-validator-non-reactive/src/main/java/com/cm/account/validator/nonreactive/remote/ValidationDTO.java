package com.cm.account.validator.nonreactive.remote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationDTO {
  private String source;
  private boolean isValid;
}
