package application.test.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ClientOrganisation;
import application.domain.User;
import application.service.user.ClientOrganisationService;
import application.service.user.EmailService;
import application.test.AbstractTest;

@Transactional
public class ClientOrganisationServiceTest extends AbstractTest {
	
	@Autowired
	private ClientOrganisationService clientOrganisationService;
	
	@Before
	public void setUp(){
		List<String> subs = new ArrayList<String>();
		subs.add("TESTSUB");
		clientOrganisationService.createNewClientOrganisation("deletetestname123", "testemail@testemail", subs, "testadminname");
		
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void getAllClientOrganisationTest() {
		Collection<ClientOrganisation> entities = clientOrganisationService.getAllClientOrganisations();

		Assert.assertNotNull("Getting client orgs should not be null as already inserted via sql", entities);
		//Assert.assertEquals("expected no. of client orgs is 2", 2, entities.size());
	
	}
	
	@Test
	public void getClientOrganisationByNameTest() {
		ClientOrganisation org = clientOrganisationService.getClientOrganisationByName("Expo");
		Assert.assertNotNull("Getting org should not be null as Expo already inserted via sql", org);
		Assert.assertEquals("expected name is Expo", "Expo", org.getOrganisationName());	
	}
	
	@Test
	public void getClientOrganisationByNameNotFoundTest() {
		ClientOrganisation org = clientOrganisationService.getClientOrganisationByName("w8efh08egy08237408273");
		Assert.assertNull("Getting org should be null", org);
	}
	
	@Test
	public void createNewClientOrganisationTest(){
		List<String> subs = new ArrayList<String>();
		subs.add("TESTSUB");
		System.err.println("before createnewclientorg");
		clientOrganisationService.createNewClientOrganisation("testname123", "testemail@test", subs, "testadminname");
		System.err.println("after  createnewclientorg");
		ClientOrganisation createdEntity = clientOrganisationService.getClientOrganisationByName("testname123");
		System.err.println("getting client org");
		System.err.println("createnewclientorgtest" + createdEntity.getOrganisationName() );
		Assert.assertNotNull("Expected not null", createdEntity);
		Assert.assertNotNull("Expected id attribute not null",
				createdEntity.getId());
		Assert.assertEquals("Expected email match", "testname123",
				createdEntity.getOrganisationName());
		System.err.println("Created new client organisation of name: "  + createdEntity.getOrganisationName());

		//Collection<ClientOrganisation> entities = clientOrganisationService.getAllClientOrganisations();

		//Assert.assertEquals("Expected size to be 3 after creation", 3, entities.size());
		
	}
	
	@Test
	public void deleteClientOrgTest() {
		ClientOrganisation org = clientOrganisationService.getClientOrganisationByName("deletetestname123");
		Assert.assertNotNull("Getting org should not be null", org);
		clientOrganisationService.deleteClientOrg(org.getId());
		org = clientOrganisationService.getClientOrganisationByName("testname123");
		Assert.assertNull("Getting org should now be null after deletion", org);
		System.err.println("Size of client orgs is now : " + clientOrganisationService.getAllClientOrganisations().size() );
	}
	

}
