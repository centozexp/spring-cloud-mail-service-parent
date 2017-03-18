package com.ddobryak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableMailService
public class MailServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(MailServiceApplication.class, args);
	}

}