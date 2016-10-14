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
import application.exception.MessageNotFoundException;
import application.exception.UserNotFoundException;
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
	public void setUp() throws UserNotFoundException{
		User user1 = userService.getUserById((long)2).get();
		User user2 = userService.getUserById((long)11).get();
		messageService.sendMessage(user1, user2, "testmsgggg", "testmsggg");
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testGetMessages() throws UserNotFoundException{
		User user = userService.getUserById((long)2).get();
		Collection<Message> t = messageService.getMessages(user);
		Assert.assertNotNull("Getting messages should not be null as already inserted via sql", t);		
	}

	@Test
	public void testSendMessage() throws UserNotFoundException{
		User user1 = userService.getUserById((long)2).get();
		User user2 = userService.getUserById((long)10).get();
		boolean result = messageService.sendMessage(user1, user2, "testmsgsubj", "testmsg");
		Assert.assertTrue("Message should have been sent properrlty",result);		
	}
	
	@Test(expected=UserNotFoundException.class)
	public void testSendMessageUserNotExists() throws UserNotFoundException{
		boolean result = messageService.sendMessage(null, null, "testmsgsubj", "testmsg");
		Assert.assertFalse("Message should not have been sent properrlty",result);	
	}

	@Test
	public void testDeleteMessage() throws MessageNotFoundException, UserNotFoundException{
		User user = userService.getUserById((long)11).get();
		Collection<Message> t = messageService.getMessages(user);
		System.err.println("msg size is " + t.size());
		boolean result = false;
		for ( Message m : t){
			result = messageService.deleteMessage(m);
			break;
		}
		Assert.assertTrue("Message should have been deleted",result);		
	}
	
	@Test(expected=MessageNotFoundException.class)
	public void testDeleteMessageMessageNotFound() throws MessageNotFoundException, UserNotFoundException{
		boolean result = messageService.deleteMessage(null);
		Assert.assertFalse("Null message should not have been deleted",result);;		
	}
}
