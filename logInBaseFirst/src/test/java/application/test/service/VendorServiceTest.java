package application.test.service;

import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ClientOrganisation;
import application.domain.Vendor;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.UserNotFoundException;
import application.exception.VendorNotFoundException;
import application.service.user.ClientOrganisationService;
import application.service.user.VendorService;
import application.test.AbstractTest;

@Transactional
public class VendorServiceTest extends AbstractTest {
	@Autowired
	private ClientOrganisationService orgService;
	@Autowired
	private VendorService vendorService;

	@Before
	public void setUp() throws UserNotFoundException{

	}

	@After
	public void tearDown(){

	}
	
	@Test
	public void testGetAllVendors() throws ClientOrganisationNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		Set<Vendor> result = vendorService.getAllVendors(org);
		Assert.assertNotNull(result);
	}
	
	
	@Test
	public void testDeleteVendor() throws ClientOrganisationNotFoundException, VendorNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		Boolean result = vendorService.deleteVendor(org, (long)1);
		Assert.assertTrue(result);
	}

	@Test(expected=VendorNotFoundException.class)
	public void testDeleteVendorNotFound() throws ClientOrganisationNotFoundException, VendorNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		Boolean result = vendorService.deleteVendor(org, Long.MAX_VALUE);
		Assert.assertTrue(result);
	}
	
	@Test
	public void testGetVendorById() throws VendorNotFoundException{
		Optional<Vendor> result = vendorService.getVendorById((long)1);
		Assert.assertNotNull(result);
	}
	
	@Test(expected=VendorNotFoundException.class)
	public void testGetVendorByIdNotFound() throws VendorNotFoundException{
		Optional<Vendor> result = vendorService.getVendorById(Long.MAX_VALUE);
		Assert.assertNull(result);
	}
	
	@Test
	public void testCreateVendor(){
		//	boolean createVendor(ClientOrganisation client, String email, String name, String description, String contact);

	}
		
	@Test
	public void testEditVendor(){
		//	boolean editVendor(long id, String email, String name, String description, String contact);	

	}

	

}
