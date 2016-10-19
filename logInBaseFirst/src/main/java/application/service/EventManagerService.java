package application.service;

import java.util.Collection;

import application.entity.Event;
import application.entity.EventCreateForm;
import application.entity.Message;
import application.entity.SendMessageForm;
import application.entity.User;


public interface EventManagerService {
	
	Event viewSingleEvent();
	
	Collection<Event> getAllEvents();
	
	Collection<Event> getAllPendingEvents();
	
	Collection<Event> getAllApprovedEvents();
	
	Collection<Event> getAllUsers();

	
}
