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

import application.domain.BookingAppl;
import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.Event;
import application.domain.EventCreateForm;
import application.domain.EventOrganizer;
import application.domain.Level;
import application.domain.Role;
import application.domain.Unit;
import application.domain.User;
import application.repository.BookingApplRepository;
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
	private final BookingApplRepository bookingApplRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	public EventExternalServiceImpl(EventRepository eventRepository, UserRepository userRepository, 
			UnitRepository unitRepository, BookingApplRepository bookingApplRepository) {
		//super();
		this.eventRepository = eventRepository;
		//this.eventOrganizerRepository = eventOrganizerRepository;
		this.unitRepository = unitRepository;
		this.userRepository = userRepository;
		this.bookingApplRepository = bookingApplRepository;
	}

	@Override
	public Set<Event> getAllEventsByOrg(ClientOrganisation client, User eventOrg) {
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_EXTEVE") && u.equals(eventOrg))
			    doesHave = true;
			   }
		}
		if(doesHave)
	        return eventOrg.getEvents();
		else
			return new HashSet<Event>();
	}

	@Override
	public Set<Event> getAllApprovedEventsByOrg(ClientOrganisation client, User eventOrg) {
		Set<Event> approvedEvent = new HashSet<Event>();
	    Set<Event> allEvents = eventOrg.getEvents();
	    Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_EXTEVE") && u.equals(eventOrg))
			    doesHave = true;
			   }
		}
		if(doesHave){
		for(Event ev: allEvents){
				if(ev.getEvent_approval_status().equals("approved"))
					approvedEvent.add(ev);
			}
	    return approvedEvent;
		}
		else
			return new HashSet<Event>();
	}

	@Override
	public boolean editEvent(ClientOrganisation client, User eventOrg, long id, String unitsId, String event_title, String event_content, String event_description,
			String status, Date event_start_date, Date event_end_date, String filePath) {
		boolean isAvailable = true;
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_EXTEVE") && u.equals(eventOrg))
			    doesHave = true;
			   }
		}
		if(!doesHave)
			return false;
		System.out.println("1");
		Date d1 = event_start_date;
		Date d2 = event_end_date;
		if(d1.compareTo(d2)>0)
			return false;
		System.out.println("2");
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()&&isAvailable){			
				Event event = event1.get();
				System.out.println("3");
				Set<BookingAppl> bookingList = event.getBookings();
				Set<Unit> unitsOld = new HashSet<Unit>();
				for(BookingAppl b: bookingList)
					unitsOld.add(b.getUnit());
				System.out.println("4");
				String[] units = unitsId.split(" ");
				Set<Unit> unitsNew = new HashSet<Unit>();
				for(int i = 0; i<units.length; i ++){
					System.out.println("inside the loop now");
				if(!checkUnit(client, Long.valueOf(units[i])))
					return false;
				Optional<Unit> unitNew = unitRepository.getUnitById(Long.valueOf(units[i]));
				if(unitNew.isPresent()&&(!unitsOld.contains(unitNew.get()))){	
					System.out.println("iffff");
					Unit unit1 = unitNew.get();		
					unitsNew.add(unit1);
					int count = bookingApplRepository.getNumberOfBookings(unit1, d1, d2);
					if(count != 0){
						isAvailable = false;
						break;
					}		
				}//DONE
				else if (unitNew.isPresent()&&(unitsOld.contains(unitNew.get()))){
				Unit unit1 = unitNew.get();
				    System.out.println("elseeee");
		            BookingAppl b = bookingApplRepository.getBookingEntity(unit1, d1, d2);
		            Unit unitFromB = b.getUnit();
		            unitsNew.add(unit1);
		            System.out.println(b.getId());
		            System.out.println(unitsOld.iterator().next().getId());
		            if(!unitsOld.contains(unitFromB)){
		            	isAvailable = false;
		            	break;
		            }
				}
				
				
				System.out.println("5");
				}//DONE!!! 4/10/2016
				System.out.println(isAvailable);
				if(isAvailable){					
				for(BookingAppl b1: bookingList){
					Unit unit = b1.getUnit();
					Set<BookingAppl> bookingsFromUnit = unit.getBookings();
					bookingsFromUnit.remove(b1);
					bookingList.remove(b1);
					bookingApplRepository.delete(b1);
				}
				System.out.println("6");
				//need to save the new units?
				for(Unit i : unitsNew){
					BookingAppl newBooking = new BookingAppl();
					newBooking.setEvent_start_date_time(event_start_date);
					System.out.println(event_start_date);
					newBooking.setEvent_end_date_time(event_end_date);
					newBooking.setUnit(i);
				    newBooking.setEvent(event);
				    Set<BookingAppl> bookingsFromUnit = i.getBookings();
				    bookingsFromUnit.add(newBooking);
				    bookingList.add(newBooking);	
				    unitRepository.save(i);
				    bookingApplRepository.save(newBooking);
				}
			
				System.out.println("end of second for loop");				
				event.setEvent_title(event_title);
				System.out.println("after title");
				event.setEvent_content(event_content);
				event.setEvent_description(event_description);
				event.setEvent_approval_status(status);
		        event.setBookings(bookingList);
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
	public boolean deleteEvent(ClientOrganisation client, long id) {
		System.out.println(id);
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
        try{		
			Optional<Event> event1 = getEventById(id);
			Event event = null;
			if(event1.isPresent()){		
				System.out.println("inside TRY");				
				 event = event1.get();
				 User eventOrg = event.getEventOrg();
				 for(User u: eventOrgs){
					 Set<Role> roles = u.getRoles();
					   for(Role r: roles){
					    if(r.getName().equals("ROLE_EXTEVE") && u.equals(eventOrg))
					    doesHave = true;
					   }
				}
				if(!doesHave)
					return false;
				Set<BookingAppl> bookings = event.getBookings();
				for(BookingAppl b: bookings){				
					Date d1 = b.getEvent_start_date_time();
					Date d2 = b.getEvent_start_date_time();
					Unit unit = b.getUnit();
					Set<BookingAppl> bookings1 = unit.getBookings();
					bookings1.remove(b);
					unit.setBookings(bookings1);
					unitRepository.save(unit);
					bookings.remove(b);
					bookingApplRepository.delete(b);
				}
				event.setBookings(new HashSet<BookingAppl>());
			    User eventOrg1 = event.getEventOrg();
				Set<Event> events = eventOrg1.getEvents();
				events.remove(event);
				eventOrg1.setEvents(events);
				event.setEvent_approval_status("cancelled");
				userRepository.save(eventOrg1);		   
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
	public boolean createEvent(ClientOrganisation client, User eventOrg, String unitsId, String event_title, String event_content, String event_description, String status,
			Date event_start_date, Date event_end_date, String filePath) {
		Event event = new Event();
		Set<BookingAppl> bookings = new HashSet<BookingAppl>();
		event.setBookings(bookings);
		String[] units = unitsId.split(" ");
		System.out.println(units[0]);
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_EXTEVE") && u.equals(eventOrg))
			    doesHave = true;
			   }
		}
		if(!doesHave)
			return false;
		Date d1 = event_start_date;
		Date d2 = event_end_date;
		if(d1.compareTo(d2)>0)
			return false;
		boolean isAvailable = true;
		for(int i = 0; i<units.length; i ++){
			long uId = Long.valueOf(units[i]);
			Optional<Unit> unit1 = unitRepository.getUnitById(uId);
			if(unit1.isPresent()&&isAvailable){
				Unit unit = unit1.get();
				if(!checkUnit(client, unit.getId()))
					return false;
				int count = bookingApplRepository.getNumberOfBookings(unit, d1, d2);
				if(count != 0){
					isAvailable = false;
					break;
				}
			}
		}
		if(isAvailable){
			//eventRepository.save(event);
			for(int i = 0; i<units.length; i ++){
				long uId = Long.valueOf(units[i]);
				Optional<Unit> unit1 = unitRepository.getUnitById(uId);
				if(unit1.isPresent()&&isAvailable){
					Unit unit = unit1.get();	
					BookingAppl booking = new BookingAppl();
					booking.setEvent_start_date_time(event_start_date);
					System.out.println("inside the controller");
					System.out.println(event_start_date);
					System.out.println(booking.getEvent_start_date_time());
					booking.setEvent_end_date_time(event_end_date);
					booking.setUnit(unit);
					booking.setEvent(event);
					bookings.add(booking);
					//bookingApplRepository.save(booking);
			        Set<BookingAppl> bookingList = unit.getBookings();
			        bookingList.add(booking);
			        unit.setBookings(bookingList);		
					//unitRepository.save(unit);
				}
			}
			event.setEvent_title(event_title);
			event.setEvent_content(event_content);
			event.setEvent_description(event_description);
			event.setEvent_approval_status(status);
			event.setFilePath(filePath);
			event.setBookings(bookings);
			eventRepository.save(event);
			Set<Event> events = eventOrg.getEvents();
			events.add(event);
			eventOrg.setEvents(events);
			event.setEventOrg(eventOrg);
			eventRepository.save(event);
			userRepository.save(eventOrg);
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
		Set<BookingAppl> bookingList = event.getBookings();
		for(BookingAppl b : bookingList)
			unitsId = unitsId + b.getUnit().getId() +" ";
	     }
		}catch(Exception e){
		
		}
     return unitsId;
	}

	@Override
	public boolean checkUnit(ClientOrganisation client, long unitId) {
		Set<Building> buildings = client.getBuildings();
		boolean doesHave = false;
		for(Building b: buildings){
			Set<Level> levels = b.getLevels();
			for(Level l : levels){
				Optional<Unit> unit1 = unitRepository.getUnitById(unitId);
				if(unit1.isPresent()&& l.getUnits().contains(unit1.get())){
					doesHave = true;
					break;
				}
			}
		}
		return doesHave;
	}

	@Override
	public boolean checkEvent(ClientOrganisation client, long eventId) {
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		try{
			Optional<Event> event1 = getEventById(eventId);
			if(event1.isPresent()){
				Event event = event1.get();
		     for(User u: eventOrgs){
			 Set<Role> roles = u.getRoles();
			   for(Role r: roles){
			    if(r.getName().equals("ROLE_EXTEVE") && u.getEvents().contains(event)){
			    doesHave = true;
			    break;
			    }
			   }
		    }
			}
		}catch(Exception e){
			return false;
			}
		return doesHave;
	}
}
