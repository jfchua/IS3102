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

import application.entity.ClientOrganisation;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OrganisationNameAlreadyExistsException;
import application.exception.UserNotFoundException;
import application.service.ClientOrganisationService;
import application.test.AbstractTest;

@Transactional
public class ClientOrganisationServiceTest extends AbstractTest {
	
	@Autowired
	private ClientOrganisationService clientOrganisationService;
	
	@Before
	public void setUp(){
		List<String> subs = new ArrayList<String>();
		subs.add("TESTSUB");
		try{
			clientOrganisationService.createNewClientOrganisation("deletetestname123", "testemail@testemail.edu", subs, "testadminname");
		}
		catch (Exception e){
			System.out.println(e.getMessage());
		}
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
	public void testGetClientOrganisationByName() throws ClientOrganisationNotFoundException {
		ClientOrganisation result = clientOrganisationService.getClientOrganisationByName("Expo");
		Assert.assertNotNull("Getting org should not be null as Expo already inserted via sql", result);
		//Assert.assertEquals("expected name is Expo", "Expo", org.getOrganisationName());	
	}
	
	@Test(expected=ClientOrganisationNotFoundException.class)
	public void testGetClientOrganisationByNameNotFound() throws ClientOrganisationNotFoundException {
		ClientOrganisation result = clientOrganisationService.getClientOrganisationByName("NON-EXISTENT");
		Assert.assertNull("Getting org should be null", result);
	}
	
	@Test
	public void testCreateNewClientOrganisation() throws EmailAlreadyExistsException,OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException {
		List<String> subs = new ArrayList<String>();
		System.err.println("before createnewclientorg");
		boolean result = clientOrganisationService.createNewClientOrganisation("testname123", "testemail@test.org", subs, "testadminname");
		System.err.println("after  createnewclientorg");
		Assert.assertTrue(result);	
	}
	
	@Test(expected=EmailAlreadyExistsException.class)
	public void testCreateNewClientOrganisationUserAlreadyExists() throws EmailAlreadyExistsException,OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException {
		List<String> subs = new ArrayList<String>();
		System.err.println("before createnewclientorg");
		boolean result = clientOrganisationService.createNewClientOrganisation("testname123", "kenneth1399@hotmail.com", subs, "testadminname");
		System.err.println("after  createnewclientorg");
		Assert.assertFalse(result);	
	}
	
	@Test(expected=OrganisationNameAlreadyExistsException.class)
	public void testCreateNewClientOrganisationOrgNameAlreadyExists() throws EmailAlreadyExistsException,OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException {
		List<String> subs = new ArrayList<String>();
		System.err.println("before createnewclientorg");
		boolean result = clientOrganisationService.createNewClientOrganisation("Expo", "testy@localhost.com", subs, "testname");
		System.err.println("after  createnewclientorg");
		Assert.assertFalse(result);	
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateNewClientOrganisationInvalidEmail() throws EmailAlreadyExistsException,OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException {
		List<String> subs = new ArrayList<String>();
		System.err.println("before createnewclientorg");
		boolean result = clientOrganisationService.createNewClientOrganisation("Expo2", "invalidemail", subs, "testname");
		System.err.println("after  createnewclientorg");
		Assert.assertFalse(result);	
	}
	
	@Test
	public void testDeleteClientOrg() throws ClientOrganisationNotFoundException {
		boolean result = clientOrganisationService.deleteClientOrg((long)3);
		Assert.assertTrue("Getting org should now be null after deletion", result);
	}
	
	@Test(expected=ClientOrganisationNotFoundException.class)
	public void testDeleteClientOrgNotExist() throws ClientOrganisationNotFoundException {
		boolean result = clientOrganisationService.deleteClientOrg(Long.MAX_VALUE);
		Assert.assertFalse("client org should not be deleted as it does not exist", result);		
	}
	

}
