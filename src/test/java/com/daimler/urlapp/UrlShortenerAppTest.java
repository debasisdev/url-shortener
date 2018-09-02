package com.daimler.urlapp;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.daimler.urlapp.controller.UrlService;
import com.daimler.urlapp.model.Url;
import com.daimler.urlapp.store.UrlStore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test placeholder to test the Web-Layer.
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UrlShortenerAppTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UrlStore urlStore;
	
	@MockBean
	private UrlService urlService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	private static Url sampleUrl;
	
	@Before
	public void init() {
		mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
		
		sampleUrl = new Url();
		sampleUrl.setPath("https://www.daimler.com/");
	}
	
	@Test
	public void shortenRouteWithoutHash() throws Exception {
		when(urlService.isUrl(Mockito.anyString())).thenReturn(true);
		when(urlStore.save(Mockito.any(Url.class))).thenReturn(sampleUrl);
		when(urlService.shortenUrl(Mockito.any(Url.class))).thenReturn(sampleUrl);
		this.mockMvc
		        .perform(post("/urls/shorten").content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().isOk());
		
		when(urlService.shortenUrl(Mockito.any(Url.class))).thenReturn(null);
		this.mockMvc
		        .perform(post("/urls/shorten").content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().is5xxServerError());
		
		when(urlService.isUrl(Mockito.anyString())).thenReturn(false);
		this.mockMvc
		        .perform(post("/urls/shorten").content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void shortenRouteWithHash() throws Exception {
		sampleUrl.setCustomHash("cars");
		
		when(urlService.isUrl(Mockito.anyString())).thenReturn(true);
		when(urlStore.save(Mockito.any(Url.class))).thenReturn(sampleUrl);
		when(urlService.shortenUrl(Mockito.any(Url.class), Mockito.anyString())).thenReturn(sampleUrl);
		this.mockMvc.perform(
		        post("/urls/shorten/" + sampleUrl.getCustomHash()).content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().isOk());
		
		when(urlService.shortenUrl(Mockito.any(Url.class), Mockito.anyString())).thenReturn(null);
		this.mockMvc.perform(
		        post("/urls/shorten/" + sampleUrl.getCustomHash()).content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().is5xxServerError());
		
		when(urlService.isUrl(Mockito.anyString())).thenReturn(false);
		this.mockMvc.perform(
		        post("/urls/shorten/" + sampleUrl.getCustomHash()).content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().is4xxClientError());
	}
	
	@Test
	public void redirectRoute() throws Exception {
		
		when(urlService.getDatabaseId(Mockito.anyString())).thenReturn(Mockito.anyLong());
		when(urlStore.findById(sampleUrl.getId())).thenReturn(Optional.of(sampleUrl));
		when(urlService.shortenUrl(Mockito.any(Url.class), Mockito.anyString())).thenReturn(Mockito.any(Url.class));
		this.mockMvc
		        .perform(post("/urls/expand").content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().is3xxRedirection());
		
		when(urlStore.findById(sampleUrl.getId())).thenReturn(Optional.empty());
		when(urlService.getHash(sampleUrl.getPath())).thenReturn(Mockito.anyString());
		this.mockMvc
		        .perform(post("/urls/expand").content(objectMapper.writeValueAsBytes(sampleUrl))
		                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
		        .andDo(print()).andExpect(status().is3xxRedirection());
	}
	
}
