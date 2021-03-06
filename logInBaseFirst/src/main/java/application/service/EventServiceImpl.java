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
import application.entity.Category;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.EventCreateForm;
import application.entity.EventOrganizer;
import application.entity.Message;
import application.entity.Payment;
import application.entity.PaymentPlan;
import application.entity.Role;
import application.entity.Square;
import application.entity.Ticket;
import application.entity.Unit;
import application.entity.User;
import application.enumeration.ApprovalStatus;
import application.enumeration.PaymentStatus;
import application.enumeration.Subscription;
import application.exception.EventNotFoundException;
import application.repository.AreaRepository;
import application.repository.BookingApplRepository;
import application.repository.CategoryRepository;
import application.repository.EventOrganizerRepository;
import application.repository.EventRepository;
import application.repository.PaymentPlanRepository;
import application.repository.PaymentRepository;
import application.repository.SquareRepository;
import application.repository.TicketRepository;
import application.repository.UnitRepository;
import application.repository.UserRepository;


@Service
public class EventServiceImpl implements EventService {
	private final EventRepository eventRepository;
	private final UnitRepository unitRepository;
	private final BookingApplRepository bookingApplRepository;
	private final AreaRepository areaRepository;
	private final SquareRepository squareRepository;
	private final UserRepository userRepository;
	private final PaymentPlanRepository paymentPlanRepository;
	private final PaymentRepository paymentRepository;
	private final CategoryRepository categoryRepository;
	private final TicketRepository ticketRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	public EventServiceImpl(EventRepository eventRepository, UnitRepository unitRepository,
			UserRepository userRepository, BookingApplRepository bookingRepository,
			AreaRepository areaRepository, SquareRepository squareRepository, 
			PaymentPlanRepository paymentPlanRepository, PaymentRepository paymentRepository,
			CategoryRepository categoryRepository,  TicketRepository ticketRepository) {
		super();
		this.eventRepository = eventRepository;
		this.unitRepository = unitRepository;
		this.userRepository=userRepository;
		this.bookingApplRepository = bookingRepository;
		//this.eventOrganizerRepository = eventOrganizerRepository;
		this.areaRepository= areaRepository;
		this.squareRepository =squareRepository;
		this.paymentPlanRepository = paymentPlanRepository;
		this.paymentRepository =paymentRepository;
		this.categoryRepository= categoryRepository;
		this.ticketRepository= ticketRepository;

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
				for(Event e : events){
					/*	Set<BookingAppl> bookings=e.getBookings();
					for(BookingAppl booking:bookings){
						booking.setAreas(null);
						booking.getUnit().setAreas(null);
						booking.getUnit().setBookings(null);
						booking.getUnit().setLevel(null);
						booking.getUnit().setMaintenanceSchedule(null);
						booking.getUnit().setUnitAttributeValues(null);
					}
					Set<Category> cats=e.getCategories();
					for(Category cat: cats){
						cat.setEvent(null);
						cat.setTickets(null);
					}
					e.setEventOrg(null);
					e.setPaymentPlan(null);
					 */
					allEvents.add(e);
				}
			}
		}
		return allEvents;
	}

	@Override
	public Set<Event> getAllEvents() {
		// TODO Auto-generated method stub
		return eventRepository.getAllEvents();
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
					if(client.getSystemSubscriptions().contains(Subscription.valueOf("FINANCE")))
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
				System.out.println("*1");
				//if(checkEvent(client, id)){
					//System.err.println(event.getCategories().isEmpty());
					//System.err.println(event.getCategories()==null);
					if(event.getCategories().size()!=0){
						System.out.println("*****5");
						Set<Category> cats = event.getCategories();
						boolean canBeDeleted = true;
						for(Category c : cats){
							Set<Ticket> tics = c.getTickets();	
							if(tics.size()!=0){
								canBeDeleted = false;
								break;
							}						
						}
						if(!canBeDeleted){
							System.err.println("Has ticket sold, event cannot be deleted");
							return false;
						}else{
							for(Category c : cats){
								c.setEvent(null);
								c.setTickets(null);
								categoryRepository.delete(c);
								categoryRepository.flush();		
								}
								System.err.println("*******7");
									
							}
						}
						if(event.getBookings().size()!=0){
							System.out.println("**2");
							Set<BookingAppl> bookings=event.getBookings();
							System.out.println("**3333");
							for(BookingAppl b: bookings){	
								if(b.getAreas().size()!=0){
									Set<Area> areas = b.getAreas();
									System.out.println("**2222");
									for(Area a : areas){
										System.out.println("***3");
										Square sq = a.getSquare();
										squareRepository.delete(sq);
										squareRepository.flush();
										a.setBooking(null);
										a.setSquare(null);
										areaRepository.delete(a);
										areaRepository.flush();
										System.out.println("**2555555");
									}
								}
								
								System.out.println("**277777");
								Unit unit = b.getUnit();
								Set<BookingAppl> bookings1 = unit.getBookings();
								bookings1.remove(b);
								unit.setBookings(bookings1);
								unitRepository.saveAndFlush(unit);
								b.setUnit(null);
								b.setAreas(null);
								b.setEvent(null);
								bookingApplRepository.flush();
								bookingApplRepository.delete(b);
								bookingApplRepository.flush();
							}
							//bookings.remove(b);
							System.out.println("**2999999");
							
						}
						if(event.getPaymentPlan()!= null){
							System.out.println("****4");
							PaymentPlan p1 = event.getPaymentPlan();
							Set<Payment> ps = p1.getPayments();
							for(Payment p : ps){
							
								paymentRepository.delete(p);
								paymentRepository.flush();
							}
							p1.setPayments(null);
							p1.setEvent(null);
					
							paymentPlanRepository.delete(p1);
							paymentPlanRepository.flush();
						}
						System.out.println("**564636432");
						//eventRepository.flush();
						User eventOrg1 = event.getEventOrg();
						Set<Event> eventsUser = eventOrg1.getEvents();
						eventsUser.remove(event);
						eventOrg1.setEvents(eventsUser);
						userRepository.saveAndFlush(eventOrg1);
						event.setEventOrg(null);
						event.setBookings(null);
						event.setBeacons(null);
						event.setDiscounts(null);
						event.setFeedbacks(null);
						System.out.println("**2dddddddddd");
						eventRepository.delete(event);
						eventRepository.flush();
					//}

//				else{
//					System.err.println("Error at checking event in delete event method");
//					return false;
//				}
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

