package com.aurionpro.app.service;

public interface EmailService 
{
	public void sendEmail(String to, String subject, String body);
	void sendHtmlEmail(String to, String subject, String htmlBody);
}
