package application.service.user;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import application.domain.Area;
import application.domain.ClientOrganisation;
import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.EventOrganizer;
import application.domain.Message;
import application.domain.Unit;
import application.domain.User;
import application.repository.EventOrganizerRepository;
import application.repository.EventRepository;
import application.repository.UnitRepository;


@Service
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final UnitRepository unitRepository;
	
	private final EventOrganizerRepository eventOrganizerRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	public EventServiceImpl(EventRepository eventRepository, UnitRepository unitRepository,EventOrganizerRepository eventOrganizerRepository) {
		super();
		this.eventRepository = eventRepository;
		this.unitRepository = unitRepository;
		this.eventOrganizerRepository=eventOrganizerRepository;
		//this.eventOrganizerRepository = eventOrganizerRepository;
		
	}

	/*public Collection<Event> getEventsByUsername(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails currentUser = (UserDetails) authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		return eventRepository.getEventsByUsername(currentUsername);
	}
	 */

/*
	@Override
	public Set<EventOrganizer> getAllEventOrganizers() {
		// TODO Auto-generated method stub
		return null;
	}*/


	@Override
	public Set<Event> getAllEvents() {
		// TODO Auto-generated method stub
		return eventRepository.getAllEvents();
	}


	@Override
	public Set<Event> getAllApprovedEvents() {
		Set<Event> allEvents = getAllEvents();
		Set<Event> approvedEvents = new HashSet<Event>();
		for(Event ev: allEvents){
				if(ev.getEvent_approval_status().equals("approved"))
					approvedEvents.add(ev);	
		}
	    return approvedEvents;
	}


	@Override
	public Set<Event> getAllToBeApprovedEvents() {
		Set<Event> allEvents = getAllEvents();
		Set<Event> toBeApprovedEvents = new HashSet<Event>();
		for(Event ev: allEvents){
				if(ev.getEvent_approval_status().equals("processing"))
					toBeApprovedEvents.add(ev);	
		}
	    return toBeApprovedEvents;
	}



	@Override
	public boolean updateEventStatusForPayment(long id, String status) {
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()){	
				Event event = event1.get();
				event.setEvent_approval_status(status);
				eventRepository.save(event);
			}
		}
	catch(Exception e){
		return false;
	}
		return true;
	}


	@Override
	public boolean approveEvent(long id) {
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()){	
				Event event = event1.get();
				event.setEvent_approval_status("approved");
				eventRepository.save(event);
			}
		}
	catch(Exception e){
		return false;
	}
		return true;
	}


	@Override
	public boolean deleteEvent(long id) {
		// TODO Auto-generated method stub
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()){	
				Event event = event1.get();
				/*
				User org=event.getEventOrg();
				Set<Event> events=org.getEvents();
				events.remove(event);
				org.setEvents(events);
				//eventOrganizerRepository.saveAndFlush(org); 
				event.setAreas(new HashSet<Area>());
				event.setUnits(new HashSet<Unit>());
				*/
				eventRepository.delete(event);
				eventRepository.flush();
			}
		}
	catch(Exception e){
		return false;
	}
		return true;
	}


	@Override
	public Optional<Event> getEventById(long id) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting event={}", id);
		return Optional.ofNullable(eventRepository.findOne(id));
	}

	/*
	@Override
	public void createEvent(ClientOrganisation client, String unitsId, String event_title, String event_content,
			String event_descriptions, String status, Date event_start_date, Date event_end_date, String filePath) {
		Event event = new Event();
		String[] units = unitsId.split(" ");
		System.out.println(units[0]);
		for(int i = 0; i<units.length; i ++){
			long uId = Long.valueOf(units[i]);
			Optional<Unit> unit1 = unitRepository.getUnitById(uId);
			System.out.println(uId);
			System.out.println(unit1.isPresent());
			if(unit1.isPresent()){
				System.out.println("inside if");
				Unit unit = unit1.get();
				System.out.println("get unit");
				event.getUnits().add(unit);
				System.out.println("add unit");
				Set<Event> eventFromUnit = unit.getEvents();
				eventFromUnit.add(event);
				unit.setEvents(eventFromUnit);			
				System.out.println("save unit");
				System.out.println("saved by repo");
			
			}
			else{
				System.out.println("no units");
			}
		}
		event.setEvent_title(event_title);
		event.setEvent_content(event_content);
		event.setEvent_description(event_descriptions);
		event.setEvent_approval_status(status);
		event.setEvent_start_date(event_start_date);
		event.setEvent_end_date(event_end_date);
		event.setFilePath(filePath);
		eventRepository.save(event);
			Set<Event> events = eventOrg.getEvents();
			events.add(event);
			eventOrg.setEvents(events);
			event.setEventOrg(eventOrg);
		    eventRepository.save(event);
			eventOrganizerRepository.save(eventOrg);
		    eventRepository.save(event);
		
		
		
	}*/



}

