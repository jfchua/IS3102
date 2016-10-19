package application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import application.exception.InvalidAttachmentException;

/*This Service sends an email using SOC email server.
 * Example Usage: Inject this class by having attribute private final EmailServiceImpl emailService.
 * 				: Include the emailService inside your constructor.
 * 				: emailService.sendEmail("recipient's address", "subject", "body");
 * 				: emailService.sendEmailWithAttachments("recipient's address", "subject", "body", attachments);
 */

@Service
public class EmailServiceImpl implements EmailService {

	private JavaMailSender javaMailSender;

	@Autowired
	public EmailServiceImpl(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}

	public boolean checkAttachments(String... attachments) throws InvalidAttachmentException{
		for ( String currentAttachment : attachments){
			if ( !currentAttachment.contains(".") || !currentAttachment.contains("/") ){
				throw new InvalidAttachmentException("Invalid attachment selected");

			}
		}
		return true;
	}

	//ATTACHMENT PATH CONTAINS THE FOLDER INSIDE RESOURCES. EG, images/IMAGE.png.
	//This method takes in a dynamic sized string array containing path names of files.
	//TODO : ERROR HANDLING PAGE
	@Async
	public void sendEmailWithAttachment(String recipient, String subject, String body, String... attachment) throws InvalidAttachmentException {

		try {
			MimeMessagePreparator preparator = getContentWtihAttachementMessagePreparator(recipient,subject,body,attachment);
			javaMailSender.send(preparator);
			System.out.println("Message With Attachement has been sent!");
			//return true;
		} catch (MailException ex) {
			throw ex;
		}
		 catch ( Exception ex ){
			 throw ex;
		 }

	}

	//PRIVATE HELPER METHOD FOR SENDEMAILWITHATTACHMENT METHOD
	private MimeMessagePreparator getContentWtihAttachementMessagePreparator(String recipient, String subject, String body, String... attachment) {

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

				helper.setSubject(subject);
				helper.setFrom("IFMS@Algattas.com");
				helper.setTo(recipient);
				helper.setText(body);
				// Add a resource as an attachment
				for ( String currentAttachment : attachment){
					String[] temp = currentAttachment.split("/");
					String currentAttachmentFilename = temp[1]; //Removes the path name, to get the file name
					helper.addAttachment(currentAttachmentFilename, new ClassPathResource(currentAttachment));
				}

			}
		};
		return preparator;
	}

	//@Async makes this method asynchronous so that its possible to access the website while mail is being sent
	//Sends basic email with text inside body.
	//TODO: Error handling in case email does not work.
	@Async
	public void sendEmail(String recipient, String subject, String body) {	

		// *** DO NOT EDIT ***
		try{
			System.out.println("Sending email...");      
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(recipient);
			mail.setFrom("IFMS@Algattas.com");
			mail.setSubject(subject);
			mail.setText(body);
			javaMailSender.send(mail);	
			System.out.println("Email Sent!");
		}
		catch ( MailException e){
			throw e;
		}
	}
	


	//TODO: HTML EMAIL
	/*private MimeMessagePreparator getContentAsInlineResourceMessagePreparator(String recipient, String subject, String body, String... attachment) {

	    MimeMessagePreparator preparator = new MimeMessagePreparator() {

	        public void prepare(MimeMessage mimeMessage) throws Exception {
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

	            helper.setSubject(subject);
	            helper.setFrom("IFMS@Algattas.com");
	            helper.setTo(recipient);
	            helper.setText("<html><body><p>" + body + "</p><img src='cid:company-logo' height="20" width="20"></body></html>", true);
	            helper.addInline("company-logo", new ClassPathResource(attachment));

	        }
	    };
	    return preparator;
	}*/



}
