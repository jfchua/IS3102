package application.test.service;

import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.entity.ToDoTask;
import application.entity.User;
import application.exception.ToDoTaskNotFoundException;
import application.exception.UserNotFoundException;
import application.service.ToDoTaskService;
import application.service.UserService;
import application.test.AbstractTest;
@Transactional
public class ToDoTaskServiceTest extends AbstractTest {


	@Autowired
	private ToDoTaskService toDoTaskService;
	@Autowired
	private UserService userService;

	@Before
	public void setUp() throws UserNotFoundException{
		User user = userService.getUserById((long)10).get();
		toDoTaskService.addToDoTask(user, "testtaskk", new Date());
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testGetToDoList() throws UserNotFoundException{
		User user = userService.getUserById((long)2).get();
		Collection<ToDoTask> result = toDoTaskService.getToDoList(user);
		Assert.assertNotNull("Getting todotask should not be null as already inserted via sql", result);		
	}

	@Test(expected=UserNotFoundException.class)
	public void testGetToDoListNullUser() throws UserNotFoundException{
		Collection<ToDoTask> result = toDoTaskService.getToDoList(null);
		Assert.assertNull("Getting todo task should be null for null user", result);		
	}
	@Test
	public void testAddToDoTask() throws UserNotFoundException{
		User user = userService.getUserById((long)11).get();
		boolean result = toDoTaskService.addToDoTask(user, "testtask", new Date());
		Assert.assertTrue("Getting todotask should work for an existing user", result);
		//Assert.assertEquals("Expected 1 task ", 1, t.size());
	}

	@Test(expected=UserNotFoundException.class)
	public void testAddToDoTaskNullUser() throws UserNotFoundException{
		boolean result = toDoTaskService.addToDoTask(null, "testtask", new Date());
		Assert.assertFalse("Getting todotask should not work for null user", result);
		//Assert.assertEquals("Expected 1 task ", 1, t.size());
	}

	@Test
	public void testDeleteToDoTask() throws UserNotFoundException, ToDoTaskNotFoundException{
		Collection<ToDoTask> t = toDoTaskService.getToDoList(userService.getUserById((long)10).get());
		boolean result = false;
		for ( ToDoTask task : t){
			result = toDoTaskService.deleteToDoTask(userService.getUserById((long)10).get(), task.getId());
			break;
		}
		Assert.assertTrue(result);
	}

	@Test(expected=UserNotFoundException.class)
	public void testDeleteToDoTaskNullUser() throws UserNotFoundException, ToDoTaskNotFoundException{
		boolean result = toDoTaskService.deleteToDoTask(null, 1);
		Assert.assertFalse(result);
	}

	@Test(expected=ToDoTaskNotFoundException.class)
	public void testDeleteToDoTaskTaskNotFound() throws UserNotFoundException, ToDoTaskNotFoundException{
		boolean result = toDoTaskService.deleteToDoTask(userService.getUserById((long)10).get(), Long.MAX_VALUE);
		Assert.assertFalse(result);
	}
}
