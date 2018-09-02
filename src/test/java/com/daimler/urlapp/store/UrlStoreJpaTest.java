package com.daimler.urlapp.store;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.daimler.urlapp.model.Url;

/**
 * Test placeholder to test the JpaRepository query with a
 * {@link TestEntityManager}.
 * 
 * @author Debasis Kar <d.kar@reply.de>
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UrlStoreJpaTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Autowired
	private UrlStore urlStore;
	
	private static Url sampleUrl;
	
	@Before
	public void init() {
		sampleUrl = new Url();
		sampleUrl.setPath("https://www.daimler.com/");
		sampleUrl.setCustomHash("cars");
	}
	
	@Test
	public void testFindByCustomHash() {
		entityManager.persist(sampleUrl);
		Url url = urlStore.fetchByCustomHash("cars").get(0);
		assertEquals(sampleUrl.getPath(), url.getPath());
	}
	
}