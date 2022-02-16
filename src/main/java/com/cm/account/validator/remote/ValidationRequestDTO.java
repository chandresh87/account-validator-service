package com.cm.account.validator.remote;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationRequestDTO {
  private Integer accountNumber;
}
