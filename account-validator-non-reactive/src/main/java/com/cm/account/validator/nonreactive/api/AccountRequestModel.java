package com.cm.account.validator.nonreactive.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountRequestModel implements Serializable {

  private static final long serialVersionUID = -2338626292552177968L;

  @NotNull(message = "Account number is mandatory field")
  private Integer accountNumber;

  private List<String> sources;
}
