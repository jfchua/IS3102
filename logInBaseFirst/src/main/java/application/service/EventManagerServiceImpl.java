package application.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import application.entity.Event;
import application.entity.EventCreateForm;
import application.entity.Message;
import application.entity.User;
import application.repository.EventManagerRepository;


@Service
public class EventManagerServiceImpl implements EventManagerService {
	private final EventManagerRepository eventManagerRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerServiceImpl.class);

	@Autowired
	public EventManagerServiceImpl(EventManagerRepository eventManagerRepository) {
		//super();
		this.eventManagerRepository = eventManagerRepository;
	}


	@Override
	public Event viewSingleEvent() {
		// TODO Auto-generated method stub
		Long event = null;
		
		System.out.println("Retrieving the single event into event Repo............");

		return eventManagerRepository.getOne(event);
	}

	/*public Collection<Event> getEventsByUsername(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails currentUser = (UserDetails) authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		return eventRepository.getEventsByUsername(currentUsername);
	}
	 */
	@Override
	public Collection<Event> getAllEvents() {
		// TODO Auto-generated method stub

		System.out.println("Retrieving all events from event Repo............");
		LOGGER.debug("Getting all events");
		return eventManagerRepository.findAll(new Sort("event_title"));
	}




	@Override
	public Collection<Event> getAllUsers() {
		LOGGER.debug("Getting all event managers");
		return null;
		//return eventRepository.findAll(new Sort(""));
	}


	@Override
	public Collection<Event> getAllPendingEvents() {
		System.out.println("Retrieving pending events from event Repo............");
		LOGGER.debug("Getting pending events");
		return eventManagerRepository.findAll(new Sort("event_status"));

	}


	@Override
	public Collection<Event> getAllApprovedEvents() {
		// TODO Auto-generated method stub
		return null;
	}



}

