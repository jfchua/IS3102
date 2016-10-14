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
import application.exception.ClientOrganisationNotFoundException;
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
	public void testCreate() throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Suntec");
		buildingService.create(clientOrg, "name", "address", "999", "city", 2, "filepath");

		Assert.assertNotNull("Getting users should not be null as already inserted via sql", buildingService.getAllBuildings(clientOrg));
		//Assert.assertEquals("expected no. of users is 12", 12, users.size());

	}

	@Test
	public void testGetAllBuildings() throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		Set<Building> buildings = buildingService.getAllBuildings(clientOrg);

		Assert.assertNotNull(buildings);
	}

	@Test
	public void testEditBuildingInfo() throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		buildingService.editBuildingInfo(clientOrg, (long)1, "newname", "newaddess", "945699", "newcity", 1, "newfilepath");
		Building result = buildingService.getBuilding((long)1);

		Assert.assertNotNull("Getting edited should not be null", result);
		Assert.assertEquals("Should be changed to new name", "newaddess",result.getAddress());

	}

	@Test
	public void testGetBuilding(){
		Building building = buildingService.getBuilding((long)1);
		Assert.assertNotNull("Getting edited should not be null", building);
	}
	@Test
	public void testGetBuildingById(){
		Building building = buildingService.getBuildingById((long)1).get();
		Assert.assertNotNull("Getting edited should not be null", building);


	}

	@Test(expected=NoSuchElementException.class)
	public void deleteBuilding() throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		buildingService.deleteBuilding(clientOrg, (long)2);
		Building building = buildingService.getBuilding((long)2);
		Assert.assertNull(building);		
	}


	@Test
	public void checkBuilding() throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrgService.getClientOrganisationByName("Expo");
		boolean result = buildingService.checkBuilding(clientOrg, (long)1);
		Assert.assertTrue(result);	

	}


}
