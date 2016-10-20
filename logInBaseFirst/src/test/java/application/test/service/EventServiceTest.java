package application.test.service;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import application.domain.BookingAppl;
import application.domain.ClientOrganisation;
import application.domain.Event;
import application.domain.Level;
import application.domain.Unit;
import application.domain.User;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EventNotFoundException;
import application.exception.UserNotFoundException;
import application.service.user.ClientOrganisationService;
import application.service.user.EventExternalService;
import application.service.user.EventService;
import application.service.user.LevelService;
import application.service.user.UnitService;
import application.service.user.UserService;
import application.test.AbstractTest;

@Transactional
public class EventServiceTest extends AbstractTest {
	@Autowired
	private EventService eventService;
	@Autowired
	private ClientOrganisationService orgService;
	@Autowired
	private EventExternalService eService;
	@Autowired
	private UserService uService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private LevelService lService;

	ClientOrganisation org;
	long eventId = -1;
	long eventId2 = -1;
	long unitId;
	long unitId2;
	long levelId;

	@Before
	public void setUp() throws UserNotFoundException, ClientOrganisationNotFoundException{
		org = orgService.getClientOrganisationByName("Expo");
		User user = uService.getUserByEmail("exteve@localhost").get();
		boolean level = lService.create(org, 1, 1, 100, 100, "FILEPATH");
		Set<Level> levels = lService.getAllLevels(org, 1);
		for ( Level l : levels){
			levelId = l.getId();
		}

		Unit u = unitService.createUnitOnLevel(levelId,10, 10, 10, 10, "red", "type", "#123", 10, 10, false, "desc");
		Unit u2 = unitService.createUnitOnLevel(levelId,101, 110, 110, 110, "red", "type", "#1213", 110, 110, false, "desc");

		unitId = u.getId();
		unitId2 = u2.getId();

		eService.createEvent(org, user, String.valueOf(unitId), "title", "content", "desc", "status", new Date(), new Date(), "path");
		eService.createEvent(org, user, String.valueOf(unitId2), "ti2tle", "con2tent", "d2esc", "stat2us", new Date(), new Date(), "pa2th");


		Set<Event> resultEvent = eventService.getAllEvents(org);
		for ( Event e: resultEvent){
			if ( eventId == -1){
				eventId = e.getId();
			}
			else{
				eventId2 = e.getId();
				break;
			}
		}
	}

	@After
	public void tearDown(){

	}

	@Test
	public void testGetAllEvents(){
		Set<Event> result = eventService.getAllEvents(org);	
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetAllApprovedEvents(){
		Set<Event> result = eventService.getAllApprovedEvents(org);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetAllToBeApprovedEvents(){
		Set<Event> result = eventService.getAllToBeApprovedEvents(org);
		Assert.assertNotNull(result);
	}

	@Test
	public void testGetEventById() throws EventNotFoundException{
		Optional<Event> result = eventService.getEventById(eventId);
		Assert.assertNotNull(result);
	}

	@Test(expected=EventNotFoundException.class)
	public void testGetEventByIdNotFound() throws EventNotFoundException{
		Optional<Event> result = eventService.getEventById(Long.MAX_VALUE);
		Assert.assertNull(result);
	}

	@Test
	public void testCheckEvent() throws EventNotFoundException{
		boolean result = eventService.checkEvent(org, eventId);
		Assert.assertTrue(result);
	}

	@Test(expected=EventNotFoundException.class)
	public void testCheckEventNotFound() throws EventNotFoundException{
		boolean result = eventService.checkEvent(org, Long.MAX_VALUE);
		Assert.assertFalse(result);
	}

	@Test
	public void testDeleteEvent() throws EventNotFoundException{
		System.err.println("Delete eventid2");
		boolean result = eventService.deleteEvent(org, eventId2);
		System.err.println("result is + " + result);
		Assert.assertTrue(result);
	}

	@Test(expected=EventNotFoundException.class)
	public void testDeleteEventNotFound() throws EventNotFoundException{
		System.err.println("Delete eventidmax");
		boolean result = eventService.deleteEvent(org, Long.MAX_VALUE);
		Assert.assertFalse(result);		
	}

	@Test
	public void testApproveEvent() throws EventNotFoundException{
		boolean result = eventService.approveEvent(org, eventId);
		Assert.assertTrue(result);
	}

	@Test(expected=EventNotFoundException.class)
	public void testApproveEventNotFound() throws EventNotFoundException{
		boolean result = eventService.approveEvent(org, Long.MAX_VALUE);
		Assert.assertFalse(result);
	}

	@Test
	public void testUpdateEventStatusForPayment() throws EventNotFoundException{
		boolean result = eventService.updateEventStatusForPayment(org, eventId, "PAID");
		Assert.assertTrue(result);
	}

	@Test(expected=EventNotFoundException.class)
	public void testUpdateEventStatusForPaymentNotFound() throws EventNotFoundException{
		boolean result = eventService.updateEventStatusForPayment(org, Long.MAX_VALUE, "PAID");
		Assert.assertFalse(result);
	}			


}
