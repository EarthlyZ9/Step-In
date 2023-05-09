package com.earthlyz9.stepin.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PermissionDeniedException extends CustomException {

    public PermissionDeniedException() {
        super(ErrorType.NO_PERMISSION, null);
    }

    public PermissionDeniedException(String message) {
        super(ErrorType.NO_PERMISSION, message);
    }
}
