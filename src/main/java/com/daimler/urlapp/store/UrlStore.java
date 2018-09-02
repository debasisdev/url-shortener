package com.daimler.urlapp.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daimler.urlapp.model.Url;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@Repository
public interface UrlStore extends JpaRepository<Url, Long> {
  
    Url fetchByCustomHash(@Param("hash") String hash);
}
