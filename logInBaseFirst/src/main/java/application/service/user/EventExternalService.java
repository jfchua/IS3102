package application.service.user;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.EventOrganizer;
import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;


public interface EventExternalService {
    boolean createEvent(User eventOrg, String unitsId, String event_title, String event_content, String event_descriptions, String status, 
			Date event_start_date, Date event_end_date, String filePath);
	
	Set<Event> getAllEventsByOrg(User eventOrg);
	
	Set<Event> getAllApprovedEventsByOrg(User eventOrg);
	
	boolean editEvent(long id, String unitsId, String event_title, String event_content, String event_description, String status,
			Date event_start_date, Date event_end_date, String filePath);

	boolean updateEventOrganizerWithOnlyEventId(long eventId);
	
	boolean updateEventOrganizerForDelete(long eventId);
	
	boolean deleteEvent(long id);
		
	Optional<Event> getEventById(long id);
	
	String getUnitsId(long eventId);
	
	//boolean checkAvailability(long unitId, Date event_start_date, Date event_end_date);
}
