package com.ddobryak.service;

import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;

@FunctionalInterface
public interface MailQueueService {
	Mail add(MailDTO mailDTO);
}
