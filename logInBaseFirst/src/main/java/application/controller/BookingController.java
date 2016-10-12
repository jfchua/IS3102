package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

//import org.hibernate.metamodel.source.annotations.xml.mocker.SchemaAware.SecondaryTableSchemaAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.domain.*;
import application.domain.validator.EventCreateFormValidator;
import application.service.user.BookingService;
import application.service.user.EventExternalService;
import application.service.user.EventOrganizerService;
import application.service.user.UserService;
//import application.service.user.EventService;
//import application.service.user.UserService;

@Controller
@RequestMapping("/booking")
public class BookingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
	private final EventExternalService eventExternalService;
	private final BookingService bookingService;
	private final UserService userService;
	private final EventCreateFormValidator eventCreateFormValidator;
	private JSONParser parser = new JSONParser();
	
	@Autowired
	public BookingController(EventExternalService eventService, BookingService bookingService,
			UserService userService, EventCreateFormValidator eventCreateFormValidator) {
		super();
		this.eventExternalService = eventService;
		this.bookingService = bookingService;
		this.userService = userService;
		this.eventCreateFormValidator = eventCreateFormValidator;
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
			// Each object (building) will contain... long id, collection of levels.
				@RequestMapping(value = "/getBooking/{id}", method = RequestMethod.GET)
				@ResponseBody
				public String getBooking(@PathVariable("id") String bId, HttpServletRequest rq) {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return null; 
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();
						long id = Long.parseLong(bId);
						boolean bl = bookingService.checkBooking(client, id);
						BookingAppl booking= bookingService.getBookingById(id).get();
						System.out.println("getting event now");
						if(bl){
						Gson gson2 = new GsonBuilder()
								.setExclusionStrategies(new ExclusionStrategy() {
									public boolean shouldSkipClass(Class<?> clazz) {
										return (clazz == Area.class)||(clazz == Unit.class)||(clazz == Event.class);
									}
									/**
									 * Custom field exclusion goes here
									 */
									@Override
									public boolean shouldSkipField(FieldAttributes f) {
										//TODO Auto-generated method stub
										return false;
									}
								})
								/**
								 * Use serializeNulls method if you want To serialize null values 
								 * By default, Gson does not serialize null values
								 */
								.serializeNulls()
								.create();
						JSONObject obj = new JSONObject();
						obj.put("id", booking.getId());
						System.out.println(booking.getId());
						obj.put("unit", bookingService.getUnitId(id));
					    obj.put("event", bookingService.getEventId(id));
					    obj.put("event_start_date", String.valueOf(booking.getEvent_start_date_time()));
					    System.out.println(booking.getEvent_start_date_time());
					    obj.put("event_end_date", String.valueOf(booking.getEvent_end_date_time()));
					    System.out.println(booking.getEvent_end_date_time());
					    System.out.println(obj.toString());
						return obj.toString();
						//return json;
						}else
							return "cannot fetch";
					}
					catch (Exception e){
						return "cannot fetch";
					}
				}	
			
	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
		// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
				// Each object (building) will contain... long id, .
					@RequestMapping(value = "/viewAllBookings/{id}",  method = RequestMethod.GET)
					@ResponseBody
					public String viewAllBookings(@PathVariable("id") String bId, HttpServletRequest rq) {
					    System.out.println("start view");
					    Principal principal = rq.getUserPrincipal();
					    Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
						if ( !eventOrg1.isPresent() ){
							return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
						}
						try{
						//EventOrganizer eventOrg = eventOrg1.get();	
						User eventOrg = eventOrg1.get();
						ClientOrganisation client = eventOrg.getClientOrganisation();
						System.out.println(eventOrg.getId());
						long id = Long.parseLong(bId);
						Set<BookingAppl> bookings = eventExternalService.getBookings(client,id);	
						System.out.println("There are " + bookings.size() + " events under this organizer");
						//Set<BookingAppl> bookingsWithUnits=new HashSet<BookingAppl>();
						//Gson gson = new Gson();
						//String json = gson.toJson(levels);
					    //System.out.println("Returning levels with json of : " + json);
						//return json;

						Gson gson2 = new GsonBuilder()
							    .setExclusionStrategies(new ExclusionStrategy() {
							        public boolean shouldSkipClass(Class<?> clazz) {
							            return (clazz == Event.class)||(clazz == Area.class)||(clazz == Unit.class)||(clazz == MaintenanceSchedule.class);
							        }

							        /**
							          * Custom field exclusion goes here
							          */

									@Override
									public boolean shouldSkipField(FieldAttributes f) {
										//TODO Auto-generated method stub
										return false;
												//(f.getDeclaringClass() == Level.class && f.getUnits().equals("units"));
									}

							     })
							    /**
							      * Use serializeNulls method if you want To serialize null values 
							      * By default, Gson does not serialize null values
							      */
							    .serializeNulls()
							    .create();			    
					    String json = gson2.toJson(bookings);
					    System.out.println(json);
					    return json;
						}
						catch (Exception e){
							return "cannot fetch";
						}
					}
	               @PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
					// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
					// Each object (building) will contain... long id, .
						@RequestMapping(value = "/viewAllSelectedUnits/{id}",  method = RequestMethod.GET)
						@ResponseBody
						public String viewAllSelectedUnits(@PathVariable("id") String bId, HttpServletRequest rq) {
						    System.out.println("start view");
						    Principal principal = rq.getUserPrincipal();
						    Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
							if ( !eventOrg1.isPresent() ){
								return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
							}
							try{
							//EventOrganizer eventOrg = eventOrg1.get();	
							User eventOrg = eventOrg1.get();
							ClientOrganisation client = eventOrg.getClientOrganisation();
							System.out.println(eventOrg.getId());
							long id = Long.parseLong(bId);
							Set<BookingAppl> bookings = eventExternalService.getBookings(client,id);	
							System.out.println("There are " + bookings.size() + " events under this organizer");
							//Set<BookingAppl> bookingsWithUnits=new HashSet<BookingAppl>();
							//Gson gson = new Gson();
							//String json = gson.toJson(levels);
						    //System.out.println("Returning levels with json of : " + json);
							//return json;
							Set<Unit> units = new HashSet<Unit>();
							for(BookingAppl booking:bookings){									
								booking.getUnit().setBookings(null);;
								booking.getUnit().setMaintenanceSchedule(null);
								booking.getUnit().setSquare(null);
								booking.getUnit().setLevel(null);
								units.add(booking.getUnit());
								}
							Gson gson = new Gson();
							String json = gson.toJson(units);
						    return json;
							}
							catch (Exception e){
								return "cannot fetch";
							}
						}
						
					//This method takes in a String which is the ID of the event to be deleted
					// Call $http.post(URL,(String)id);
				/*	@RequestMapping(value = "/deleteBooking", method = RequestMethod.POST)
					@ResponseBody
					public ResponseEntity<Void> deleteEvent(@RequestBody String bookingJSON, HttpServletRequest rq) {
						Principal principal = rq.getUserPrincipal();
					 Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());	
					   if ( !eventOrg1.isPresent() ){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
						}
						try{	
					    User eventOrg = eventOrg1.get();
					    ClientOrganisation client = eventOrg.getClientOrganisation();
							System.out.println("Start deleting");
							Object obj = parser.parse(bookingJSON);
							JSONObject jsonObject = (JSONObject) obj;
							long bId = (Long)jsonObject.get("bookingId");
							System.out.println(bId);	
							boolean bl=bookingService.deleteBooking(client, bId);
							if(!bl){
								System.out.println("cannot delete booking");
								return new ResponseEntity<Void>(HttpStatus.CONFLICT);	
							}			
						}
						catch (Exception e){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);
						}
						return new ResponseEntity<Void>(HttpStatus.OK);
					}		*/
					
	              @PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
					@RequestMapping(value = "/deleteBooking/{id}", method = RequestMethod.POST)
					@ResponseBody
					public ResponseEntity<Void> deleteBooking(@PathVariable("id") String bId, HttpServletRequest rq) {
						Principal principal = rq.getUserPrincipal();
						Optional<User> usr = userService.getUserByEmail(principal.getName());
						if ( !usr.isPresent() ){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT); 
						}
						try{
						    ClientOrganisation client = usr.get().getClientOrganisation();
							long id = Long.parseLong(bId);
							boolean bl = bookingService.deleteBooking(client, id);
							//BookingAppl booking= bookingService.getBookingById(id).get();
							System.out.println("delete booking" + bl);
							if(!bl){
								System.out.println("cannot delete booking");
								return new ResponseEntity<Void>(HttpStatus.CONFLICT);
							}
						}
						catch (Exception e){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);
						}
						return new ResponseEntity<Void>(HttpStatus.OK);
					}					
}

