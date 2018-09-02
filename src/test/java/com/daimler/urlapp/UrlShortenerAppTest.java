package com.daimler.urlapp;


import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.daimler.urlapp.controller.UrlController;
import com.daimler.urlapp.controller.UrlService;
import com.daimler.urlapp.model.Url;
import com.daimler.urlapp.store.UrlStore;

/**
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
//@RunWith(SpringRunner.class)
//@WebMvcTest(UrlController.class)
//public class UrlShortenerAppTest {
//    
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UrlStore urlStore;
//    
//    @MockBean
//    private UrlService urlService;
//
//    private static Url sampleUrl;
//
//    @BeforeClass
//    public static void init() {
//        sampleUrl = new Url();
//        sampleUrl.setPath("https://www.daimler.com/");
//    }

//}
