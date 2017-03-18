package com.ddobryak.utils.impl;

import com.ddobryak.dto.MailDTO;
import com.ddobryak.model.Mail;
import com.ddobryak.utils.EntityConverter;

public class EntityConverterImpl implements EntityConverter {

	@Override
	public Mail convert(MailDTO mail) {
		Mail res = new Mail();
		
		res.setTo(mail.getTo());
		res.setSubject(mail.getSubject());
		res.setMsg(mail.getMsg());
		res.setAttempts(mail.getAttempts());
		
		return res;
	}

}
