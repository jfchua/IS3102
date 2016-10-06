package application.service.user;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import application.domain.BookingAppl;
import application.domain.ClientOrganisation;
import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.EventOrganizer;
import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;


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

	Set<BookingAppl> getBookings(ClientOrganisation client, long id);
}
