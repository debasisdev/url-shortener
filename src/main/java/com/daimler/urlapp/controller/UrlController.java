package com.daimler.urlapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.daimler.urlapp.exception.BusinessLogicException;
import com.daimler.urlapp.exception.InvalidUserRequestException;
import com.daimler.urlapp.exception.ResourceNotFoundException;
import com.daimler.urlapp.model.Url;
import com.daimler.urlapp.store.UrlStore;

/**
 * Endpoints for URL Application.
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@RestController
public class UrlController {
	
	@Autowired
	private UrlStore urlStore;
	
	@Autowired
	private UrlService urlService;
	
	/**
	 * Retrieves all the persisted URLs.
	 * 
	 * @param pageable
	 *            pagination information
	 * @return list of all saved URLs
	 */
	@GetMapping("/urls")
	public List<Url> getUrls(Pageable pageable) {
		return urlStore.findAll(pageable).getContent();
	}
	
	/**
	 * Shortens a given URL with an auto-generated Hash.
	 * 
	 * @param url
	 *            Long URL
	 * @return see {@link HttpServletResponse}
	 */
	@PostMapping("/urls/shorten")
	public ResponseEntity<?> shortenUrl(@Valid @RequestBody final Url url) {
		if (urlService.isUrl(url.getPath())) {
			Url savedUrl = urlStore.save(url);
			Url shortUrl = urlService.shortenUrl(savedUrl);
			if (shortUrl != null) {
				return ResponseEntity.ok(shortUrl);
			} else {
				throw new BusinessLogicException("Short URL computed to be null.");
			}
		} else {
			throw new InvalidUserRequestException("URL is not Valid.");
		}
	}
	
	/**
	 * Shortens a given URL with an user-defined Hash.
	 * 
	 * @param userHash
	 *            custom hash provided by User
	 * @param url
	 *            Long URL
	 * @return see {@link HttpServletResponse}
	 */
	@PostMapping("/urls/shorten/{userHash}")
	public ResponseEntity<?> shortenUrl(@PathVariable final String userHash, @Valid @RequestBody Url url) {
		List<Url> existingUrl = urlStore.fetchByCustomHash(userHash);
		
		if (existingUrl != null && !existingUrl.isEmpty()) {
			if (existingUrl.stream().filter(eu -> eu.compareTo(url) == 0).count() > 0) {
				throw new BusinessLogicException("Hash not available anymore for the requested domain.");
			}
		}
		
		if (urlService.isUrl(url.getPath())) {
			url.setCustomHash(userHash);
			Url savedUrl = urlStore.save(url);
			Url shortUrl = urlService.shortenUrl(savedUrl, userHash);
			if (shortUrl != null) {
				return ResponseEntity.ok(shortUrl);
			} else {
				throw new BusinessLogicException("Short URL computed to be null.");
			}
		} else {
			throw new InvalidUserRequestException("URL is not Valid.");
		}
		
	}
	
	/**
	 * Redirects a given short URL to its real Long URL.
	 * 
	 * @param url
	 *            Short URL
	 * @return see {@link HttpServletResponse}
	 */
	@PostMapping("/urls/expand")
	public ResponseEntity<?> redirectUrl(@Valid @RequestBody final Url url) {
		long id = urlService.getDatabaseId(url.getPath());
		
		if (urlStore.findById(id).isPresent()) {
			return new ResponseEntity<Url>(urlStore.findById(id).get(), HttpStatus.TEMPORARY_REDIRECT);
		} else {
			String hash = urlService.getHash(url.getPath());
			List<Url> longUrl = urlStore.fetchByCustomHash(hash);
			if (longUrl != null && !longUrl.isEmpty()) {
				Url result = longUrl.stream().filter(lu -> url.getPath().contains(lu.computeDomain())).findAny()
				        .orElseThrow(() -> new ResourceNotFoundException("URL doesn't exist in database."));
				return new ResponseEntity<Url>(result, HttpStatus.TEMPORARY_REDIRECT);
			} else {
				throw new ResourceNotFoundException("URL doesn't exist in database.");
			}
		}
	}
	
	/**
	 * Updates an URL entry in the database.
	 * 
	 * @param urlId
	 *            database id of URL
	 * @param url
	 *            new URL (which need to be saved)
	 * @return see {@link HttpServletResponse}
	 */
	@PutMapping("/urls/{urlId}")
	public ResponseEntity<?> updateQuestion(@PathVariable final Long urlId, @Valid @RequestBody Url url) {
		return urlStore.findById(urlId).map(urlHit -> {
			urlHit.setPath(url.getPath());
			urlStore.save(urlHit);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("URL not found with id " + urlId));
	}
	
	/**
	 * Removes an URL entry from the database.
	 * 
	 * @param urlId
	 *            database id of URL
	 * @return see {@link HttpServletResponse}
	 */
	@DeleteMapping("/urls/{urlId}")
	public ResponseEntity<?> deleteUrl(@PathVariable final Long urlId) {
		return urlStore.findById(urlId).map(url -> {
			urlStore.delete(url);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("URL not found with id " + urlId));
	}
	
}
