package com.daimler.urlapp.controller;

import com.daimler.urlapp.model.AuditEntity;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
public interface IConverter<T extends AuditEntity> {

    T shortenUrl(T t);

    T shortenUrl(T t, String userHash);

    long getDatabaseId(String shortUrl);
}
