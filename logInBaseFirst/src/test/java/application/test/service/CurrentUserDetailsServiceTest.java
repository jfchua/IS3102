package application.test.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import application.domain.CurrentUser;
import application.exception.UserNotFoundException;
import application.service.currentuser.CurrentUserDetailsService;
import application.test.AbstractTest;

@Transactional
public class CurrentUserDetailsServiceTest extends AbstractTest {

	@Autowired
	private CurrentUserDetailsService currentUserDetailsService;

	@Before
	public void setUp(){
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testLoaduserByUsername(){
		CurrentUser currUser = currentUserDetailsService.loadUserByUsername("property@localhost");
		Assert.assertNotNull(currUser);
	}

	@Test(expected=UsernameNotFoundException.class)
	public void testLoaduserByUsernameNotFound(){
		CurrentUser currUser = currentUserDetailsService.loadUserByUsername("non-existent@non-existent");
		Assert.assertNull(currUser);
	}
}

