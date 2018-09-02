package com.daimler.urlapp.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daimler.urlapp.UrlShortenerApp;
import com.daimler.urlapp.controller.UrlService;
import com.daimler.urlapp.model.Url;

/**
 * 
 * @author Debasis Kar <d.kar@reply.de>
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrlShortenerApp.class)
public class UrlServiceTest {

    @Autowired
    private UrlService urlService;

    @Test
    public void contextLoads() {
        assertThat(urlService).isNotNull();
    }

    @Test
    public void givenUrl_a_checkValidity() {
        String[] sampleUrls = { "http://www.google.de/", "http://en.wikipedia.de/", "http://play.com/",
                "ftp://www.daimler.com/23cv5r", "http:www.google.com#344" };

        Boolean[] expectedValidity = { true, true, true, false, false };

        List<Boolean> validities =
                Arrays.asList(sampleUrls).stream().map(url -> urlService.isUrl(url)).collect(Collectors.toList());
        assertEquals(Arrays.asList(expectedValidity), validities);
    }

    @Test
    public void givenShortUrl_getHash() {
        String[] sampleUrls = { "http://goo.gl/jd6xcc", "http://en.wikipedia.de/35xcv4", "http://play/732bc2",
                "ftp://www.daimler.com/23cv5r", "http:www.google.com#344" };

        String[] hash = { "jd6xcc", "35xcv4", "732bc2", "23cv5r", null };
        List<String> hashList =
                Arrays.asList(sampleUrls).stream().map(url -> urlService.getHash(url)).collect(Collectors.toList());
        assertEquals(Arrays.asList(hash), hashList);
    }

    @Test
    public void givenUrl_getShortUrl() {
        String[] sampleUrls = { "http://www.google.de/jd6xcc", "https://en.wikipedia.org/wiki/Zoji_La",
                "http://stackoverflow.com/732bc2", "ftp://we.chat/23cv5r", "http://www.spotify.co.uk/732bc2",
                "http://play/732bc2" };

        List<Url> urlList = new ArrayList<>();
        Arrays.asList(sampleUrls).forEach(u -> {
            Url urlLocal = new Url();
            urlLocal.setId(1075743502L);
            urlLocal.setPath(u);
            urlList.add(urlLocal);
        });

        String[] shortUrls = { "http://goog.le/bkXSbO", "https://wikiped.ia/bkXSbO", "http://stackoverfl.ow/bkXSbO",
                "ftp://we.io/bkXSbO", "http://spoti.fy/bkXSbO", "http://pl.ay/bkXSbO" };

        List<String> shortUrlList =
                urlList.stream().map(url -> urlService.shortenUrl(url).getPath()).collect(Collectors.toList());
        assertNotNull(shortUrlList);
        assertEquals(shortUrlList.size(), sampleUrls.length);
        assertEquals(Arrays.asList(shortUrls), shortUrlList);
    }

    @Test
    public void givenUrlAndHash_getShortUrl() {
        String[] sampleUrls = { "http://www.google.de/jd6xcc", "https://en.wikipedia.org/wiki/Zoji_La",
                "http://stackoverflow.com/732bc2", "ftp://we.chat/23cv5r", "http://www.spotify.co.uk/732bc2",
                "http://play/732bc2" };

        List<Url> urlList = new ArrayList<>();
        Arrays.asList(sampleUrls).forEach(u -> {
            Url urlLocal = new Url();
            urlLocal.setPath(u);
            urlList.add(urlLocal);
        });

        String[] shortUrls = { "http://goog.le/career", "https://wikiped.ia/career", "http://stackoverfl.ow/career",
                "ftp://we.io/career", "http://spoti.fy/career", "http://pl.ay/career" };
        
        List<String> shortUrlList = urlList.stream().map(url -> urlService.shortenUrl(url, "career").getPath())
                .collect(Collectors.toList());
        assertNotNull(shortUrlList);
        assertEquals(shortUrlList.size(), sampleUrls.length);
        assertEquals(Arrays.asList(shortUrls), shortUrlList);
    }

    @Test
    public void givenShortUrl_getDatabaseId() {
        String[] shortUrls = { "http://goog.le/bkXSbO", "https://wikiped.ia/bkXSbO", "http://stackoverfl.ow/bkXSbO",
                "ftp://we.io/bkXSbO", "http://spoti.fy/bkXSbO", "http://pl.ay/bkXSbO" };
        Set<Long> shortUrlSet =
                Arrays.asList(shortUrls).stream().map(url -> urlService.getDatabaseId(url)).collect(Collectors.toSet());

        assertNotNull(shortUrlSet);
        assertEquals(shortUrlSet.size(), 1);
        assertEquals(shortUrlSet.toArray()[0], 1075743502L);
    }
}
