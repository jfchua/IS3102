package application.test.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ClientOrganisation;
import application.domain.Icon;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.IconNotFoundException;
import application.exception.InvalidIconException;
import application.service.user.ClientOrganisationService;
import application.service.user.IconService;
import application.test.AbstractTest;

@Transactional
public class IconServiceTest extends AbstractTest {
	@Autowired
	private IconService iconService;
	@Autowired
	private ClientOrganisationService orgService;
	private Long id1 = (long) 0;
	private Long id2 = (long) 0;

	@Before
	public void setUp() throws ClientOrganisationNotFoundException, InvalidIconException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean created = iconService.createIconOnClientOrganisation(org, "CUST", "images/Logo.png");
		iconService.createIconOnClientOrganisation(org, "CUST", "images/Logo2.png");
		if ( created ){
			System.out.println("setup created" );		
			for (Icon i :iconService.getAllIconFromClientOrganisation(org)){
				if ( id1 == 0){
				id1 = i.getId();
				}
				else{
					id2 = i.getId();
				}
			}
		}
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testGetAllIconFromClientOrganisation() throws ClientOrganisationNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		Assert.assertNotNull(iconService.getAllIconFromClientOrganisation(org));
	}

	@Test
	public void testCreateIconOnClientOrganisation() throws InvalidIconException, ClientOrganisationNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = iconService.createIconOnClientOrganisation(org, "CUST", "images/algattasLogo.png");
		Assert.assertTrue(result);

	}

	@Test(expected=InvalidIconException.class)
	public void testCreateIconOnClientOrganisationInvalidIcon() throws InvalidIconException, ClientOrganisationNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = iconService.createIconOnClientOrganisation(org, "CUST", "invalidicon");
		Assert.assertFalse(result);

	}
	@Test
	public void testEditIcon() throws ClientOrganisationNotFoundException, IconNotFoundException, InvalidIconException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = iconService.editIcon(org, id1, "images/logo.png");
		Assert.assertTrue(result);


	}

	@Test(expected=IconNotFoundException.class)
	public void testEditIconNotFound() throws ClientOrganisationNotFoundException, IconNotFoundException, InvalidIconException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = iconService.editIcon(org, Long.MAX_VALUE, "images/logo.png");
		Assert.assertFalse(result);

	}

	@Test
	public void testDeleteIconFromClientOrganisation() throws ClientOrganisationNotFoundException, IconNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = iconService.deleteIconFromClientOrganisation(org, id2);
		Assert.assertTrue(result);

	}

	@Test(expected=IconNotFoundException.class)
	public void testDeleteIconFromClientOrganisationNotFound() throws ClientOrganisationNotFoundException, IconNotFoundException{
		ClientOrganisation org = orgService.getClientOrganisationByName("Expo");
		boolean result = iconService.deleteIconFromClientOrganisation(org, Long.MAX_VALUE);
		Assert.assertFalse(result);
	}




}
