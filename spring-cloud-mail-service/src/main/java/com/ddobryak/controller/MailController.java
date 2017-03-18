package com.ddobryak.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;
import com.ddobryak.model.MailStatus;
import com.ddobryak.service.MailQueueService;
import com.ddobryak.service.MailService;

@RestController
@RequestMapping("/api/v1/mail")
public class MailController {
	
	private static final Logger log = Logger.getLogger(MailController.class.getName());

	@Autowired
	private MailQueueService notificationService;
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public String send(@RequestBody @Valid MailDTO mail) {
		Mail res = notificationService.add(mail);
		return res.getId();
	}

	@RequestMapping(value = "/status/{id}", method = RequestMethod.GET)
	public ResponseEntity<MailStatus> status(@PathVariable("id") String id) {
		Mail res = mailService.find(id);
		
		if (res != null) {
			return ResponseEntity.ok(res.getStatus());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public void handleException(HttpServletRequest request, MethodArgumentNotValidException ex){
		log.log(Level.WARNING, "Returning HTTP 400 Bad Request", ex);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public void handleException(HttpServletRequest request, Exception ex){
		log.log(Level.WARNING, "Returning HTTP 500 Internal Server Error", ex);
	}
}
