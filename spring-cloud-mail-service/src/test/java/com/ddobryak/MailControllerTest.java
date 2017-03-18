package com.ddobryak;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.ddobryak.controller.MailController;
import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;
import com.ddobryak.service.MailQueueService;
import com.google.gson.Gson;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MailServiceApplication.class, webEnvironment = RANDOM_PORT, properties = { "wait.await=false" })
public class MailControllerTest {

	@InjectMocks
	@Autowired
	private MailController controller;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Mock
	private MailQueueService notificationService;

	private MockMvc mockMvc;

	@Before
	public void before() {
		MockitoAnnotations.initMocks(this);

		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	public void addMail() throws Exception {
		MailDTO mailDTO = new MailDTO();
		mailDTO.setTo("to@to.ru");
		mailDTO.setSubject("subject");
		mailDTO.setMsg("msg");
		mailDTO.setAttempts(1);
		
		Mail mail = new Mail();
		mail.setTo("to@to.ru");
		mail.setSubject("subject");
		mail.setMsg("msg");
		mail.setAttempts(1);
		mail.setId("123456");
		
		Gson gson = new Gson();
		String json = gson.toJson(mailDTO);
		
		Mockito.when(notificationService.add(mailDTO)).thenReturn(mail);

		mockMvc.perform(post("/api/v1/mail/send")
				.content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	
		Mockito.verify(notificationService, Mockito.only()).add(mailDTO);
	}
	
	@Test
	public void invalidMail() throws Exception {
		Mail mail = new Mail();
		mail.setTo("to");
		mail.setSubject("subject");
		
		Gson gson = new Gson();
		String json = gson.toJson(mail);

		mockMvc.perform(post("/api/v1/mail/send")
				.content(json).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
}