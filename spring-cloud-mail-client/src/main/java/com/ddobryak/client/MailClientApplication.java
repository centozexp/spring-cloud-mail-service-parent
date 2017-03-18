package com.ddobryak.client;

import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.ddobryak.client.dto.MailDTO;

@SpringBootApplication
public class MailClientApplication {

	private static final Logger log = Logger.getLogger(MailClientApplication.class.getName());

	public static void main(String[] args) {
		new SpringApplicationBuilder(MailClientApplication.class).web(false).run(args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) {
		return args -> {
			
			if(args.length == 4) {
				String mailTo = args[0];
				String subject = args[1];
				String msg = args[2];
				int attempts = Integer.parseInt(args[3]);
				
				MailDTO mail = new MailDTO();
				mail.setTo(mailTo);
				mail.setSubject(subject);
				mail.setMsg(msg);
				mail.setAttempts(attempts);
				
				HttpEntity<MailDTO> request = new HttpEntity<>(mail);
				String res = restTemplate.postForObject("http://localhost:8081/api/v1/mail", request, String.class);

				log.info(res);
			}
		};
	}
	

}
