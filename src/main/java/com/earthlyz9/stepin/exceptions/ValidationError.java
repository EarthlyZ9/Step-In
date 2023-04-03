package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationError extends CustomException {

  public ValidationError() {
    super(ErrorType.BAD_REQUEST, null);
  }

  public ValidationError(String message) {
    super(ErrorType.BAD_REQUEST, message);
  }
}
