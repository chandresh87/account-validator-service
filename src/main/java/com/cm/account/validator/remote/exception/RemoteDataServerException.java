package com.cm.account.validator.remote.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RemoteDataServerException extends RuntimeException {
  private String message;
  private HttpStatus httpStatus;

  public RemoteDataServerException(String message, HttpStatus httpStatus) {
    super(message);
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
