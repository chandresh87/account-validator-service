package com.cm.account.validator.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountModel {

  @NotNull(message = "Account number is mandatory field")
  private Integer accountNumber;

  private List<String> sources;
}
