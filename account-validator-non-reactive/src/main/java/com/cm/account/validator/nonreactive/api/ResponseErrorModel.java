package com.cm.account.validator.nonreactive.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseErrorModel {
  private HttpStatus statusCode;
  private List<String> errors;
}
