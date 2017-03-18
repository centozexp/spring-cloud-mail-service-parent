package com.ddobryak.service;

import com.ddobryak.model.Mail;

public interface MailService {
	Iterable<Mail> findSendingMails();
	Mail find(String id);
	Mail save(Mail mail);
}
