package application.test.service;

import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.exception.BuildingNotFoundException;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.InvalidPostalCodeException;
import application.repository.UserRepository;
import application.service.user.BuildingService;
import application.service.user.ClientOrganisationService;
import application.test.AbstractTest;

@Transactional
public class BuildingServiceTest extends AbstractTest {

	@Autowired
	private BuildingService buildingService;
	@Autowired
	private ClientOrganisationService clientOrgService;

	@Before
	public void setUp(){
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testCreate() throws ClientOrganisationNotFoundException, InvalidPostalCodeException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Suntec");
		boolean result = buildingService.create(clientOrg, "name", "address", "650403", "city", 2, "filepath");
		Assert.assertTrue(result);
		Assert.assertNotNull("Getting users should not be null as already inserted via sql", buildingService.getAllBuildings(clientOrg));
		//Assert.assertEquals("expected no. of users is 12", 12, users.size());

	}
	
	@Test(expected=InvalidPostalCodeException.class)
	public void testCreateInvalidPostal() throws ClientOrganisationNotFoundException, InvalidPostalCodeException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Suntec");
		boolean result = buildingService.create(clientOrg, "buildingname", "buildingaddress", "123456789", "buildingcity", 5, "buildingfilepath");
		Assert.assertFalse(result);
		//Assert.assertNotNull("Getting users should not be null as already inserted via sql", buildingService.getAllBuildings(clientOrg));
		//Assert.assertEquals("expected no. of users is 12", 12, users.size());

	}

	@Test
	public void testGetAllBuildings() throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		Set<Building> result = buildingService.getAllBuildings(clientOrg);

		Assert.assertNotNull(result);
	}

	@Test
	public void testGetBuilding() throws BuildingNotFoundException{
		Building result = buildingService.getBuilding((long)1);
		Assert.assertNotNull("Getting edited should not be null", result);
	}
	
	@Test(expected=BuildingNotFoundException.class)
	public void testGetBuildingNotFound() throws BuildingNotFoundException{
		Building result = buildingService.getBuilding(Long.MAX_VALUE);
		Assert.assertNull("Getting edited should not be null", result);
	}
	
	@Test
	public void testGetBuildingById() throws BuildingNotFoundException{
		Building result = buildingService.getBuildingById((long)1).get();
		Assert.assertNotNull("Getting edited should not be null", result);
	}
	
	@Test(expected=BuildingNotFoundException.class)
	public void testGetBuildingByIdNotFound() throws BuildingNotFoundException{
		Building result = buildingService.getBuildingById(Long.MAX_VALUE).get();
		Assert.assertNull("Getting edited should not be null", result);
	}

	
	@Test
	public void testEditBuildingInfo() throws ClientOrganisationNotFoundException, BuildingNotFoundException, InvalidPostalCodeException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.editBuildingInfo(clientOrg, (long)1, "newname", "newaddess", "945699", "newcity", 1, "newfilepath");
		Assert.assertTrue(result);

	}
	
	@Test(expected=BuildingNotFoundException.class)
	public void testEditBuildingInfoNotFound() throws ClientOrganisationNotFoundException, BuildingNotFoundException, InvalidPostalCodeException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.editBuildingInfo(clientOrg, Long.MAX_VALUE, "newname", "newaddess", "945699", "newcity", 1, "newfilepath");
		Assert.assertFalse(result);
	}
	
	@Test(expected=InvalidPostalCodeException.class)
	public void testEditBuildingInfoInvalidPostal() throws ClientOrganisationNotFoundException, BuildingNotFoundException, InvalidPostalCodeException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.editBuildingInfo(clientOrg, (long)1, "newname", "newaddess", "99", "newcity", 1, "newfilepath");
		Assert.assertFalse(result);
	}

	
	@Test
	public void testDeleteBuilding() throws ClientOrganisationNotFoundException, BuildingNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.deleteBuilding(clientOrg, (long)2);
		Assert.assertTrue(result);	
	}
	
	@Test(expected=BuildingNotFoundException.class)
	public void testDeleteBuildingNotFound() throws ClientOrganisationNotFoundException, BuildingNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.deleteBuilding(clientOrg, Long.MAX_VALUE);
		Assert.assertFalse(result);	
	}


	@Test
	public void checkBuilding() throws ClientOrganisationNotFoundException, BuildingNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.checkBuilding(clientOrg, (long)1);
		Assert.assertTrue(result);	

	}
	
	@Test(expected=BuildingNotFoundException.class)
	public void checkBuildingNotFound() throws ClientOrganisationNotFoundException, BuildingNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.checkBuilding(clientOrg, Long.MAX_VALUE);
		Assert.assertFalse(result);	
	}


}
