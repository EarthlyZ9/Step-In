package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends CustomException {

    public InternalServerErrorException() {
        super(ErrorType.INTERNAL_SERVER_ERROR, null);
    }

    public InternalServerErrorException(String message) {
        super(ErrorType.INTERNAL_SERVER_ERROR, message);
    }
}
