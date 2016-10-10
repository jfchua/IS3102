package application.test.service;

import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.Message;
import application.domain.ToDoTask;
import application.domain.User;
import application.service.user.MessageService;
import application.service.user.ToDoTaskService;
import application.service.user.UserService;
import application.test.AbstractTest;

@Transactional
public class MessageServiceTest extends AbstractTest {
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;

	@Before
	public void setUp(){
		User user1 = userService.getUserById((long)2).get();
		User user2 = userService.getUserById((long)11).get();
		messageService.sendMessage(user1, user2, "testmsgggg", "testmsggg");
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testGetMessages(){
		User user = userService.getUserById((long)2).get();
		Collection<Message> t = messageService.getMessages(user);
		Assert.assertNotNull("Getting messages should not be null as already inserted via sql", t);		
	}

	@Test
	public void testSendMessage(){
		User user1 = userService.getUserById((long)2).get();
		User user2 = userService.getUserById((long)10).get();
		messageService.sendMessage(user1, user2, "testmsg", "testmsg");
		Collection<Message> t = messageService.getMessages(user2);
		Assert.assertNotNull("Getting messages should not be null as already send via test case", t);		
	}

	@Test
	public void testDeleteMessage(){
		User user = userService.getUserById((long)11).get();
		Collection<Message> t = messageService.getMessages(user);
		System.err.println("msg size is " + t.size());
		for ( Message m : t){
			messageService.deleteMessage(m);
		}
		t = messageService.getMessages(user);
		Assert.assertEquals("Getting messages should not be null as already inserted via sql", 0,t.size());		
	}
}
