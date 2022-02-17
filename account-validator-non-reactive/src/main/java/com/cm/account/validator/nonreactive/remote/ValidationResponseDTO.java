package com.cm.account.validator.nonreactive.remote;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidationResponseDTO implements Serializable {
  private static final long serialVersionUID = -2338626292552177500L;
  private Boolean isValid;
}
