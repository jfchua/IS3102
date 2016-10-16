package application.test.service;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.Event;
import application.service.user.EventManagerService;
import application.service.user.UserService;
import application.test.AbstractTest;

@Transactional
public class EventManagerServiceTest extends AbstractTest {
	@Autowired
	private EventManagerService eventManagerService;
	@Autowired
	private UserService userService;

	@Before
	public void setUp(){

	}

	@After
	public void tearDown(){

	}
	
	@Test
	public void testViewSingleEvent(){
		eventManagerService.viewSingleEvent();
		
	}
	@Test
	public void testGetAllEvents(){
		//eventManagerServic
		
	}
	@Test
	public void testGetAllPendingEvents(){
		//eventManagerServ
		
	}
	@Test
	public void testGetAllApprovedEvents(){
		//eventManagerServic
		
	}

	
	@Test
	public void testGetAllUsers(){
		
	}

	
}
