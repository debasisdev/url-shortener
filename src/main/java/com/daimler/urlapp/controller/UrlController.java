package com.daimler.urlapp.controller;

import java.util.List;

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

    @GetMapping("/urls")
    public List<Url> getUrls(Pageable pageable) {
        return urlStore.findAll(pageable).getContent();
    }

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

    @PostMapping("/urls/shorten/{userHash}")
    public ResponseEntity<?> shortenUrl(@PathVariable final String userHash, @Valid @RequestBody Url url) {
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

    @PostMapping("/urls/expand")
    public ResponseEntity<?> redirectUrl(@Valid @RequestBody final Url url) {
        long id = urlService.getDatabaseId(url.getPath());

        if (urlStore.findById(id).isPresent()) {
            return new ResponseEntity<Url>(urlStore.findById(id).get(), HttpStatus.TEMPORARY_REDIRECT);
        } else {
            String hash = urlService.getHash(url.getPath());
            Url longUrl = urlStore.fetchByCustomHash(hash);
            if (longUrl != null) {
                return new ResponseEntity<Url>(longUrl, HttpStatus.TEMPORARY_REDIRECT);
            } else {
                throw new ResourceNotFoundException("URL doesn't exist in database.");
            }
        }
    }

    @PutMapping("/urls/{urlId}")
    public ResponseEntity<?> updateQuestion(@PathVariable final Long urlId, @Valid @RequestBody Url url) {
        return urlStore.findById(urlId).map(urlHit -> {
            urlHit.setPath(url.getPath());
            urlStore.save(urlHit);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("URL not found with id " + urlId));
    }

    @DeleteMapping("/urls/{urlId}")
    public ResponseEntity<?> deleteUrl(@PathVariable final Long urlId) {
        return urlStore.findById(urlId).map(url -> {
            urlStore.delete(url);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("URL not found with id " + urlId));
    }

}
