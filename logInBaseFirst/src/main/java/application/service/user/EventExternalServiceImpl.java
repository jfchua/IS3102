package application.service.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import application.domain.Building;
import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.EventOrganizer;
import application.domain.Level;
import application.domain.Unit;
import application.domain.User;
import application.repository.EventOrganizerRepository;
import application.repository.EventRepository;
import application.repository.UnitRepository;
import application.repository.UserRepository;
@Service
public class EventExternalServiceImpl implements EventExternalService {
    private final UserRepository userRepository;
	private final EventRepository eventRepository;
	//private final EventOrganizerRepository eventOrganizerRepository;
	private final UnitRepository unitRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	public EventExternalServiceImpl(EventRepository eventRepository, UserRepository userRepository, 
			UnitRepository unitRepository) {
		//super();
		this.eventRepository = eventRepository;
		//this.eventOrganizerRepository = eventOrganizerRepository;
		this.unitRepository = unitRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Set<Event> getAllEventsByOrg(User eventOrg) {
	    return eventOrg.getEvents();
	}

	@Override
	public Set<Event> getAllApprovedEventsByOrg(User eventOrg) {
		Set<Event> approvedEvent = new HashSet<Event>();
	    Set<Event> allEvents = eventOrg.getEvents();
		for(Event ev: allEvents){
				if(ev.getEvent_approval_status().equals("approved"))
					approvedEvent.add(ev);
			}
	    return approvedEvent;
	}

	@Override
	public boolean editEvent(long id, String unitsId, String event_title, String event_content, String event_description,
			String status, Date event_start_date, Date event_end_date, String filePath) {
		boolean isAvailable = true;
		Date d1 = event_start_date;
		Date d2 = event_end_date;
		if(d1.compareTo(d2)>0)
			return false;
		int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
		Calendar c = Calendar.getInstance();
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()&&isAvailable){			
				Event event = event1.get();
				Date d3 = event.getEvent_start_date();
				Date d4 = event.getEvent_end_date();
				  int diffInDays2 = (int) ((d4.getTime() - d3.getTime()) / (1000 * 60 * 60 * 24));
				Set<Unit> unitsOld = event.getUnits();
				String[] units = unitsId.split(" ");
				Set<String> unitsNew = new HashSet<String>();
				for(int i = 0; i<units.length; i ++){
				unitsNew.add(units[i]);
				Optional<Unit> unitNew = unitRepository.getUnitById(Long.valueOf(units[i]));
				if(unitNew.isPresent()&&(!unitsOld.contains(unitNew.get()))){					
					Unit unit1 = unitNew.get();
					unitsOld.add(unit1);
					ArrayList<Date> avail = unit1.getAvail();							
					for(int startNum = 0; startNum <= diffInDays; startNum ++){
						if(avail.contains(d1)){
						isAvailable = false;
						break;
						}
						else{
						avail.add(d1);
						c.setTime(d1); 
						c.add(Calendar.DATE, 1);
						d1 = c.getTime();
						}
					}					
				}//DONE.
				else if (unitNew.isPresent()&&(unitsOld.contains(unitNew.get()))){
				Unit unit1 = unitNew.get();
				ArrayList<Date> avail = unit1.getAvail();		
				for(int startNum = 0; startNum <= diffInDays; startNum ++){
					if(!avail.contains(d1)){
					avail.add(d1);
					c.setTime(d1); 
					c.add(Calendar.DATE, 1);
					d1 = c.getTime();
					}//DONE
					else{
						if(d1.before(d3) || d1.after(d4)){
						isAvailable = false;
						break;
						}
					}
				}
				}
				}//DONE
				
				System.out.println(units[0]);
				System.out.println("end of first for loop");
				if(isAvailable){
				//need to save the new units?
					
				for(Unit u: unitsOld){
				if(!unitsNew.contains(String.valueOf(u.getId()))){
				   unitsOld.remove(u);
				   System.out.println(u.getId());	   
				   Set<Event> eventFromUnit = u.getEvents();
				   eventFromUnit.remove(event);
				   u.setEvents(eventFromUnit);
				   ArrayList<Date> avail = u.getAvail();	
				 
					for(int startNum = 0; startNum <= diffInDays2; startNum ++){
						avail.remove(d3);
						c.setTime(d3); 
						c.add(Calendar.DATE, 1);
						d1 = c.getTime();
					}
				   System.out.println("set null");
				}
				else{
					ArrayList<Date> avail = u.getAvail();
					ArrayList<Date> newDate = new ArrayList<>();	
					ArrayList<Date> oldDate = new ArrayList<>();
					for(int startNum = 0; startNum <= diffInDays; startNum ++){
						newDate.add(d1);
						c.setTime(d1); 
						c.add(Calendar.DATE, 1);
						d1 = c.getTime();
					}
					for(int startNum = 0; startNum <= diffInDays2; startNum ++){
						oldDate.add(d3);
						c.setTime(d3); 
						c.add(Calendar.DATE, 1);
						d1 = c.getTime();
					}
					for(Date date: oldDate){
						if(!newDate.contains(date))
					   avail.remove(date);   
					}
					
				}
				System.out.println("exiting");
				unitRepository.save(u);
				}
				System.out.println("end of second for loop");				
				event.setUnits(unitsOld);
				event.setEvent_title(event_title);
				System.out.println("after title");
				event.setEvent_content(event_content);
				event.setEvent_description(event_description);
				event.setEvent_approval_status(status);
				event.setEvent_start_date(event_start_date);
				event.setEvent_end_date(event_end_date);
				event.setFilePath(filePath);
			    eventRepository.save(event);
			    System.out.println("saved by repo");
			    
				}
			}
			}catch(Exception e){
				return isAvailable;
			}
			return isAvailable;
	}

	@Override
	public boolean deleteEvent(long id) {
		System.out.println(id);
      try{		
			Optional<Event> event1 = getEventById(id);
			Event event = null;
			if(event1.isPresent()){		
				System.out.println("inside TRY");
				 event = event1.get();
				Set<Unit> units = event.getUnits();
				for(Unit u: units){
					ArrayList<Date> avail = u.getAvail();
					Date d1 = event.getEvent_start_date();
					Date d2 = event.getEvent_start_date();
					int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
					Calendar c = Calendar.getInstance(); 		
					for(int startNum = 0; startNum <= diffInDays; startNum ++){
						avail.remove(d1);
						c.setTime(d1); 
						c.add(Calendar.DATE, 1);
						d1 = c.getTime();
					}
					Set<Event> eventFromUnit = u.getEvents();
					eventFromUnit.remove(event);
					u.setEvents(eventFromUnit);
				}
				event.setUnits(new HashSet<Unit>());
			    User eventOrg = event.getEventOrg();
				Set<Event> events = eventOrg.getEvents();
				events.remove(event);
				eventOrg.setEvents(events);
				event.setEvent_approval_status("cancelled");
				userRepository.save(eventOrg);		   
				eventRepository.save(event);
				eventRepository.flush();
			    userRepository.flush();
			}
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public Optional<Event> getEventById(long id) {
		LOGGER.debug("Getting event={}", id);
		return Optional.ofNullable(eventRepository.findOne(id));
	}

	@Override
	public boolean createEvent(User eventOrg, String unitsId, String event_title, String event_content, String event_description, String status,
			Date event_start_date, Date event_end_date, String filePath) {
		Event event = new Event();
		String[] units = unitsId.split(" ");
		System.out.println(units[0]);
		Date d1 = event_start_date;
		Date d2 = event_end_date;
		if(d1.compareTo(d2)>0)
			return false;
		boolean isAvailable = true;
		for(int i = 0; i<units.length; i ++){
			long uId = Long.valueOf(units[i]);
			Optional<Unit> unit1 = unitRepository.getUnitById(uId);
			System.out.println(uId);
			System.out.println(unit1.isPresent());
			if(unit1.isPresent()&&isAvailable){
				System.out.println("inside if");
				Unit unit = unit1.get();
				System.out.println("get unit");
				event.getUnits().add(unit);
				System.out.println("add unit lol");
				Set<Event> eventFromUnit = unit.getEvents();
				eventFromUnit.add(event);
				unit.setEvents(eventFromUnit);	
				unit.createList();
				ArrayList<Date> avail = unit.getAvail();		
				int diffInDays = (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
				System.out.println(d1);
				System.out.println(d2);
				System.out.println(diffInDays);
				for(int startNum = 0; startNum <= diffInDays; startNum ++){
					System.out.println("inside the loop");
					if ( avail != null ) System.err.println("not null");
					if(avail.contains(d1)){
						isAvailable = false;
						break;
					}
					else{
						System.out.println("else");
					unit.getAvail().add(d1);
				    Date nextDay = new Date(d1.getTime() + (1000 * 60 * 60 * 24));
					d1 = nextDay;	
					}
					System.out.println("inside the loop for add");
				}
				unit.setAvail(avail);
				System.out.println("save unit");
				System.out.println("saved by repo");
			}
			else{
				System.out.println("no units");
			}
		}
		if(isAvailable){
		event.setEvent_title(event_title);
		event.setEvent_content(event_content);
		event.setEvent_description(event_description);
		event.setEvent_approval_status(status);
		
		event.setEvent_start_date(event_start_date);
		event.setEvent_end_date(event_end_date);
		event.setFilePath(filePath);
		eventRepository.save(event);
		//Optional<EventOrganizer> eventOrg1;	
		Set<Event> events = eventOrg.getEvents();
		events.add(event);
		eventOrg.setEvents(events);
		event.setEventOrg(eventOrg);
		eventRepository.save(event);
		userRepository.save(eventOrg);
		//eventRepository.save(event);
		}
		return isAvailable;
	}

	@Override
	public boolean updateEventOrganizerWithOnlyEventId(long eventId) {
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
			Event event = event1.get();
			User eventOrg= event.getEventOrg();
			Set<Event> events = eventOrg.getEvents();
			events.add(event);
			eventOrg.setEvents(events);
			userRepository.save(eventOrg);
			}
			}
			catch(Exception e){
				return false;
				}
			return true;
	}

	@Override
	public boolean updateEventOrganizerForDelete(long eventId) {
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
		Event event = event1.get();		
		User eventOrg = event.getEventOrg();
		System.out.println("inside TRY 1");
		Set<Event> events = eventOrg.getEvents();
		System.out.println("inside TRY 2");
		events.remove(event);
		System.out.println("inside TRY 3");
		eventOrg.setEvents(events);
		System.out.println("hasnt persist yet");
		userRepository.save(eventOrg);
			}
		}
		catch(Exception e){
			return false;
			}
		return true;
	}

	@Override
	public String getUnitsId(long eventId) {
		String unitsId = "";	
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
		Event event = event1.get();	
		Set<Unit> unitList = event.getUnits();
		for(Unit u : unitList)
			unitsId = unitsId + u.getId() +" ";
	     }
		}catch(Exception e){
		
		}
     return unitsId;
	}

	/*
	@Override
	public Set<Event> getAllEventsByOrg(EventOrganizer eventOrg) {
	    return eventOrg.getEvents();
	}

	@Override
	public Set<Event> getAllApprovedEventsByOrg(EventOrganizer eventOrg) {
		Set<Event> approvedEvent = new HashSet<Event>();
	    Set<Event> allEvents = eventOrg.getEvents();
		for(Event ev: allEvents){
				if(ev.getEvent_approval_status().equals("approved"))
					approvedEvent.add(ev);
			}
	    return approvedEvent;
	}

	@Override
	public boolean editEvent(long id, String unitsId, String event_title, String event_content, String event_description,
			String status, Date event_start_date, Date event_end_date, String filePath) {
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()){			
				Event event = event1.get();
				Set<Unit> unitsOld = event.getUnits();
				String[] units = unitsId.split(" ");
				Set<String> unitsNew = new HashSet<String>();
				for(int i = 0; i<units.length; i ++){
				unitsNew.add(units[i]);
				Optional<Unit> unitNew = unitRepository.getUnitById(Long.valueOf(units[i]));
				if(unitNew.isPresent()&&(!unitsOld.contains(unitNew.get()))){
					Unit unit1 = unitNew.get();
					unitsOld.add(unit1);
				}
				}
				System.out.println(units[0]);
				System.out.println("end of first for loop");
				for(Unit u: unitsOld){
				if(!unitsNew.contains(String.valueOf(u.getId()))){
				   unitsOld.remove(u);
				   System.out.println(u.getId());	   
				   Set<Event> eventFromUnit = u.getEvents();
				   eventFromUnit.remove(event);
				   u.setEvents(eventFromUnit);
				   System.out.println("set null");
				}
				System.out.println("exiting");
				}
				System.out.println("end of second for loop");
				event.setUnits(unitsOld);
				event.setEvent_title(event_title);
				System.out.println("after title");
				event.setEvent_content(event_content);
				event.setEvent_description(event_description);
				event.setEvent_approval_status(status);
				event.setEvent_start_date(event_start_date);
				event.setEvent_end_date(event_end_date);
				event.setFilePath(filePath);
			    eventRepository.save(event);
			    System.out.println("saved by repo");
			}
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public boolean deleteEvent(long id) {
		System.out.println(id);
      try{		
			Optional<Event> event1 = getEventById(id);
			Event event = null;
			if(event1.isPresent()){		
				System.out.println("inside TRY");
				 event = event1.get();
				Set<Unit> units = event.getUnits();
				for(Unit u: units){
					Set<Event> eventFromUnit = u.getEvents();
					eventFromUnit.remove(event);
					u.setEvents(eventFromUnit);
				}
				event.setUnits(new HashSet<Unit>());
			    EventOrganizer eventOrg = event.getEventOrg();
				Set<Event> events = eventOrg.getEvents();
				events.remove(event);
				eventOrg.setEvents(events);
				event.setEvent_approval_status("cancelled");
				eventOrganizerRepository.save(eventOrg);		   
				eventRepository.save(event);
				eventRepository.flush();
			    eventOrganizerRepository.flush();
			}
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public Optional<Event> getEventById(long id) {
		LOGGER.debug("Getting event={}", id);
		return Optional.ofNullable(eventRepository.findOne(id));
	}

	@Override
	public void createEvent(EventOrganizer eventOrg, String unitsId, String event_title, String event_content, String event_description, String status,
			Date event_start_date, Date event_end_date, String filePath) {
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
		event.setEvent_description(event_description);
		event.setEvent_approval_status(status);
		event.setEvent_start_date(event_start_date);
		event.setEvent_end_date(event_end_date);
		event.setFilePath(filePath);
		eventRepository.save(event);
		Optional<EventOrganizer> eventOrg1;	
			Set<Event> events = eventOrg.getEvents();
			events.add(event);
			eventOrg.setEvents(events);
			event.setEventOrg(eventOrg);
		    eventRepository.save(event);
			eventOrganizerRepository.save(eventOrg);
		    eventRepository.save(event);
	}

	@Override
	public boolean updateEventOrganizerWithOnlyEventId(long eventId) {
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
			Event event = event1.get();
			EventOrganizer eventOrg= event.getEventOrg();
			Set<Event> events = eventOrg.getEvents();
			events.add(event);
			eventOrg.setEvents(events);
			eventOrganizerRepository.save(eventOrg);
			}
			}
			catch(Exception e){
				return false;
				}
			return true;
	}

	@Override
	public boolean updateEventOrganizerForDelete(long eventId) {
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
		Event event = event1.get();		
		EventOrganizer eventOrg = event.getEventOrg();
		System.out.println("inside TRY 1");
		Set<Event> events = eventOrg.getEvents();
		System.out.println("inside TRY 2");
		events.remove(event);
		System.out.println("inside TRY 3");
		eventOrg.setEvents(events);
		System.out.println("hasnt persist yet");
		eventOrganizerRepository.save(eventOrg);
			}
		}
		catch(Exception e){
			return false;
			}
		return true;
	}

	@Override
	public String getUnitsId(long eventId) {
		String unitsId = "";	
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
		Event event = event1.get();	
		Set<Unit> unitList = event.getUnits();
		for(Unit u : unitList)
			unitsId = unitsId + u.getId() +" ";
	     }
		}catch(Exception e){
		
		}
     return unitsId;
	}

	@Override
	public boolean checkAvailability(long unitId, Date event_start_date, Date event_end_date) {
		// TODO Auto-generated method stub
		Optional<Unit> unit = unitRepository.getUnitById(unitId);
		if(!unit.isPresent())
			return false;
		else{
		Set<Unit> unitsBooked = unitRepository.getUnitsByDates(event_start_date, event_end_date);
		if(!unitsBooked.contains(unit.get()))
		    return true;
		else
			return false;
		}
	}*/
}
