package com.daimler.urlapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * It's the entrypoint for the application where auto-configuration, component
 * scan, and extra configuration are configured.
 * 
 * @author Debasis Kar <debasis.babun@gmail.com>
 *
 */
@SpringBootApplication
@EnableJpaAuditing
public class UrlShortenerApp {
	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApp.class, args);
	}
}
