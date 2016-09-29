package application.service.user;

public interface EmailService {

	 public void sendEmail(String recipient, String subject, String body);
	 public void sendEmailWithAttachment(String recipient, String subject, String body, String... attachment);
}
