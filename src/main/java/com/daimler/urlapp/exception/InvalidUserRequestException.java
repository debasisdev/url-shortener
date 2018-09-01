package com.daimler.urlapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserRequestException extends RuntimeException {

    private static final long serialVersionUID = 8863121217983453538L;

    public InvalidUserRequestException(String message) {
        super(message);
    }

    public InvalidUserRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
