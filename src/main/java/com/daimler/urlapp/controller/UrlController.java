package com.daimler.urlapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    private UrlConverter urlConverter;

    @GetMapping("/urls")
    public List<Url> getUrls(Pageable pageable) {
        return urlStore.findAll(pageable).getContent();
    }

    @PostMapping("/urls/shorten")
    public ResponseEntity<?> shortenUrl(@Valid @RequestBody Url url) {
        if (urlConverter.isUrl(url.getPath())) {
            long id = urlStore.save(url).getId();
            return ResponseEntity.ok(urlConverter.shortenUrl(url.getPath(), id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PostMapping("/urls/shorten/")
    public ResponseEntity<?> shortenUrl(@Valid @RequestBody Url url) {
        if (urlConverter.isUrl(url.getPath())) {
            long id = urlStore.save(url).getId();
            return ResponseEntity.ok(urlConverter.shortenUrl(url.getPath(), id));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/urls/expand")
    public ResponseEntity<?> redirectUrl(@Valid @RequestBody Url url) {
        if (urlConverter.isUrl(url.getPath())) {
            long id = urlConverter.getDatabaseId(url.getPath());
            if (id > 0) {
                return ResponseEntity.ok(urlStore.findById(id).get());
            } else {
                throw new ResourceNotFoundException("URL doesn't exist in database.");
            }
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/urls/{urlId}")
    public ResponseEntity<?> updateQuestion(@PathVariable Long urlId, @Valid @RequestBody Url url) {
        return urlStore.findById(urlId).map(urlHit -> {
            urlHit.setPath(url.getPath());
            urlStore.save(urlHit);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("URL not found with id " + urlId));
    }

    @DeleteMapping("/urls/{urlId}")
    public ResponseEntity<?> deleteUrl(@PathVariable Long urlId) {
        return urlStore.findById(urlId).map(url -> {
            urlStore.delete(url);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("URL not found with id " + urlId));
    }

}
