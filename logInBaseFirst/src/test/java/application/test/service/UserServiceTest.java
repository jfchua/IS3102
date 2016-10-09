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
import org.springframework.transaction.annotation.Transactional;

import application.domain.ClientOrganisation;
import application.domain.Role;
import application.domain.User;
import application.repository.ClientOrganisationRepository;
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

	}

	@After
	public void tearDown(){

	}

	@Test
	public void testFindAllUsers(){
		Collection<User> users = userService.getAllUsers();

		Assert.assertNotNull("Getting users should not be null as already inserted via sql", users);
		Assert.assertEquals("expected no. of users is 11", 11, users.size());

	}

	@Test(expected=NoSuchElementException.class)
	public void testFindUserByIdNotFound() {

		Long id = Long.MAX_VALUE;

		Optional<User> entity = userService.getUserById(id);

		Assert.assertNull("Expected null user instead", entity.get());

	}

	@Test
	public void testFindUserByIdFound() {

		Long id = new Long(1);

		Optional<User> entity = userService.getUserById(id);

		Assert.assertNotNull("Expected not null user", entity);
		Assert.assertEquals("Expected id attribute to match", id,entity.get().getId());

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

		String email = new String("weiotowinoidsgnoingr@21070fj208h4802h408h028ht082ht");

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
		Assert.assertNotNull("Expected id attribute not null",
				createdEntity.getId());
		Assert.assertEquals("Expected email match", "test@test.com",
				createdEntity.getEmail());
		Assert.assertEquals("Expected name match", "test",
				createdEntity.getName());

		Collection<User> list = userService.getAllUsers();

		Assert.assertEquals("Expected size to be 12 after creation", 12, list.size());

	}

}