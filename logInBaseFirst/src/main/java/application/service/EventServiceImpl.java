package application.service;
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

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.EventCreateForm;
import application.entity.EventOrganizer;
import application.entity.Message;
import application.entity.Role;
import application.entity.Unit;
import application.entity.User;
import application.enumeration.ApprovalStatus;
import application.enumeration.PaymentStatus;
import application.exception.EventNotFoundException;
import application.repository.BookingApplRepository;
import application.repository.EventOrganizerRepository;
import application.repository.EventRepository;
import application.repository.UnitRepository;
import application.repository.UserRepository;


@Service
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final UnitRepository unitRepository;
    private final BookingApplRepository bookingApplRepository;
    private final UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	public EventServiceImpl(EventRepository eventRepository, UnitRepository unitRepository,
			UserRepository userRepository, BookingApplRepository bookingRepository) {
		super();
		this.eventRepository = eventRepository;
		this.unitRepository = unitRepository;
		this.userRepository=userRepository;
		this.bookingApplRepository = bookingRepository;
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
	public Set<Event> getAllEvents(ClientOrganisation client) {
		// TODO Auto-generated method stub
		Set<Event> allEvents = new HashSet<Event>();
		Set<User> allUsers = client.getUsers();
		for(User u: allUsers){
			Set<Event> events= u.getEvents();
			if(!events.isEmpty()){
				for(Event e : events)
					allEvents.add(e);
			}
		}
		return allEvents;
	}


	@Override
	public Set<Event> getAllApprovedEvents(ClientOrganisation client) {
		Set<Event> allEvents = getAllEvents(client);
		Set<Event> approvedEvents = new HashSet<Event>();
		for(Event ev: allEvents){
			if(String.valueOf(ev.getApprovalStatus()).equals("APPROVED"))
				approvedEvents.add(ev);	
		}
		return approvedEvents;
	}


	@Override
	public Set<Event> getAllToBeApprovedEvents(ClientOrganisation client) {
		Set<Event> allEvents = getAllEvents(client);
		Set<Event> toBeApprovedEvents = new HashSet<Event>();
		for(Event ev: allEvents){
			if(String.valueOf(ev.getApprovalStatus()).equals("PROCESSING"))
				toBeApprovedEvents.add(ev);	
		}
		return toBeApprovedEvents;
	}



	@Override
	public boolean updateEventStatusForPayment(ClientOrganisation client,long id, String status) throws EventNotFoundException {
		Optional<Event> event1 = getEventById(id);
		try{		

			if(event1.isPresent()){	
				Event event = event1.get();
				if(checkEvent(client, id)){
					event.setApprovalStatus(ApprovalStatus.valueOf(status));
					eventRepository.save(event);
				}
				else
					return false;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}


	@Override
	public boolean approveEvent(ClientOrganisation client, long id) throws EventNotFoundException {
		Optional<Event> event1 = getEventById(id);
		try{		

			if(event1.isPresent()){	
				Event event = event1.get();
				if(checkEvent(client, id)){
					event.setApprovalStatus(ApprovalStatus.valueOf("APPROVED"));
					event.setPaymentStatus(PaymentStatus.valueOf("UNPAID"));
					eventRepository.save(event);
				}
				else
					return false;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}


	@Override
	public boolean deleteEvent(ClientOrganisation client, long id) throws EventNotFoundException {
		try{		
			Optional<Event> event1 = getEventById(id);
			if(event1.isPresent()){	
				Event event = event1.get();
				if(checkEvent(client, id)){
					Set<BookingAppl> bookings = event.getBookings();
					if(!bookings.isEmpty()){
					for(BookingAppl b: bookings){				
						Unit unit = b.getUnit();
						Set<BookingAppl> bookings1 = unit.getBookings();
						bookings1.remove(b);
						unit.setBookings(bookings1);
						unitRepository.flush();
						//bookings.remove(b);
						bookingApplRepository.delete(b);
					}
					event.setBookings(new HashSet<BookingAppl>());
					}
					 User eventOrg1 = event.getEventOrg();
				     Set<Event> events = eventOrg1.getEvents();
			         events.remove(event);
				    eventOrg1.setEvents(events);
				    userRepository.flush();
					eventRepository.delete(event);
					eventRepository.flush();
				}
				else{
					System.err.println("Error at checking event in delete event method");
					return false;
				}
			}
		}
		catch (EventNotFoundException e){
			throw e;
		}
		catch(Exception e){
			System.out.println("EXCEPTION" + e.getMessage());
			return false;
		}
		return true;
	}


	@Override
	public Optional<Event> getEventById(long id) throws EventNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting event={}", id);
		Event e = eventRepository.findOne(id);
		if ( e == null ){
			System.err.println("Throwing: Event of id: " + id + " was not found" );
			throw new EventNotFoundException("Event of id: " + id + " was not found");
		}
		return Optional.ofNullable(eventRepository.findOne(id));
	}

	@Override
	public boolean checkEvent(ClientOrganisation client, long id) throws EventNotFoundException {
		Set<User> users = client.getUsers();
		boolean doesHave = false;
		Optional<Event> event1 = getEventById(id);
		try{

			if(event1.isPresent()){
				Event event = event1.get();
				for(User u: users){
					Set<Role> roles = u.getRoles();
					for(Role r: roles){
						if(r.getName().equals("ROLE_EXTEVE") && u.getEvents().contains(event)){
							doesHave = true;
							System.err.println("Does have is now true");
							break;
						}
					}
				}
			}
		}catch(Exception e){
			System.err.println("Exception at check event method" + e.getMessage());
			return false;
		}
		return doesHave;
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

