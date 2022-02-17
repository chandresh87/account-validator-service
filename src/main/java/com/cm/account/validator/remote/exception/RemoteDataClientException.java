package com.cm.account.validator.remote.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RemoteDataClientException extends RuntimeException {
  private String message;
  private HttpStatus httpStatus;

  public RemoteDataClientException(String message) {
    super(message);
    this.message = message;
    this.httpStatus = HttpStatus.BAD_REQUEST;
  }

  public RemoteDataClientException(String message, HttpStatus httpStatus) {
    super(message);
    this.message = message;
    this.httpStatus = httpStatus;
  }
}
