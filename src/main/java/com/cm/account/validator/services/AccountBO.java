package com.cm.account.validator.services;

import lombok.Data;

import java.util.List;

@Data
public class AccountBO {
  private Integer accountNumber;
  private List<String> sources;
}
