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
import application.service.user.EmailService;
import application.service.user.EventManagerService;
import application.service.user.IconService;
import application.service.user.MessageService;
import application.service.user.ToDoTaskService;
import application.service.user.UserService;
import application.test.AbstractTest;

@Transactional
public class IconServiceTest extends AbstractTest {
	@Autowired
	private IconService iconService;
	@Autowired
	private UserService userService;

	@Before
	public void setUp(){

	}

	@After
	public void tearDown(){

	}

	
}
