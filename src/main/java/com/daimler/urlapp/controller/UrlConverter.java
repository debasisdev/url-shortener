package com.daimler.urlapp.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import com.daimler.urlapp.GLOBALS;
import com.daimler.urlapp.exception.BusinessLogicException;
import com.daimler.urlapp.model.Url;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@Service
public class UrlConverter implements IConverter {

    @Override
    public long getDatabaseId(final String shortUrl) {
        String hash = getHash(shortUrl);
        if (hash != null) {
            return shortURLtoId(hash);
        } else {
            return Long.MIN_VALUE;
        }
    }

    @Override
    public Url shortenUrl(final String url, final long databaseId) {
        Url shortUrl = new Url();
        shortUrl.setPath(computeDomain(url) + GLOBALS.STANDARD_URL_SEPARATOR + idToShortURL(databaseId));
        return shortUrl;
    }

    @Override
    public Url shortenUrl(final String url, final String userHash) {
        Url shortUrl = new Url();
        shortUrl.setPath(computeDomain(url) + GLOBALS.STANDARD_URL_SEPARATOR + userHash);
        return shortUrl;
    }

    public String computeDomain(final String url) {
        try {
            URI uri = new URI(url);
            String[] domain = uri.getHost().split(GLOBALS.DOT_ESCAPED);
            StringBuffer dnsName = new StringBuffer(domain.length > 2 ? domain[1] : domain[0]);
            if (dnsName.length() < 3) {
                dnsName.append(GLOBALS.GENERIC_ZOP_DOMAIN);
            }
            return uri.getScheme() + GLOBALS.PROTOCOL_TO_DOMAIN_SEPARATOR
                    + new StringBuffer(dnsName).insert(dnsName.length() - 2, GLOBALS.DOMAIN_NAME_SEPARATOR).toString();
        } catch (URISyntaxException uriSyntaxException) {
            throw new BusinessLogicException("Domain from the URL coudln't be computed", uriSyntaxException);
        }
    }

    private String idToShortURL(@NotNull final long databaseId) {
        final char[] alphanumerics = GLOBALS.ALPHANUMERIC_LITERALS.toCharArray();

        final int base = alphanumerics.length;

        if (databaseId < 0 || databaseId == 0) {
            throw new BusinessLogicException("Database Id must be positive: " + databaseId);
        }

        StringBuilder buf = new StringBuilder();
        long base62Index = databaseId;
        while (base62Index != 0) {
            buf.append(alphanumerics[(int) (base62Index % base)]);
            base62Index /= base;
        }
        return buf.reverse().toString();
    }

    private long shortURLtoId(final String shortUrl) {
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

    public boolean isUrl(final String url) {
        String[] customSchemes = GLOBALS.ALLOWED_PROTOCOLS_FOR_URL;
        UrlValidator urlValidator = new UrlValidator(customSchemes);
        return urlValidator.isValid(url);
    }

    public String getHash(final String shortUrl) {
        Pattern pattern = Pattern.compile(GLOBALS.SHORT_URL_REGEX);
        Matcher localMatcher = pattern.matcher(shortUrl);

        if (localMatcher.find()) {
            return localMatcher.group(1);
        } else {
            return null;
        }
    }

}
