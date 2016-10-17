package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.EventCreateForm;
import application.entity.EventCreateFormValidator;
import application.entity.EventOrganizer;
import application.entity.Message;
import application.entity.PaymentPlan;
import application.entity.Role;
import application.entity.ToDoTask;
import application.entity.Unit;
import application.entity.User;
import application.exception.EventNotFoundException;
import application.exception.UserNotFoundException;
import application.service.EventOrganizerService;
import application.service.EventService;
import application.service.MessageService;
import application.service.UserService;

@Controller
@RequestMapping("/eventManager")
public class EventController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private final EventService eventService;
	private final MessageService messageService;
	private final EventOrganizerService eventOrganizerService;
	private final UserService userService;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();


	@Autowired
	public EventController(EventService eventService, EventOrganizerService eventOrganizerService, 
			UserService userService, MessageService messageService) {
		super();
		this.eventService = eventService;
		this.userService = userService;
		this.eventOrganizerService = eventOrganizerService;
		this.messageService = messageService;
	}

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/getEvent/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getEvent(@PathVariable("id") String eventId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();				   
			long id = Long.parseLong(eventId);
			Event event= eventService.getEventById(id).get();
			boolean bl = eventService.checkEvent(client, id);
			if(bl){
				Gson gson2 = new GsonBuilder()
						.setExclusionStrategies(new ExclusionStrategy() {
							public boolean shouldSkipClass(Class<?> clazz) {
								return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
				String json = gson2.toJson(event);
				System.out.println("EVENT IS " + json);
				return new ResponseEntity<String>(json,HttpStatus.OK);
			}else
				return new ResponseEntity<String>(geeson.toJson("Server error in getting event"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting event"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewAllEvents",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewAllEvents(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("start view");
			Set<Event> events = eventService.getAllEvents(client);
			System.err.println("There are " + events.size() + " events");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			String json = gson2.toJson(events);
			String json2 = gson2.toJson("Server error in getting all the events");
			System.out.println(json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewApprovedEvents",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewApprovedEvents(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Event> events = eventService.getAllApprovedEvents(client);
			System.err.println("There are " + events.size() + " approved events");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			String json = gson2.toJson(events);
			System.out.println(json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewToBeApprovedEvents",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewToBeApprovedEvents(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Event> events = eventService.getAllToBeApprovedEvents(client);
			System.err.println("There are " + events.size() + " to be approved events");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			String json = gson2.toJson(events);
			System.out.println(json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}

		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	//This method takes in a String which is the ID of the event to be deleted
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteEvent", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteEvent(@RequestBody String eventJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting event"),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("Start deleting");
			//Principal principal = rq.getUserPrincipal();
			//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Object obj = parser.parse(eventJSON);
			JSONObject jsonObject = (JSONObject) obj;
			//long buildingId = (Long)jsonObject.get("buildingId");
			long eventId = (Long)jsonObject.get("eventId");
			System.out.println("eventId");	
			//eventExternalService.updateEventOrganizerForDelete(eventId);
			boolean bl=eventService.deleteEvent(client, eventId);
			System.out.println(bl);	
			if(!bl){
				return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}						

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	//This method takes in a JSON format which contains an object with 5 attributes
	//Long/String id, int levelNum, int length, int width, String filePath
	//Call $httpPost(Url,JSONData);
	@RequestMapping(value = "/approveEvent", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> approveEvent(@RequestBody String eventJSON, HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start approving not yet");
		Principal principal = rq.getUserPrincipal();
		System.out.println(principal.getName());
		Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
		if ( !eventOrg1.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			User eventManager = eventOrg1.get();
			ClientOrganisation client = eventManager.getClientOrganisation();
			System.out.println("start approving");
			Object obj = parser.parse(eventJSON);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println("start lala");
			long eventId = (Long)jsonObject.get("eventId");
			System.out.println(eventId);
			boolean bl=eventService.approveEvent(client, eventId);
			System.out.println(bl);										
			String subject = "Approval of Event";
			String msg = "Event ID " +eventId +" is approved.";
			Set<User> finance = userService.getFinanceManagers(client);						
			for(User u: finance){
				messageService.sendMessage(eventManager, u, subject, msg);
			}
			subject = "Approval of Event";
			msg = "Event ID " +eventId +" with ticket to be issued is approved.";
			Set<User> ticket = userService.getTicketManagers(client);
			for(User u: ticket){
				messageService.sendMessage(eventManager, u, subject, msg);
			}
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}	

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	//This method takes in a JSON format which contains an object with 5 attributes
	//Long/String id, int levelNum, int length, int width, String filePath
	//Call $httpPost(Url,JSONData);
	@RequestMapping(value = "/updateEventStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateEventStatus(@RequestBody String eventJSON, HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start updating not yet");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting event"),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("start updating");
			Object obj = parser.parse(eventJSON);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println("start lala");
			//long buildingId = (Long)jsonObject.get("buildingId");
			long eventId = (Long)jsonObject.get("id");
			String status = (String)jsonObject.get("event_approval_status");
			System.out.println(eventId);
			boolean bl=eventService.updateEventStatusForPayment(client, eventId, status);
			System.out.println(bl);
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}		

	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewEventOrganizers",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewEventOrganizers(HttpServletRequest rq) throws UserNotFoundException {
		//Principal principal = rq.getUserPrincipal();
		//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
		Principal principal = rq.getUserPrincipal();
		System.out.println(principal.getName());
		Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
		//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
		if ( !eventOrg1.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			//EventOrganizer eventOrg = eventOrg1.get();
			User eventOrg = eventOrg1.get();
			Set<User> eventOrgs = userService.getExternalUsers(eventOrg.getClientOrganisation());
			System.err.println("There are " + eventOrgs.size() + " event organizers");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz==Event.class)||(clazz==Role.class)||(clazz==ToDoTask.class)||(clazz==Message.class)
									||(clazz==ClientOrganisation.class);
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
			String json = gson2.toJson(eventOrgs);
			System.out.println(json);
			return new ResponseEntity<String>(json,HttpStatus.OK);

		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@PreAuthorize("hasAnyAuthority('ROLE_EVENT')")	
	@RequestMapping(value = "/getNotifications/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getNotifications(@PathVariable("id") String userId, HttpServletRequest rq) {
		try{

			long id = Long.parseLong(userId);
			User eventOrg= userService.getUserById(id).get();
			Set<Message> msg = eventOrg.getMessagesReceived();
			System.out.println(msg.size());
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class);
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
			String json = gson2.toJson(msg);
			System.out.println("NOTIFICATIONS ARE " + json);
			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}


}