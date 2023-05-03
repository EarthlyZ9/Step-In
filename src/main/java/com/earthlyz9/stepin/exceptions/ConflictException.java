package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends CustomException {

    public ConflictException() {
        super(ErrorType.DUPLICATE_RESOURCE, null);
    }

    public ConflictException(String message) {
        super(ErrorType.DUPLICATE_RESOURCE, message);
    }
}
