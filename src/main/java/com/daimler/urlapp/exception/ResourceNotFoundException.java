package com.daimler.urlapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Basic HTTP 404 Response throwing Exception {@link RuntimeException} used
 * throughout this project.
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 8863121217983453538L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}
	
	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
