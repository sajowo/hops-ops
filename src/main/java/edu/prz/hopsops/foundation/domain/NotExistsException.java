package edu.prz.hopsops.foundation.domain;

import jakarta.validation.ValidationException;

public class NotExistsException extends ValidationException {

  protected NotExistsException(String message) {
    super(message);
  }

  public static NotExistsException of(String message) {
    return new NotExistsException(message);
  }
}
