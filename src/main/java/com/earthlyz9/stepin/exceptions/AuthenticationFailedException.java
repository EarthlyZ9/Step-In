package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationFailedException extends CustomException {

    public AuthenticationFailedException() {
        super(ErrorType.AUTHENTICATION_FAILED, null);
    }

    public AuthenticationFailedException(String message) {
        super(ErrorType.AUTHENTICATION_FAILED, message);
    }
}
