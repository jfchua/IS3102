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

import application.domain.Area;
import application.domain.ClientOrganisation;
import application.domain.Event;
	import application.domain.EventCreateForm;
	import application.domain.EventOrganizer;
import application.domain.Message;
import application.domain.Role;
import application.domain.ToDoTask;
import application.domain.Unit;
import application.domain.User;
import application.domain.validator.EventCreateFormValidator;
	import application.service.user.EventOrganizerService;
	import application.service.user.EventService;
import application.service.user.MessageService;
import application.service.user.UserService;
	
	@Controller
	@RequestMapping("/eventManager")
	public class EventController {
		private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
		private final EventService eventService;
		private final MessageService messageService;
		private final EventOrganizerService eventOrganizerService;
		private final UserService userService;
		private JSONParser parser = new JSONParser();
		
		
		@Autowired
		public EventController(EventService eventService, EventOrganizerService eventOrganizerService, 
				UserService userService, MessageService messageService) {
			super();
			this.eventService = eventService;
			this.userService = userService;
			this.eventOrganizerService = eventOrganizerService;
			this.messageService = messageService;
		}
		
		// Call this method using $http.get and you will get a JSON format containing an array of event objects.
			// Each object (building) will contain... long id, collection of levels.
				@RequestMapping(value = "/getEvent/{id}", method = RequestMethod.GET)
				@ResponseBody
				public String getEvent(@PathVariable("id") String eventId, HttpServletRequest rq) {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return null; 
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
										return (clazz == User.class)||(clazz == Unit.class)||(clazz == Area.class);
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
						return json;
						}else
							return "cannot fetch";
					}
					catch (Exception e){
						return "cannot fetch";
					}
				}
				
				
				// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
							// Each object (building) will contain... long id, .
								@RequestMapping(value = "/viewAllEvents",  method = RequestMethod.GET)
								@ResponseBody
								public String viewAllEvents(HttpServletRequest rq) {
									Principal principal = rq.getUserPrincipal();
									Optional<User> usr = userService.getUserByEmail(principal.getName());
									if ( !usr.isPresent() ){
										return null; 
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
										            return (clazz == User.class)||(clazz==Unit.class)||(clazz==Area.class);
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
								    return json;
									}
									catch (Exception e){
										return "cannot fetch";
									}
									//return new ResponseEntity<Void>(HttpStatus.OK);
								}
								
								
								// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
								// Each object (building) will contain... long id, .
									@RequestMapping(value = "/viewApprovedEvents",  method = RequestMethod.GET)
									@ResponseBody
									public String viewApprovedEvents(HttpServletRequest rq) {
										Principal principal = rq.getUserPrincipal();
										Optional<User> usr = userService.getUserByEmail(principal.getName());
										if ( !usr.isPresent() ){
											return null; 
										}
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
											            return (clazz == User.class)||(clazz==Unit.class)||(clazz==Area.class);
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
									    return json;
										}
										catch (Exception e){
											return "cannot fetch";
										}
										//return new ResponseEntity<Void>(HttpStatus.OK);
									}
									// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
									// Each object (building) will contain... long id, .
										@RequestMapping(value = "/viewToBeApprovedEvents",  method = RequestMethod.GET)
										@ResponseBody
										public String viewToBeApprovedEvents(HttpServletRequest rq) {
											Principal principal = rq.getUserPrincipal();
											Optional<User> usr = userService.getUserByEmail(principal.getName());
											if ( !usr.isPresent() ){
												return null; 
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
												            return (clazz == User.class)||(clazz==Unit.class)||(clazz==Area.class);
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
										    return json;
											}
											catch (Exception e){
												return "cannot fetch";
											}
											//return new ResponseEntity<Void>(HttpStatus.OK);
										}
										//This method takes in a String which is the ID of the event to be deleted
										// Call $http.post(URL,(String)id);
										@RequestMapping(value = "/deleteEvent", method = RequestMethod.POST)
										@ResponseBody
										public ResponseEntity<Void> deleteEvent(@RequestBody String eventJSON, HttpServletRequest rq) {
											Principal principal = rq.getUserPrincipal();
											Optional<User> usr = userService.getUserByEmail(principal.getName());
											if ( !usr.isPresent() ){
												return null; 
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
											}
											catch (Exception e){
												return new ResponseEntity<Void>(HttpStatus.CONFLICT);
											}
											return new ResponseEntity<Void>(HttpStatus.OK);
										}						
				
										//This method takes in a JSON format which contains an object with 5 attributes
										//Long/String id, int levelNum, int length, int width, String filePath
										//Call $httpPost(Url,JSONData);
										@RequestMapping(value = "/approveEvent", method = RequestMethod.POST)
										@ResponseBody
										public ResponseEntity<Void> approveEvent(@RequestBody String eventJSON, HttpServletRequest rq) {
											System.out.println("start approving not yet");
											Principal principal = rq.getUserPrincipal();
											System.out.println(principal.getName());
											Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
											if ( !eventOrg1.isPresent() ){
												return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
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
											catch (Exception e){
												return new ResponseEntity<Void>(HttpStatus.CONFLICT);
											}
											return new ResponseEntity<Void>(HttpStatus.OK);
										}	
										
										//This method takes in a JSON format which contains an object with 5 attributes
										//Long/String id, int levelNum, int length, int width, String filePath
										//Call $httpPost(Url,JSONData);
										@RequestMapping(value = "/updateEventStatus", method = RequestMethod.POST)
										@ResponseBody
										public ResponseEntity<Void> updateEventStatus(@RequestBody String eventJSON, HttpServletRequest rq) {
											System.out.println("start updating not yet");
											Principal principal = rq.getUserPrincipal();
											Optional<User> usr = userService.getUserByEmail(principal.getName());
											if ( !usr.isPresent() ){
												return null; 
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
											catch (Exception e){
												return new ResponseEntity<Void>(HttpStatus.CONFLICT);
											}
											return new ResponseEntity<Void>(HttpStatus.OK);
										}		
			
										// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
										// Each object (building) will contain... long id, .
											@RequestMapping(value = "/viewEventOrganizers",  method = RequestMethod.GET)
											@ResponseBody
											public String viewEventOrganizers(HttpServletRequest rq) {
												//Principal principal = rq.getUserPrincipal();
												//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
												Principal principal = rq.getUserPrincipal();
												System.out.println(principal.getName());
												Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
												//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
												if ( !eventOrg1.isPresent() ){
													return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
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
											    return json;
												}
												catch (Exception e){
													return "cannot fetch";
												}
												//return new ResponseEntity<Void>(HttpStatus.OK);
											}
									
				
	}