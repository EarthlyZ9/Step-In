package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends CustomException {

    public NotFoundException() {
        super(ErrorType.INSTANCE_NOT_FOUND, null);
    }

    public NotFoundException(String message) {
        super(ErrorType.INSTANCE_NOT_FOUND, message);
    }
}
