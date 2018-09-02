package com.example.urlapp.controller.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daimler.urlapp.UrlShortenerApp;
import com.daimler.urlapp.controller.UrlConverter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrlShortenerApp.class)
public class UrlConverterTest {

    @Autowired
    private UrlConverter urlConverter;

    @Test
    public void contextLoads() {
        assertThat(urlConverter).isNotNull();
    }

    @Test
    public void givenUrl_a_checkValidity() {
        String[] sampleUrls = { "http://www.google.de/", "http://en.wikipedia.de/", "http://play.com/",
                "ftp://www.daimler.com/23cv5r", "http:www.google.com#344" };

        Boolean[] expectedValidity = { true, true, true, false, false };

        List<Boolean> validities =
                Arrays.asList(sampleUrls).stream().map(url -> urlConverter.isUrl(url)).collect(Collectors.toList());
        assertEquals(Arrays.asList(expectedValidity), validities);
    }

    @Test
    public void givenShortUrl_a_getHash() {
        String[] sampleUrls = { "http://goo.gl/jd6xcc", "http://en.wikipedia.de/35xcv4", "http://play/732bc2",
                "ftp://www.daimler.com/23cv5r", "http:www.google.com#344" };

        String[] hash = { "jd6xcc", "35xcv4", "732bc2", "23cv5r", null };
        List<String> hashList =
                Arrays.asList(sampleUrls).stream().map(url -> urlConverter.getHash(url)).collect(Collectors.toList());
        assertEquals(Arrays.asList(hash), hashList);
    }

    @Test
    public void givenShortUrl_a_getDomain() {
        String[] sampleUrls =
                { "http://www.google.de/jd6xcc", "https://en.wikipedia.org/wiki/Zoji_La", "http://stackoverflow.com/732bc2",
                        "ftp://we.chat/23cv5r", "http://www.spotify.co.uk/732bc2", "http://play/732bc2" };

        String[] expectedValidity = { "http://goog.le", "https://wikiped.ia", "http://stackoverfl.ow", "ftp://we.io",
                "http://spoti.fy", "http://pl.ay" };
        List<String> validities = Arrays.asList(sampleUrls).stream().map(url -> urlConverter.computeDomain(url))
                .collect(Collectors.toList());
        assertEquals(Arrays.asList(expectedValidity), validities);
    }

}
