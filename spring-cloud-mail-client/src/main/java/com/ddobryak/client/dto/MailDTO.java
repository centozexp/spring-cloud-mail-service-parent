package com.ddobryak.client.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

public class MailDTO {

	@NotNull
	@Email
	private String to;

	@NotNull
	private String subject;

	@NotNull
	private String msg;
	
	@Min(1)
	private int attempts;
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		
		int result = 1;
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + attempts;
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		MailDTO other = (MailDTO) obj;
		
		if (msg == null) {
			if (other.msg != null) {
				return false;
			}
		} else if (!msg.equals(other.msg)) {
			return false;
		}
		
		if (subject == null) {
			if (other.subject != null) {
				return false;
			}
		} else if (!subject.equals(other.subject)) {
			return false;
		}
		
		if (to == null) {
			if (other.to != null) {
				return false;
			}
		} else if (!to.equals(other.to)) {
			return false;
		}
		
		if (attempts != other.attempts) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		return "MailDTO [to=" + to + ", subject=" + subject + ", attempts=" + attempts + "]";
	}
}