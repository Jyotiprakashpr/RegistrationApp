package com.ashokit.util;

import com.ashokit.exception.RegAppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(String subject, String body, String to) {
		boolean isMailSent = false;
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setText(body, true);
			mailSender.send(mimeMessageHelper.getMimeMessage());
			isMailSent = true;
		}
		/*
		 * } catch (MessagingException e) { e.printStackTrace(); }
		 * 
		 * return false;
		 */
		catch (Exception e) {
			throw new RegAppException(e.getMessage());
		}
		return isMailSent;	
		}
}
