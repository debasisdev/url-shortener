package com.daimler.urlapp.controller;

import com.daimler.urlapp.model.Url;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
public interface IConverter {

    Url shortenUrl(String url, long databaseId);

    long getDatabaseId(String shortUrl);
}
