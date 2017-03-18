package com.ddobryak.model;

public enum MailStatus {
	SENDING("SENDING"), SUCCESS("SUCCESS"), FAILED("FAILED");
	
	private String text;

	MailStatus(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
