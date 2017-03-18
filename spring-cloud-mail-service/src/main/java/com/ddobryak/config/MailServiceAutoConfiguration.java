package com.ddobryak.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ConditionalOnBean(MailServiceConfiguration.Marker.class)
@EnableConfigurationProperties(MailServiceProperties.class)
@EnableJpaRepositories(basePackages = {"com.ddobryak.repo"})
@EntityScan(basePackages = {"com.ddobryak.model"})
@Import(value = { AppConfiguration.class })
public class MailServiceAutoConfiguration { 
}