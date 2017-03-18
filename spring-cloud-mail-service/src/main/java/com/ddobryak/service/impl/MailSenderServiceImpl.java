package com.ddobryak.service.impl;

import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.ddobryak.model.Mail;
import com.ddobryak.service.MailSenderService;

public class MailSenderServiceImpl implements MailSenderService {

	private static final Logger log = Logger.getLogger(MailSenderServiceImpl.class.getName());

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private SimpleMailMessage templateMessage;

	/**
	 * send mail async
	 * @param mail - mail to send
	 */
	@Async
	@Override
	public Future<Boolean> send(Mail mail) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage(templateMessage);
			mailMessage.setSubject(mail.getSubject());
			mailMessage.setTo(mail.getTo());
			mailMessage.setText(mail.getMsg());

			javaMailSender.send(mailMessage);

			return new AsyncResult<>(true);
		} catch (MailException ex) {
			log.warning(String.format("error during send email: %s, exception: %s", mail, ex));
			return new AsyncResult<>(false);
		}
	}

}
