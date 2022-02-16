package com.cm.account.validator.remote;

import lombok.Data;

import java.util.List;

@Data
public class AccountDTO {
  private Integer accountNumber;
  private List<String> sources;
}
