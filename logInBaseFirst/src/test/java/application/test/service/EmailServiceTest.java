package application.test.service;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.exception.InvalidAttachmentException;
import application.service.EmailService;
import application.test.AbstractTest;

@Transactional
public class EmailServiceTest extends AbstractTest {
	@Autowired
	private EmailService emailService;
	@Autowired
	private ServletContext servletContext;

	@Before
	public void setUp(){

	}

	@After
	public void tearDown(){

	}
	@Test
	public void testSendEmail(){
		 emailService.sendEmail("test@test.com", "test", "test");
		//Assert.assertTrue(result);
	}
	 
	@Test
	public void testSendEmailWithAttachment() throws InvalidAttachmentException{
		emailService.sendEmailWithAttachment("test@test.com", "test", "test", "algattasLogo.png");
		//Assert.assertTrue(result);
	}
	
	@Test
	public void testCheckAttachment() throws InvalidAttachmentException{
		String path = servletContext.getRealPath("/") + "\\algattasLogo.png";
		//System.err.println("filepath is " + path);
		boolean result = emailService.checkAttachments(path);
		Assert.assertTrue(result);
	}
	
	@Test(expected=InvalidAttachmentException.class)
	public void testCheckInvalidAttachment() throws InvalidAttachmentException{
		boolean result =  emailService.checkAttachments("invalidattachment");
		//Assert.assertTrue(result);
	}
	
	
}
