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
	public void testGetAllClientOrganisation() {
		Collection<ClientOrganisation> result = clientOrganisationService.getAllClientOrganisations();
		//Assert.assertFalse(entities.isEmpty());
		Assert.assertNotNull("Getting client orgs should not be null as already inserted via sql", result);
		//Assert.assertEquals("expected no. of client orgs is 2", 2, entities.size());
	
	}
	
	@Test
	public void testGetClientOrganisationByName() {
		ClientOrganisation result = clientOrganisationService.getClientOrganisationByName("Expo");
		Assert.assertNotNull("Getting org should not be null as Expo already inserted via sql", result);
		//Assert.assertEquals("expected name is Expo", "Expo", org.getOrganisationName());	
	}
	
	@Test
	public void testGetClientOrganisationByNameNotFound() {
		ClientOrganisation result = clientOrganisationService.getClientOrganisationByName("NON-EXISTENT");
		Assert.assertNull("Getting org should be null", result);
	}
	
	@Test
	public void testCreateNewClientOrganisation(){
		List<String> subs = new ArrayList<String>();
		System.err.println("before createnewclientorg");
		clientOrganisationService.createNewClientOrganisation("testname123", "testemail@test", subs, "testadminname");
		System.err.println("after  createnewclientorg");
		ClientOrganisation result = clientOrganisationService.getClientOrganisationByName("testname123");
		System.err.println("getting client org");
		System.err.println("createnewclientorgtest" + result.getOrganisationName() );
		Assert.assertNotNull("Expected not null", result);
		Assert.assertEquals("Expected organisation name match", "testname123",
				result.getOrganisationName());
		System.err.println("Created new client organisation of name: "  + result.getOrganisationName());

		//Collection<ClientOrganisation> entities = clientOrganisationService.getAllClientOrganisations();

		//Assert.assertEquals("Expected size to be 3 after creation", 3, entities.size());
		
	}
	
	@Test
	public void testDeleteClientOrg() {
		ClientOrganisation result = clientOrganisationService.getClientOrganisationByName("deletetestname123");
		Assert.assertNotNull("Getting org should not be null", result);
		clientOrganisationService.deleteClientOrg(result.getId());
		result = clientOrganisationService.getClientOrganisationByName("testname123");
		Assert.assertNull("Getting org should now be null after deletion", result);
		System.err.println("Size of client orgs is now : " + clientOrganisationService.getAllClientOrganisations().size() );
	}
	

}
