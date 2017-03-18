package com.ddobryak.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ui.velocity.VelocityEngineFactory;

import com.ddobryak.controller.MailController;
import com.ddobryak.service.MailService;
import com.ddobryak.service.MailQueueService;
import com.ddobryak.service.MailSenderService;
import com.ddobryak.service.impl.MailServiceImpl;
import com.ddobryak.utils.EntityConverter;
import com.ddobryak.utils.impl.EntityConverterImpl;
import com.ddobryak.service.impl.MailQueueServiceImpl;
import com.ddobryak.service.impl.MailSenderServiceImpl;

@Configuration
@EnableAsync
public class AppConfiguration {
	@Autowired
	private MailServiceProperties properties;
	
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(properties.getPool().getMin());
		taskExecutor.setMaxPoolSize(properties.getPool().getMax());
		taskExecutor.setQueueCapacity(properties.getPool().getCapacity());
		taskExecutor.setWaitForTasksToCompleteOnShutdown(properties.getWait().isAwait());
		if(properties.getWait().isAwait()) {
			taskExecutor.setAwaitTerminationSeconds(properties.getWait().getTimeout());
		}

		return taskExecutor;
	}

	@Bean
	public JavaMailSender getMailSender() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", properties.getMail().isAuth());
		props.put("mail.smtp.ssl.enable", properties.getMail().isSsl());
		props.put("mail.transport.protocol", properties.getMail().getProtocol());
		props.put("mail.smtp.allow8bitmime", properties.getMail().isAllow8bitmime());
		props.put("mail.smtps.allow8bitmime", properties.getMail().isAllow8bitmime());
		props.put("defaultEncoding", StandardCharsets.UTF_8.name());
		props.put("mail.mime.charset", StandardCharsets.UTF_8.name());

		JavaMailSenderImpl res = new JavaMailSenderImpl();
		res.setHost(properties.getMail().getHost());
		res.setPort(properties.getMail().getPort());
		res.setUsername(properties.getMail().getUsername());
		res.setPassword(properties.getMail().getPassword());
		res.setJavaMailProperties(props);

		return res;
	}

	@Bean
	public VelocityEngine getVelocityEngine() throws IOException {
		VelocityEngineFactory factory = new VelocityEngineFactory();
		Properties props = new Properties();
		props.put("resource.loader", "class");
		props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

		factory.setVelocityProperties(props);
		return factory.createVelocityEngine();
	}
	
	@Bean
	public SimpleMailMessage templateMessage() {
		SimpleMailMessage res = new SimpleMailMessage();
		res.setFrom(properties.getMail().getUsername());
		
		return res;
	}

	@Bean
	public MailController mailController() {
		return new MailController();
	}
	
	@Bean
	public MailService mailService() {
		return new MailServiceImpl();
	}
	
	@Bean
	public MailSenderService mailSenderService() {
		return new MailSenderServiceImpl();
	}
	
	@Bean
	public MailQueueService mailQueueService() {
		return new MailQueueServiceImpl();
	}
	
	@Bean
	public EntityConverter entityConverter() {
		return new EntityConverterImpl();
	}
}