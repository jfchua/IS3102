package application.test.service;

import java.util.Collection;
import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ToDoTask;
import application.domain.User;
import application.service.user.ToDoTaskService;
import application.service.user.UserService;
import application.test.AbstractTest;
@Transactional
public class ToDoTaskServiceTest extends AbstractTest {

	
	@Autowired
	private ToDoTaskService toDoTaskService;
	@Autowired
	private UserService userService;
	
	@Before
	public void setUp(){
		User user = userService.getUserById((long)10).get();
		toDoTaskService.addToDoTask(user, "testtaskk", new Date());
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testGetToDoList(){
		User user = userService.getUserById((long)2).get();
		Collection<ToDoTask> t = toDoTaskService.getToDoList(user);
		Assert.assertNotNull("Getting todotask should not be null as already inserted via sql", t);		
	}
	@Test
	public void testAddToDoTask(){
		User user = userService.getUserById((long)11).get();
		toDoTaskService.addToDoTask(user, "testtask", new Date());
		Collection<ToDoTask> t = toDoTaskService.getToDoList(user);
		Assert.assertNotNull("Getting todotask should not be null as already inserted", t);
		//Assert.assertEquals("Expected 1 task ", 1, t.size());
	}
	@Test
	public void testDeleteToDoTask(){
		Collection<ToDoTask> t = toDoTaskService.getToDoList(userService.getUserById((long)10).get());
		for ( ToDoTask task : t){
			toDoTaskService.deleteToDoTask(userService.getUserById((long)10).get(), task.getId());
		}
		t = toDoTaskService.getToDoList(userService.getUserById((long)10).get());
		Assert.assertEquals("Expected 0 task ", 0, t.size());

	}
}
