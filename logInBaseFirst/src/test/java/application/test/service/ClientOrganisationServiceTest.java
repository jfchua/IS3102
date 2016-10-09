package application.test.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		
	}
	
	@After
	public void tearDown(){
		
	}
	
	@Test
	public void getAllClientOrganisationTest() {
		Collection<ClientOrganisation> entities = clientOrganisationService.getAllClientOrganisations();

		Assert.assertNotNull("Getting users should not be null as already inserted via sql", entities);
		Assert.assertEquals("expected no. of client orgs is 2", 2, entities.size());
	
	}
	
	@Test
	public void getClientOrganisationByNameTest() {
		ClientOrganisation org = clientOrganisationService.getClientOrganisationByName("Expo");
		Assert.assertNotNull("Getting org should not be null as Expo already inserted via sql", org);
		Assert.assertEquals("expected no. of client orgs is 2", "Expo", org.getOrganisationName());
	
	}
	
	@Test
	public void createNewClientOrganisationTestt(){
		List<String> subs = new ArrayList<String>();
		subs.add("TESTSUB");
		clientOrganisationService.createNewClientOrganisation("testname123", "testemail@testemail", subs, "testadminname");
		ClientOrganisation createdEntity = clientOrganisationService.getClientOrganisationByName("testname123");
		Assert.assertNotNull("Expected not null", createdEntity);
		Assert.assertNotNull("Expected id attribute not null",
				createdEntity.getId());
		Assert.assertEquals("Expected email match", "testname123",
				createdEntity.getOrganisationName());


		Collection<ClientOrganisation> entities = clientOrganisationService.getAllClientOrganisations();

		Assert.assertEquals("Expected size to be 3 after creation", 3, entities.size());
		
	}
	

}
