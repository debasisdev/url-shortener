package com.daimler.urlapp.store;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daimler.urlapp.model.Url;

/**
 * JPA specific extension for Data persistence. This will offer
 * you a more sophisticated interface than the plain {@link EntityManager} .
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@Repository
public interface UrlStore extends JpaRepository<Url, Long> {
	
	List<Url> fetchByCustomHash(@Param("hash") String hash);
}
