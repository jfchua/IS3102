package application.service.user;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.EventOrganizer;
import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;


public interface EventService {
    
	 //void createEvent(ClientOrganisation client, String unitsId, String event_title, String event_content, String event_descriptions, String status, 
				//Date event_start_date, Date event_end_date, String filePath);
	
	//Collection<Event> getAllEvents();
	
	//Set<EventOrganizer> getAllEventOrganizers();

    Set<Event> getAllEvents();
	
	Set<Event> getAllApprovedEvents();
	
	Set<Event> getAllToBeApprovedEvents();
	

	boolean updateEventStatusForPayment(long id, String status);
	
	boolean approveEvent(long id);
	
	boolean deleteEvent(long id);
		
	Optional<Event> getEventById(long id);
	
}
