package application.service;

import application.exception.InvalidAttachmentException;

public interface EmailService {

	 public void sendEmail(String recipient, String subject, String body);
	 public boolean checkAttachments(String... attachments) throws InvalidAttachmentException;
	 public void sendEmailWithAttachment(String recipient, String subject, String body, String... attachment) throws InvalidAttachmentException;
}
