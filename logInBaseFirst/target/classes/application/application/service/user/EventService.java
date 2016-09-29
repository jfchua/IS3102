package application.service.user;

import java.util.Collection;

import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;


public interface EventService {
    
	Event createEvent(EventCreateForm form);
	
	Collection<Event> getAllEvents();
	
	Collection<Event> getAllUsers();

	
}
