package com.ddobryak.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ddobryak.EnableMailService;

@SpringBootApplication
@EnableMailService
public class MailSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailSampleApplication.class, args);
	}
}
