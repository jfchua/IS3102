package application.test.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.service.user.EmailService;
import application.test.AbstractTest;

@Transactional
public class EmailServiceTest extends AbstractTest {

	@Autowired
	private EmailService emailService;
	
	@Before
	public void setUp(){
		
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void testSendEmail(){
		
	}
}
