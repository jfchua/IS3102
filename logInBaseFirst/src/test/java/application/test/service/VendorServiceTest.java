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
import application.exception.InvalidEmailException;
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
		Assert.assertFalse(result);
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
	public void testCreateVendor() throws ClientOrganisationNotFoundException, InvalidEmailException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result =  vendorService.createVendor(org, "vendor@vendor.com", "vendorname", "vendordescription", "95959595");
		Assert.assertTrue(result);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testCreateVendorInvalidEmail() throws ClientOrganisationNotFoundException, InvalidEmailException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result =  vendorService.createVendor(org, "invalidemail", "vendorname", "vendordescription", "95959595");
		Assert.assertTrue(result);
	}
		
	@Test
	public void testEditVendor() throws ClientOrganisationNotFoundException, InvalidEmailException, VendorNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = vendorService.editVendor((long)2, "email@email.com", "newname", "newdesc", "12345678");	
		Assert.assertTrue(result);
	}
	
	@Test(expected=InvalidEmailException.class)
	public void testEditVendorInvalidEmail() throws ClientOrganisationNotFoundException, InvalidEmailException, VendorNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = vendorService.editVendor((long)2, "invalidemail", "newname", "newdesc", "12345678");	
		Assert.assertTrue(result);
	}

	

}
