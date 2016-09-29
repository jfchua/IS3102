package application.service.user;

import java.util.Collection;

import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;


public interface EventManagerService {
	
	Event viewSingleEvent();
	
	Collection<Event> getAllEvents();
	
	Collection<Event> getAllPendingEvents();
	
	Collection<Event> getAllApprovedEvents();
	
	Collection<Event> getAllUsers();

	
}
