package com.ddobryak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailServiceConfiguration {
	class Marker {}

	@Bean
	public Marker enableMailServerMarker() {
		return new Marker();
	}
}