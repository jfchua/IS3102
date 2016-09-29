package application.service.user;
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


import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.Message;
import application.domain.User;
import application.repository.EventRepository;


@Service
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	public EventServiceImpl(EventRepository eventRepository) {
		//super();
		this.eventRepository = eventRepository;
	}


	@Override
	public Event createEvent(EventCreateForm form) {
		// TODO Auto-generated method stub
		Event event = new Event();
		event.setEvent_title(form.getEvent_title());	
		event.setEvent_content(form.getEvent_content());
		event.setEvent_description(form.getEvent_description());
		//Get current user and set him as sender, only email available
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails currentUser = (UserDetails) authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		event.setUser(currentUsername);
		System.out.println("Saving the event into event Repo............");
		return eventRepository.save(event);
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
		LOGGER.debug("Getting all events");
		return eventRepository.findAll(new Sort("event_title"));
	}




	@Override
	public Collection<Event> getAllUsers() {
		LOGGER.debug("Getting all event managers");
		return null;
		//return eventRepository.findAll(new Sort(""));
	}



}

