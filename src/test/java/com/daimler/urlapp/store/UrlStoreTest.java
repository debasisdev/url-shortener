package com.daimler.urlapp.store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.daimler.urlapp.UrlShortenerApp;
import com.daimler.urlapp.model.Url;
import com.daimler.urlapp.store.UrlStore;

/**
 * Test placeholder to test {@link UrlStore} for Persistence.
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrlShortenerApp.class)
public class UrlStoreTest {
	
	@Autowired
	private UrlStore urlStore;
	
	private static Url sampleUrl;
	
	@BeforeClass
	public static void init() {
		sampleUrl = new Url();
		sampleUrl.setPath("https://www.daimler.com/");
	}
	
	@Test
	public void contextLoads() {
		assertThat(urlStore).isNotNull();
	}
	
	@Test
	public void givenUrlRepository_a_whenSaveAndRetreiveUrl_thenOK() {
		Url persistedUrl = urlStore.save(sampleUrl);
		Url foundUrl = urlStore.getOne(sampleUrl.getId());
		
		assertNotNull(foundUrl);
		assertEquals(persistedUrl.getPath(), foundUrl.getPath());
	}
	
	@Test
	public void givenUrlRepository_b_whenSaveAndUpdateUrl_thenOK() {
		urlStore.save(sampleUrl);
		
		Url foundUrl = urlStore.getOne(sampleUrl.getId());
		foundUrl.setPath("http://www.google.de/");
		urlStore.save(foundUrl);
		
		Url updatedUrl = urlStore.getOne(sampleUrl.getId());
		assertNotEquals(updatedUrl.getPath(), sampleUrl.getPath());
		assertEquals(updatedUrl.getPath(), foundUrl.getPath());
	}
	
	@Test
	public void givenUrlRepository_c_whenSaveAndDeleteUrl_thenOK() {
		urlStore.save(sampleUrl);
		urlStore.deleteById(sampleUrl.getId());
		assertEquals(urlStore.count(), 0);
	}
	
}
