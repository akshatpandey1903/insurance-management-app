package com.aurionpro.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService
{
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void sendEmail(String to, String subject, String body) 
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		mailSender.send(message);
	}
	
	@Override
	public void sendHtmlEmail(String to, String subject, String htmlBody) {
	    try {
	        MimeMessage mimeMessage = mailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
	        helper.setTo(to);
	        helper.setSubject(subject);
	        helper.setText(htmlBody, true); // true enables HTML
	        mailSender.send(mimeMessage);
	    } catch (MessagingException e) {
	        throw new RuntimeException("Failed to send HTML email", e);
	    }
	}

}
