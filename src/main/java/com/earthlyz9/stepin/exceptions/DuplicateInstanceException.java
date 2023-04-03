package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateInstanceException extends CustomException{
    public DuplicateInstanceException() {
        super(ErrorType.DUPLICATE_RESOURCE, null);
    }

    public DuplicateInstanceException(String message) {
        super(ErrorType.DUPLICATE_RESOURCE, message);
    }
}
