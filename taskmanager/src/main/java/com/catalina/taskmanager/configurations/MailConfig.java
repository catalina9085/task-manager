package com.catalina.taskmanager.configurations;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
		mailSender.setHost("smtp.mail.yahoo.com");
		mailSender.setPort(465);
		mailSender.setUsername("catalina_ionela7@yahoo.com");
		mailSender.setPassword("tcttcnsygjnenhpp");//parola pentru aplicatie
		
		Properties props=mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.debug", "true");
		
		return mailSender;
	}
}
