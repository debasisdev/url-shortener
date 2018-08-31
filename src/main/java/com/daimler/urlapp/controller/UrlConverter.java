package com.daimler.urlapp.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import com.daimler.urlapp.model.Url;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@Service
public class UrlConverter implements IConverter {

    @Override
    public long getDatabaseId(String shortUrl) {
        String hash = getHash(shortUrl);
        if (hash != null) {
            return shortURLtoId(hash);
        } else {
            return Long.MIN_VALUE;
        }
    }

    @Override
    public Url shortenUrl(String url, long databaseId) {
        Url shortUrl = new Url();
        shortUrl.setPath(computeDomain(url) + "/" + idToShortURL(databaseId));
        return shortUrl;
    }

    @Override
    public Url shortenUrl(String url, String userHash) {
        Url shortUrl = new Url();
        shortUrl.setPath(computeDomain(url) + "/" + userHash);
        return shortUrl;
    }

    private String computeDomain(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            String domainWithoutTopLevelAttribute =
                    domain.startsWith("www.") ? domain.substring(4).split("\\.")[0] : domain.split("\\.")[0];
            return uri.getScheme() + "://" + new StringBuffer(domainWithoutTopLevelAttribute)
                    .insert(domainWithoutTopLevelAttribute.length() - 2, ".").toString();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private String idToShortURL(long databaseId) {
        final char[] ALPHANUMERICS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

        final int BASE = ALPHANUMERICS.length;

        if (databaseId < 0)
            throw new IllegalArgumentException("Number must be positive: " + databaseId);
        if (databaseId == 0)
            return "0";
        StringBuilder buf = new StringBuilder();
        while (databaseId != 0) {
            buf.append(ALPHANUMERICS[(int) (databaseId % BASE)]);
            databaseId /= BASE;
        }
        return buf.reverse().toString();
    }

    private long shortURLtoId(String shortUrl) {
        long id = 0;
        char[] shortUrlArray = shortUrl.toCharArray();
        for (int i = 0; i < shortUrl.length(); i++) {
            if ('a' <= shortUrlArray[i] && shortUrlArray[i] <= 'z')
                id = id * 62 + shortUrlArray[i] - 'a';
            if ('A' <= shortUrlArray[i] && shortUrlArray[i] <= 'Z')
                id = id * 62 + shortUrlArray[i] - 'A' + 26;
            if ('0' <= shortUrlArray[i] && shortUrlArray[i] <= '9')
                id = id * 62 + shortUrlArray[i] - '0' + 52;
        }
        return id;
    }

    public boolean isUrl(String url) {
        String[] customSchemes = { "http", "https" };
        UrlValidator urlValidator = new UrlValidator(customSchemes);
        return urlValidator.isValid(url);
    }

    public String getHash(String shortUrl) {
        Pattern pattern = Pattern.compile(".*/\\s*(.*)");
        Matcher localMatcher = pattern.matcher(shortUrl);

        if (localMatcher.find()) {
            return localMatcher.group(1);
        } else {
            return null;
        }
    }

}
