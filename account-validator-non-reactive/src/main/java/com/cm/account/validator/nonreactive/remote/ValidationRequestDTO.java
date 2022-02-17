package com.cm.account.validator.nonreactive.remote;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValidationRequestDTO implements Serializable {
  private static final long serialVersionUID = -2338626292552177485L;
  private Integer accountNumber;
}
