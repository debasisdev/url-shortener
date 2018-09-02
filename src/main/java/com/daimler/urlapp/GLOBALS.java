package com.daimler.urlapp;

/**
 * Holds the Constants used across the project.
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
public final class GLOBALS {
	
	public static final String STANDARD_URL_SEPARATOR = "/";
	
	public static final String DOMAIN_NAME_SEPARATOR = ".";
	
	public static final String DOT_ESCAPED = "\\.";
	
	public static final String PROTOCOL_TO_DOMAIN_SEPARATOR = "://";
	
	public static final String ALPHANUMERIC_LITERALS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static final String SHORT_URL_REGEX = ".*/\\s*(.*)";
	
	public static final String[] ALLOWED_PROTOCOLS_FOR_URL = { "http", "https" };
	
	public static final Object GENERIC_ZOP_DOMAIN = "io";
	
	private GLOBALS() {
		
	}
}
