package com.cm.account.validator.remote.exception;

import lombok.Data;

@Data
public class RemoteDataException extends RuntimeException {
  private String message;

  public RemoteDataException(String message) {
    super(message);
    this.message = message;
  }
}
