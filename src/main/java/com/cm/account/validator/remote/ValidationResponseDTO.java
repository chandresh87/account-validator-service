package com.cm.account.validator.remote;

import lombok.Data;

import java.io.Serializable;

@Data
public class ValidationResponseDTO implements Serializable {
  private Boolean isValid;
}
