package com.ddobryak.service;

import java.util.concurrent.Future;

import com.ddobryak.model.Mail;

@FunctionalInterface
public interface MailSenderService {
	Future<Boolean> send(Mail mail);
}
