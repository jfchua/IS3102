package application.test.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ClientOrganisation;
import application.domain.PasswordResetToken;
import application.domain.Role;
import application.domain.User;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OldPasswordInvalidException;
import application.exception.PasswordResetTokenNotFoundException;
import application.exception.UserNotFoundException;
import application.service.user.ClientOrganisationService;
import application.service.user.UserService;
import application.test.AbstractTest;


@Transactional
public class UserServiceTest extends AbstractTest {

	@Autowired
	private UserService userService;

	@Autowired
	private ClientOrganisationService clientOrgService;

	@Before
	public void setUp() throws UserNotFoundException{
		User user = userService.getUserByEmail("property@localhost").get();
		userService.createPasswordResetTokenForUser(user, "tokentest12345");
		try{
			userService.createNewUser(clientOrgService.getClientOrganisationByName("Expo"), "testdeleteuser2", "testdeleteuser2@test.com", new HashSet<Role>());
		}
		catch ( Exception e){
			System.err.println("cannot create user" + e.getMessage());
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
	public void testViewAllUsersFromClientOrg() throws ClientOrganisationNotFoundException{
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("Expo");
		Collection<User> users = userService.viewAllUsers(tempOrg);
		Assert.assertNotNull("Getting users should not be null as users belong to expo", users);
	}

	@Test(expected=ClientOrganisationNotFoundException.class)
	public void testViewAllUsersFromNonExistClientOrg() throws ClientOrganisationNotFoundException{
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("NON-EXIST");
		Collection<User> users = userService.viewAllUsers(tempOrg);
		Assert.assertNull("Getting users should be null", users);
	}

	@Test
	public void testFindUserByIdFound() throws UserNotFoundException {

		Long id = new Long(1);

		Optional<User> entity = userService.getUserById(id);

		Assert.assertNotNull("Expected not null user", entity);
		//Assert.assertEquals("Expected id attribute to match", id,entity.get().getId());

	}

	@Test(expected=UserNotFoundException.class)
	public void testFindUserByIdNotFound() throws UserNotFoundException {

		Long id = Long.MAX_VALUE;

		Optional<User> entity = userService.getUserById(id);

		Assert.assertNull("Expected null user instead", entity);

	}

	@Test
	public void testFindUserByEmailFound() throws UserNotFoundException {

		String email = new String("property@localhost");

		Optional<User> result = userService.getUserByEmail(email);

		Assert.assertNotNull("Expected not user instead", result);

	}
	@Test(expected=UserNotFoundException.class)
	public void testFindUserByEmailNotFound() throws UserNotFoundException {
		String email = new String("non-existent@non-existent");
		Optional<User> result = userService.getUserByEmail(email);
		Assert.assertNull("Expected null user", result);
	}


	@Test
	public void testCreateUser() throws ClientOrganisationNotFoundException, EmailAlreadyExistsException, UserNotFoundException, InvalidEmailException {
		Set<Role> setOfRoles = new HashSet<Role>();
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("Suntec");
		boolean result = userService.createNewUser(tempOrg, "test", "test@test.com", setOfRoles);
		Assert.assertTrue(result);

	}

	@Test(expected=EmailAlreadyExistsException.class)
	public void testCreateUserAlreadyExists() throws ClientOrganisationNotFoundException, UserNotFoundException, EmailAlreadyExistsException, InvalidEmailException {
		Set<Role> setOfRoles = new HashSet<Role>();
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("Suntec");
		boolean result = userService.createNewUser(tempOrg, "test", "kenneth1399@hotmail.com", setOfRoles);
		Assert.assertFalse(result);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateUserInvalidEmail() throws ClientOrganisationNotFoundException, UserNotFoundException, EmailAlreadyExistsException, InvalidEmailException {
		Set<Role> setOfRoles = new HashSet<Role>();
		ClientOrganisation tempOrg = clientOrgService.getClientOrganisationByName("Suntec");
		boolean result = userService.createNewUser(tempOrg, "test", "invalidemail", setOfRoles);
		Assert.assertFalse(result);
	}

	@Test
	public void testGetPasswordResetTokenFound() throws PasswordResetTokenNotFoundException{
		PasswordResetToken prt = userService.getPasswordResetToken("tokentest12345");

		Assert.assertNotNull("Expected a prt instead", prt);
		//Assert.assertEquals("Expected tken attribute to match", "tokentest12345",prt.getToken());

	}
	@Test(expected=PasswordResetTokenNotFoundException.class)
	public void testGetPasswordResetTokenNotFound() throws PasswordResetTokenNotFoundException{
		PasswordResetToken prt = userService.getPasswordResetToken("non-existent");
		Assert.assertNull("Expected a null prt instead", prt);

	}
	@Test
	public void testCreatePasswordResetTokenForUser() throws UserNotFoundException, PasswordResetTokenNotFoundException{
		User user = userService.getUserByEmail("property@localhost").get();
		boolean result = userService.createPasswordResetTokenForUser(user, "tokentest123");
		Assert.assertTrue(result);
	}

	@Test(expected=UserNotFoundException.class)
	public void testCreatePasswordResetTokenForNullUser() throws UserNotFoundException{
		boolean result = userService.createPasswordResetTokenForUser(null, "tokentest321");
		Assert.assertFalse(result);
	}

	@Test
	public void testDeleteUser() throws UserNotFoundException{
		User userToDel = userService.getUserByEmail("testdeleteuser2@test.com").get();
		boolean result = userService.deleteUser(userToDel);
		Assert.assertTrue(result);
	}

	@Test(expected=UserNotFoundException.class)
	public void testDeleteNullUser() throws UserNotFoundException{
		boolean result = userService.deleteUser(null);
		Assert.assertFalse(result);
	}

	@Test
	public void testGetExternalUsers() throws ClientOrganisationNotFoundException{
		Set<User> s = userService.getExternalUsers(clientOrgService.getClientOrganisationByName("Expo"));	
		Assert.assertNotNull("Getting users should not be null", s);
	}
	@Test
	public void testGetFinanceManagers() throws ClientOrganisationNotFoundException{
		Set<User> s = userService.getFinanceManagers(clientOrgService.getClientOrganisationByName("Expo"));		
		Assert.assertNotNull("Getting users should not be null", s);
	}
	@Test
	public void testGetTicketManagers() throws ClientOrganisationNotFoundException{
		Set<User> s = userService.getTicketManagers(clientOrgService.getClientOrganisationByName("Expo"));		
		Assert.assertNotNull("Getting users should not be null", s);
	}

	@Test
	public void testEditUsers() throws UserNotFoundException{
		boolean result = userService.editUser("testname", userService.getUserByEmail("property@localhost").get(), new HashSet<Role>());
		Assert.assertTrue(result);
	}

	@Test(expected=UserNotFoundException.class)
	public void testEditNullUsers() throws UserNotFoundException{
		boolean result = userService.editUser("testname", null, new HashSet<Role>());
		Assert.assertFalse(result);
	}

	@Test
	public void testChangePassword() throws UserNotFoundException{
		User u = userService.getUserByEmail("property@localhost").get();
		String pas = "newpass";
		boolean result = userService.changePassword(u.getId(), pas);
		Assert.assertTrue(result);
	}

	@Test(expected=UserNotFoundException.class)
	public void testChangePasswordNullUser() throws UserNotFoundException{
		Long id = Long.MAX_VALUE;
		String pas = "newpass";
		boolean result = userService.changePassword(id, pas);
		Assert.assertFalse(result);
	}

	@Test
	public void testCheckOldPassword() throws UserNotFoundException, OldPasswordInvalidException{
		User u = userService.getUserByEmail("admin@localhost").get();
		boolean result = userService.checkOldPassword(u.getId(), "1");
		Assert.assertTrue(result);
	}

	@Test(expected=OldPasswordInvalidException.class)
	public void testCheckOldPasswordInvalid() throws UserNotFoundException, OldPasswordInvalidException{
		User u = userService.getUserByEmail("admin@localhost").get();
		boolean result = userService.checkOldPassword(u.getId(), "invalidpass");
		Assert.assertTrue(result);
	}


}