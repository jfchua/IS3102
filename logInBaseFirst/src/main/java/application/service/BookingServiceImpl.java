package application.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.BookingAppl;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Level;
import application.entity.Role;
import application.entity.Unit;
import application.entity.User;
import application.repository.BookingApplRepository;
import application.repository.EventRepository;
import application.repository.UnitRepository;
import application.repository.UserRepository;
@Service
public class BookingServiceImpl implements BookingService {
	    private final UserRepository userRepository;
		private final EventRepository eventRepository;
		//private final EventOrganizerRepository eventOrganizerRepository;
		private final UnitRepository unitRepository;
		private final BookingApplRepository bookingApplRepository;
		private static final Logger LOGGER = LoggerFactory.getLogger(EventServiceImpl.class);

		@Autowired
		public BookingServiceImpl(EventRepository eventRepository, UserRepository userRepository,UnitRepository unitRepository, BookingApplRepository bookingApplRepository){
			//super();
			this.eventRepository = eventRepository;
			//this.eventOrganizerRepository = eventOrganizerRepository;
			this.unitRepository = unitRepository;
			this.userRepository = userRepository;
			this.bookingApplRepository = bookingApplRepository;
		}
	
	@Override
	public boolean deleteBooking(ClientOrganisation client, long id) {
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		  try{		
				Optional<BookingAppl> booking1 = getBookingById(id);
				BookingAppl booking = null;
				if(booking1.isPresent()){		
					System.out.println("inside TRY");				
					 booking =booking1.get();
					 Event event = booking.getEvent();
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
					Unit unit = booking.getUnit();
					Set<BookingAppl> bookings1 = unit.getBookings();
						bookings.remove(booking);
						event.setBookings(bookings);
						bookings1.remove(booking);
						unit.setBookings(bookings1);
						unitRepository.save(unit);
						eventRepository.save(event);
						bookingApplRepository.delete(booking);
				}
				}catch(Exception e){
					return false;
				}
				return true;
	}

	@Override
	public boolean updateBooking(ClientOrganisation client, long bookingId, long unitId) {
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		  try{		
			    //boolean isAvailable = true;
				Optional<BookingAppl> booking1 = getBookingById(bookingId);
				Optional<Unit> unit1 = Optional.ofNullable(unitRepository.findOne(unitId));
				BookingAppl booking = null;
				Unit unit = null;
				if(booking1.isPresent()&&unit1.isPresent()){		
					System.out.println("inside TRY");				
					 booking =booking1.get();
					 unit = unit1.get();
					 Event event = booking.getEvent();
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
			        if(!checkUnit(client, unitId))
			        	return false;
			        if(booking.getUnit().getId().equals(unitId))
			        	return true;
			        Date d1 = booking.getEvent_start_date_time();
			        Date d2 = booking.getEvent_end_date_time();
			        int count = bookingApplRepository.getNumberOfBookings(unitId, d1, d2);
			        if(count !=0)
			        	return false;
			        else{
			        	Unit unitOld = booking.getUnit();
			        	Set<BookingAppl> bookings2 = unitOld.getBookings();
			        	bookings2.remove(booking);
			        	unitRepository.flush();
			        	Set<BookingAppl> bookings = event.getBookings();
			        	bookings.remove(booking);
			        	eventRepository.flush();
			        	bookingApplRepository.delete(booking);
			        	//booking.setUnit(unit);	
					    Set<BookingAppl> bookings1 = unit.getBookings();
						BookingAppl bookingNew = new BookingAppl();
						bookingNew.setEvent(event);
						bookingNew.setUnit(unit);
						bookingNew.setEvent_start_date_time(d1);
						bookingNew.setEvent_end_date_time(d2);
						bookingApplRepository.save(bookingNew);
						bookings1.add(booking);
						bookings.add(booking);
						unit.setBookings(bookings1);
						event.setBookings(bookings);
						unitRepository.flush();
						eventRepository.flush();
				}
				}
				}catch(Exception e){
					return false;
				}
				return true;
	}

	@Override
	public Optional<BookingAppl> getBookingById(long id) {
		LOGGER.debug("Getting booking={}", id);
		return Optional.ofNullable(bookingApplRepository.findOne(id));
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
	public boolean checkBooking(ClientOrganisation client, long id) {
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		 try{		
				Optional<BookingAppl> booking1 = getBookingById(id);
				if(booking1.isPresent()){
					BookingAppl booking = booking1.get();
					Event event = booking.getEvent();				
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
		return true;
	}

	@Override
	public long getEventId(long id) {
		 try{		
				Optional<BookingAppl> booking1 = getBookingById(id);
				if(booking1.isPresent()){
					BookingAppl booking = booking1.get();
					Event event = booking.getEvent();
					return event.getId();
				}
				else 
					return 0;
		 }catch(Exception e){
				return 0;
			}
	}

	@Override
	public long getUnitId(long id) {
		 try{		
				Optional<BookingAppl> booking1 = getBookingById(id);
				if(booking1.isPresent()){
					BookingAppl booking = booking1.get();
					Unit unit = booking.getUnit();
					return unit.getId();
				}
				else 
					return 0;
		 }catch(Exception e){
				return 0;
			}
	}
}
