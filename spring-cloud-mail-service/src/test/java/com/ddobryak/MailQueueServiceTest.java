package com.ddobryak;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.test.context.junit4.SpringRunner;

import com.ddobryak.service.MailSenderService;
import com.ddobryak.service.MailService;
import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;
import com.ddobryak.service.MailQueueService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailServiceApplication.class, webEnvironment = RANDOM_PORT, properties = { "wait.await=false" })
public class MailQueueServiceTest {
	
	@InjectMocks
	@Autowired
	private MailQueueService mailQueueService;
	
	@Mock
	private MailService mailService;
	
	@Mock
	private MailSenderService mailSenderService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void addMail() throws InterruptedException {
		MailDTO mailDTO = new MailDTO();
		mailDTO.setTo("to@to.ru");
		mailDTO.setSubject("subject");
		mailDTO.setMsg("msg");

		Mail mail = new Mail();
		mail.setTo("to@to.ru");
		mail.setSubject("subject");
		mail.setMsg("msg");
		
		Mockito.when(mailSenderService.send(mail)).thenReturn(new AsyncResult<>(true));
		Mockito.when(mailService.save(mail)).thenReturn(mail);
		Mockito.when(mailService.findSendingMails()).thenReturn(Collections.emptyList());

		mailQueueService.add(mailDTO);
		
		Thread.sleep(1000);
		
		Mockito.verify(mailSenderService, Mockito.only()).send(mail);
		Mockito.verify(mailService, Mockito.times(2)).save(mail);
	}
}
