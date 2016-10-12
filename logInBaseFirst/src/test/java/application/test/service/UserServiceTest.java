package application.test.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ClientOrganisation;
import application.domain.PasswordResetToken;
import application.domain.Role;
import application.domain.User;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import application.service.user.ClientOrganisationService;
import application.service.user.UserService;
import application.test.AbstractTest;


@Transactional
public class UserServiceTest extends AbstractTest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ClientOrganisationService clientOrgService;
	@Autowired
	private RoleRepository roleRepository;

	@Before
	public void setUp(){
		User user = userService.getUserByEmail("property@localhost").get();
		userService.createPasswordResetTokenForUser(user, "tokentest12345");
		try{
			userService.createNewUser(clientOrgService.getClientOrganisationByName("Expo"), "testdeleteuser", "testdeleteuser@test.com", new HashSet<Role>());
		}
		catch ( Exception e){
			System.err.println("cannot create user");
		}
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testFindAllUsers(){
		Collection<User> users = userService.getAllUsers();

		Assert.assertNotNull("Getting users should not be null as already inserted via sql", users);
		//Assert.assertEquals("expected no. of users is 12", 12, users.size());

	}

	@Test
	public void testViewAllUsersFromClientOrg(){
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("Expo");
		Collection<User> users = userService.viewAllUsers(tempOrg);
		Assert.assertNotNull("Getting users should not be null as users belong to expo", users);
	}

	@Test
	public void testFindUserByIdFound() {

		Long id = new Long(1);

		Optional<User> entity = userService.getUserById(id);

		Assert.assertNotNull("Expected not null user", entity);
		//Assert.assertEquals("Expected id attribute to match", id,entity.get().getId());

	}
	
	@Test(expected=NoSuchElementException.class)
	public void testFindUserByIdNotFound() {

		Long id = Long.MAX_VALUE;

		Optional<User> entity = userService.getUserById(id);

		Assert.assertNull("Expected null user instead", entity.get());

	}
	
	@Test
	public void testFindUserByEmailFound() {

		String email = new String("property@localhost");

		Optional<User> entity = userService.getUserByEmail(email);

		Assert.assertNotNull("Expected not user instead", entity.get());
		Assert.assertEquals("Expected id attribute to match", email,entity.get().getEmail());

	}
	@Test(expected=NoSuchElementException.class)
	public void testFindUserByEmailNotFound() {
		String email = new String("non-existent@non-existent");
		Optional<User> entity = userService.getUserByEmail(email);
		Assert.assertNull("Expected null user", entity.get());
	}


	@Test
	public void testCreateUser() {
		Set<Role> setOfRoles = new HashSet<Role>();
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("Suntec");
		userService.createNewUser(tempOrg, "test", "test@test.com", setOfRoles);
		User createdEntity = userService.getUserByEmail("test@test.com").get();
		Assert.assertNotNull("Expected not null", createdEntity);
		Assert.assertEquals("Expected email match", "test@test.com",
				createdEntity.getEmail());
		Assert.assertEquals("Expected name match", "test",
				createdEntity.getName());
	}

	@Test
	public void testGetPasswordResetTokenFound(){
		PasswordResetToken prt = userService.getPasswordResetToken("tokentest12345");

		Assert.assertNotNull("Expected a prt instead", prt);
		//Assert.assertEquals("Expected tken attribute to match", "tokentest12345",prt.getToken());

	}
	@Test
	public void testGetPasswordResetTokenNotFound(){
		PasswordResetToken prt = userService.getPasswordResetToken("non-existent");
		Assert.assertNull("Expected a null prt instead", prt);

	}
	@Test
	public void testCreatePasswordResetTokenForUser(){
		User user = userService.getUserByEmail("property@localhost").get();
		userService.createPasswordResetTokenForUser(user, "tokentest123");

		PasswordResetToken prt = userService.getPasswordResetToken("tokentest123");
		Assert.assertNotNull("Expected not null", prt);
		Assert.assertEquals("Expected token match", "tokentest123",
				prt.getToken());
	}

	@Test(expected=NoSuchElementException.class)
	public void testDeleteUser(){
		User userToDel = userService.getUserByEmail("testdeleteuser@test.com").get();
		userService.deleteUser(userToDel);
		User user = userService.getUserByEmail("testdeleteuser@test.com").get();

		Assert.assertNull("Getting user should be null after deleting", user);
	}

	@Test
	public void testGetExternalUsers(){
		Set<User> s = userService.getExternalUsers(clientOrgService.getClientOrganisationByName("Expo"));	
		Assert.assertNotNull("Getting users should not be null", s);
	}
	@Test
	public void testGetFinanceManagers(){
		Set<User> s = userService.getFinanceManagers(clientOrgService.getClientOrganisationByName("Expo"));		
		Assert.assertNotNull("Getting users should not be null", s);
	}
	@Test
	public void testGetTicketManagers(){
		Set<User> s = userService.getTicketManagers(clientOrgService.getClientOrganisationByName("Expo"));		
		Assert.assertNotNull("Getting users should not be null", s);
	}

	@Test
	public void testEditUsers(){
		userService.editUser("testname", userService.getUserByEmail("property@localhost").get(), new HashSet<Role>());
		User u = userService.getUserByEmail("property@localhost").get();
		Assert.assertNotNull("Getting edited should not be null", u);
		Assert.assertEquals("Should be changed to new name", "testname",u.getName());
		Assert.assertEquals("Should be changed to new roles", new HashSet<Role>(), u.getRoles());
	}

	@Test
	public void testChangePassword(){
		User u = userService.getUserByEmail("property@localhost").get();
		String pas = "newpass";
		userService.changePassword(u.getId(), pas);
		BCryptPasswordEncoder t = new BCryptPasswordEncoder();
		String newPassword = t.encode(pas);
		Assert.assertNotNull("User getting password changed should not be null", u);
		Assert.assertTrue("New password should match the current changed password" , t.matches(pas, u.getPasswordHash()));


	}

}