package com.ddobryak.utils;

import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;

@FunctionalInterface
public interface EntityConverter {
	Mail convert(MailDTO mail);
}
