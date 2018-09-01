package com.daimler.urlapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessLogicException extends RuntimeException {

    private static final long serialVersionUID = 8863121217983453538L;

    public BusinessLogicException(String message) {
        super(message);
    }

    public BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
