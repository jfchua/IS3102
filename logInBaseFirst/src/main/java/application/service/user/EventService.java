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
import application.exception.EventNotFoundException;


public interface EventService {
    
	 //void createEvent(ClientOrganisation client, String unitsId, String event_title, String event_content, String event_descriptions, String status, 
				//Date event_start_date, Date event_end_date, String filePath);
	
	//Collection<Event> getAllEvents();
	
	//Set<EventOrganizer> getAllEventOrganizers();

    Set<Event> getAllEvents(ClientOrganisation client);
	
	Set<Event> getAllApprovedEvents(ClientOrganisation client);
	
	Set<Event> getAllToBeApprovedEvents(ClientOrganisation client);
	

	boolean updateEventStatusForPayment(ClientOrganisation client, long id, String status) throws EventNotFoundException;
	
	boolean approveEvent(ClientOrganisation client, long id) throws EventNotFoundException;
	
	boolean deleteEvent(ClientOrganisation client, long id) throws EventNotFoundException;
		
	Optional<Event> getEventById(long id) throws EventNotFoundException;
	
	boolean checkEvent(ClientOrganisation client, long id) throws EventNotFoundException;
	
}
