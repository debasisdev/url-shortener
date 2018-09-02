package com.daimler.urlapp.controller;

import com.daimler.urlapp.model.AuditEntity;

/**
 * Service provider contract for the application, which performs 3 basic
 * operations:
 * <ol>
 * <li>URL Shortening with auto-generated Hash</li>
 * <li>URL Shortening with user-define Hash</li>
 * <li>URL Redirection/Expansion</li>
 * </ol>
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
public interface IService<T extends AuditEntity> {
	
	/**
	 * Placeholder for implementing the shortening of URL with auto-generated
	 * Hash
	 * 
	 * @param t
	 *            Entity that contains URL
	 * @return shortened URL
	 */
	T shortenUrl(T t);
	
	/**
	 * Placeholder for implementing the shortening of URL with user-defined Hash
	 * 
	 * @param t
	 *            Entity that contains URL
	 * @param userHash
	 *            User defined Hash
	 * @return shortened URL
	 */
	T shortenUrl(T t, String userHash);
	
	/**
	 * Placeholder for implementing the expansion/redirection of URL
	 * 
	 * @param shortUrl
	 *            Shortened URL
	 * @return id of {@link AuditEntity}
	 */
	long getDatabaseId(String shortUrl);
}
