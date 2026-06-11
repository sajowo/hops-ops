package edu.prz.hopsops.configuration.exception;

import edu.prz.hopsops.foundation.application.ProblemResponse;
import edu.prz.hopsops.foundation.domain.DomainException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    List<String> violations = ex.getBindingResult().getFieldErrors().stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .toList();

    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("validationFailed");
    problemResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    problemResponse.setTitle("Request validation failed");
    problemResponse.setViolations(violations);

    return handleExceptionInternal(ex, problemResponse, headers, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Object> handle(EntityNotFoundException e, WebRequest request) {
    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("entityNotFound");
    problemResponse.setStatus(HttpStatus.NOT_FOUND.value());
    problemResponse.setTitle("Entity not found");
    problemResponse.setDetail(e.getMessage());

    return handleExceptionInternal(e, problemResponse, new HttpHeaders(), HttpStatus.NOT_FOUND,
        request);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> handle(ValidationException e, WebRequest request) {
    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("validationFailed");
    problemResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    problemResponse.setTitle("Validation failed");
    problemResponse.setDetail(e.getMessage());

    return handleExceptionInternal(e, problemResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
        request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Object> handle(IllegalArgumentException e, WebRequest request) {
    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("validationFailed");
    problemResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    problemResponse.setTitle("Validation failed");
    problemResponse.setDetail(e.getMessage());

    return handleExceptionInternal(e, problemResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
        request);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Object> handle(IllegalStateException e, WebRequest request) {
    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("domainRuleBroken");
    problemResponse.setStatus(HttpStatus.CONFLICT.value());
    problemResponse.setTitle("Domain rule broken");
    problemResponse.setDetail(e.getMessage());

    return handleExceptionInternal(e, problemResponse, new HttpHeaders(), HttpStatus.CONFLICT,
        request);
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Object> handle(DomainException e, WebRequest request) {
    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("domainRuleBroken");
    problemResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    problemResponse.setTitle("Domain rule broken");
    problemResponse.setDetail(e.getMessage());

    return handleExceptionInternal(e, problemResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST,
        request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handle(Exception e, WebRequest request) {
    log.error(e.getMessage(), e);

    ProblemResponse problemResponse = new ProblemResponse();
    problemResponse.setType("serverError");
    problemResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    problemResponse.setTitle("Server error");

    return handleExceptionInternal(e, problemResponse, new HttpHeaders(),
        HttpStatus.INTERNAL_SERVER_ERROR, request);
  }
}
