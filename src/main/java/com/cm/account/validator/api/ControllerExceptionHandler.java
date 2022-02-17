package com.cm.account.validator.api;

import com.cm.account.validator.remote.exception.RemoteDataClientException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
  private static final String ERROR_MESSAGE = "Invalid input. Please check request params";

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ResponseErrorModel> handle(ConstraintViolationException exception) {

    log.error(exception.getMessage(), exception);

    List<String> constraintErrors =
        exception.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList());

    return Mono.just(new ResponseErrorModel(HttpStatus.BAD_REQUEST, constraintErrors));
  }

  @ExceptionHandler(WebExchangeBindException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ResponseErrorModel> handle(WebExchangeBindException exception) {
    log.error(exception.getMessage(), exception);

    List<String> fieldErrors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + " [" + fieldError.getDefaultMessage() + "]")
            .collect(Collectors.toList());

    return Mono.just(new ResponseErrorModel(HttpStatus.BAD_REQUEST, fieldErrors));
  }

  @ExceptionHandler(ServerWebInputException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Mono<ResponseErrorModel> handle(ServerWebInputException exception) {
    log.error(exception.getMessage(), exception);

    return Mono.just(
        new ResponseErrorModel(HttpStatus.BAD_REQUEST, Collections.singletonList(ERROR_MESSAGE)));
  }

  @ExceptionHandler(RemoteDataClientException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<ResponseErrorModel> handle(RemoteDataClientException exception) {
    log.error(exception.getMessage(), exception);

    return Mono.just(
        new ResponseErrorModel(
            HttpStatus.INTERNAL_SERVER_ERROR, Collections.singletonList(exception.getMessage())));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Mono<ResponseErrorModel> handle(Exception exception) {
    log.error(exception.getMessage(), exception);

    return Mono.just(
        new ResponseErrorModel(
            HttpStatus.INTERNAL_SERVER_ERROR, Collections.singletonList(exception.getMessage())));
  }
}
