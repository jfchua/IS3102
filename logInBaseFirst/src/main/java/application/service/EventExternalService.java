package application.service;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.EventCreateForm;
import application.entity.EventOrganizer;
import application.entity.Message;
import application.entity.SendMessageForm;
import application.entity.User;


public interface EventExternalService {
    boolean createEvent(ClientOrganisation client, User eventOrg, String unitsId, String event_title, String event_content, String event_descriptions, String status, 
			Date event_start_date, Date event_end_date, String filePath);
	
	Set<Event> getAllEventsByOrg(ClientOrganisation client, User eventOrg);
	
	Set<Event> getAllApprovedEventsByOrg(ClientOrganisation client, User eventOrg);
	
	boolean editEvent(ClientOrganisation client, User eventOrg, long id, String unitsId, String event_title, String event_content, String event_description, String status,
			Date event_start_date, Date event_end_date, String filePath);

	boolean updateEventOrganizerWithOnlyEventId(long eventId);
	
	boolean updateEventOrganizerForDelete(long eventId);
	
	boolean deleteEvent(ClientOrganisation client, long id);
		
	Optional<Event> getEventById(long id);
	
	String getUnitsId(long eventId);
	
	boolean checkUnit(ClientOrganisation client, long unitId);
	
	boolean checkEvent(ClientOrganisation client, long eventId);
	
	boolean checkAvailability(ClientOrganisation client, User user, String unitsId, Date event_start_date, Date event_end_date);
	
	boolean checkAvailabilityForUpdate(ClientOrganisation client, User user,long eventId, String unitsId, Date event_start_date, Date event_end_date);

	Double checkRent(ClientOrganisation client, User user, String unitsId, Date event_start_date, Date event_end_date);
	
	Set<BookingAppl> getBookings(ClientOrganisation client, long id);
}
