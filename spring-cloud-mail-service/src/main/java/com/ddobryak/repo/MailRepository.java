package com.ddobryak.repo;

import org.springframework.data.repository.CrudRepository;

import com.ddobryak.model.Mail;
import com.ddobryak.model.MailStatus;

public interface MailRepository extends CrudRepository<Mail, String> {
	public Iterable<Mail> findByStatus(MailStatus status);
}