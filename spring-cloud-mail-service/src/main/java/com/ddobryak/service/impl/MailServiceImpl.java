package com.ddobryak.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ddobryak.model.Mail;
import com.ddobryak.model.MailStatus;
import com.ddobryak.repo.MailRepository;
import com.ddobryak.service.MailService;

public class MailServiceImpl implements MailService {
	@Autowired
	private MailRepository mailRepo;

	@Override
	public Iterable<Mail> findSendingMails() {
		return mailRepo.findByStatus(MailStatus.SENDING);
	}

	@Override
	public Mail find(String id) {
		return mailRepo.findOne(id);
	}

	@Override
	public Mail save(Mail mail) {
		return mailRepo.save(mail);
	}
}
