package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
//import org.hibernate.metamodel.source.annotations.xml.mocker.SchemaAware.SecondaryTableSchemaAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import application.service.user.EventExternalService;
import application.service.user.EventOrganizerService;
import application.service.user.UserService;
//import application.service.user.EventService;
//import application.service.user.UserService;

@Controller
@RequestMapping("/event")
public class EventExternalController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventExternalController.class);
	private final EventExternalService eventExternalService;
	//private final EventOrganizerService eventOrganizerService;
	private final UserService userService;
	private final EventCreateFormValidator eventCreateFormValidator;
	private JSONParser parser = new JSONParser();
	
	@Autowired
	public EventExternalController(EventExternalService eventService, UserService userService, EventCreateFormValidator eventCreateFormValidator) {
		super();
		this.eventExternalService = eventService;
		this.userService = userService;
		this.eventCreateFormValidator = eventCreateFormValidator;
	}
	
	//Security filters for inputs needs to be added
	//This method takes in a string which contains the attributes of the event to be added.
	//Call $http.post(URL,stringToAdd);
	@RequestMapping(value = "/addEvent", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addEvent(@RequestBody String eventJSON,
			HttpServletRequest rq) {
		System.out.println("start adding");
		DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("start adding");
		Principal principal = rq.getUserPrincipal();
		System.out.println(principal.getName());
		Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
		//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
		if ( !eventOrg1.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			//EventOrganizer eventOrg = eventOrg1.get();
			User eventOrg = eventOrg1.get();
			ClientOrganisation client = eventOrg.getClientOrganisation();
			System.out.println(eventOrg.getName());
			Object obj = parser.parse(eventJSON);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray units = (JSONArray)jsonObject.get("units");
            String unitsId = "";
            for(int i = 0; i < units.size(); i++){
            	JSONObject unitObj = (JSONObject)units.get(i);		
            	System.out.println(unitObj.toString());
				long unitId = (Long)unitObj.get("id");
				System.out.println(unitId);
				unitsId = unitsId+unitId + " ";
				System.out.println(unitsId);
			}
			String event_title = (String)jsonObject.get("event_title");
			String event_content = (String)jsonObject.get("event_content");
			String event_description = (String)jsonObject.get("event_description");
			String event_approval_status = (String)jsonObject.get("event_approval_status");
			Date event_start_date = sdf.parse((String)jsonObject.get("event_start_date"));
			System.out.println(event_start_date);
			Date event_end_date = sdf.parse((String)jsonObject.get("event_end_date"));
			//String event_period = (String)jsonObject.get("event_period");
			String filePath = (String)jsonObject.get("filePath");
			//long eventOrgId = (Long)jsonObject.get("id");
			
			boolean bl = eventExternalService.createEvent(client, eventOrg, unitsId, event_title, event_content, event_description, 
					event_approval_status, event_start_date, event_end_date, filePath);
			System.out.println("adding event " + event_title);
			if(!bl){
				System.out.println("cannot add event");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}			
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);	
	}	
	
	

	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
		@RequestMapping(value = "/getEvent/{id}", method = RequestMethod.GET)
		@ResponseBody
		public String getEvent(@PathVariable("id") String eventId, HttpServletRequest rq) {
			try{
			
				long id = Long.parseLong(eventId);
				Event event= eventExternalService.getEventById(id).get();
				Gson gson2 = new GsonBuilder()
						.setExclusionStrategies(new ExclusionStrategy() {
							public boolean shouldSkipClass(Class<?> clazz) {
								return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == Area.class);
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
			}
			catch (Exception e){
				return "cannot fetch";
			}
		}	
	
		

		// Call this method using $http.get and you will get a JSON format containing an array of event objects.
		// Each object (building) will contain... long id, collection of levels.
			@RequestMapping(value = "/getEvent1/{id}", method = RequestMethod.GET)
			@ResponseBody
			public String getEvent1(@PathVariable("id") String eventId, HttpServletRequest rq) {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return null; 
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();
					long id = Long.parseLong(eventId);
					boolean bl = eventExternalService.checkEvent(client, id);
					Event event= eventExternalService.getEventById(id).get();
					System.out.println("getting event now");
					if(bl){
					Gson gson2 = new GsonBuilder()
							.setExclusionStrategies(new ExclusionStrategy() {
								public boolean shouldSkipClass(Class<?> clazz) {
									return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == Area.class);
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
					obj.put("id", event.getId());
					System.out.println(event.getId());
					String unit = eventExternalService.getUnitsId(id);
					obj.put("units", unit);
					System.out.println(unit);
				    obj.put("event_title", event.getEvent_title());
				    obj.put("event_content", event.getEvent_content());
				    obj.put("event_description", event.getEvent_description());
				    obj.put("event_approval_status", event.getEvent_approval_status());
				    obj.put("event_start_date", String.valueOf(event.getEvent_start_date()));
				    obj.put("event_end_date", String.valueOf(event.getEvent_end_date()));
				    obj.put("filePath", event.getFilePath());
					//String json = gson2.toJson(event);
					//System.out.println("EVENT IS " + json);
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
		
	
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
			// Each object (building) will contain... long id, .
				@RequestMapping(value = "/viewAllEvents",  method = RequestMethod.GET)
				@ResponseBody
				public String viewAllEvents(HttpServletRequest rq) {
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
					Set<Event> events = eventExternalService.getAllEventsByOrg(client, eventOrg);
					System.out.println("There are " + events.size() + " events under this organizer");
					
					//Gson gson = new Gson();
					//String json = gson.toJson(levels);
				    //System.out.println("Returning levels with json of : " + json);
					//return json;
					
					Gson gson2 = new GsonBuilder()
						    .setExclusionStrategies(new ExclusionStrategy() {
						        public boolean shouldSkipClass(Class<?> clazz) {
						            return (clazz == User.class)||(clazz==BookingAppl.class)||(clazz == Area.class);
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
							//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
						 Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());	
						   if ( !eventOrg1.isPresent() ){
								return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
							}
							try{
							//EventOrganizer eventOrg = eventOrg1.get();	
						    User eventOrg = eventOrg1.get();
						    ClientOrganisation client = eventOrg.getClientOrganisation();
							System.out.println(eventOrg.getId());
							Set<Event> events = eventExternalService.getAllApprovedEventsByOrg(client, eventOrg);
							System.out.println("There are " + events.size() + " events under this organizer");
						
						//Gson gson = new Gson();
						//String json = gson.toJson(levels);
					    //System.out.println("Returning levels with json of : " + json);
						//return json;
						
						Gson gson2 = new GsonBuilder()
							    .setExclusionStrategies(new ExclusionStrategy() {
							        public boolean shouldSkipClass(Class<?> clazz) {
							            return (clazz == User.class)||(clazz==BookingAppl.class)||(clazz == Area.class);
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
					 Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());	
					   if ( !eventOrg1.isPresent() ){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
						}
						try{	
					    User eventOrg = eventOrg1.get();
					    ClientOrganisation client = eventOrg.getClientOrganisation();
							System.out.println("Start deleting");
							Object obj = parser.parse(eventJSON);
							JSONObject jsonObject = (JSONObject) obj;
							//long buildingId = (Long)jsonObject.get("buildingId");
							long eventId = (Long)jsonObject.get("eventId");
							System.out.println("eventId");	
							boolean bl=eventExternalService.deleteEvent(client, eventId);
							if(!bl){
								System.out.println("cannot delete event");
								return new ResponseEntity<Void>(HttpStatus.CONFLICT);	
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
					@RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
					@ResponseBody
					public ResponseEntity<Void> updateEvent(@RequestBody String eventJSON, HttpServletRequest rq) {
						System.out.println("start updating not yet");
						Principal principal = rq.getUserPrincipal();
						 Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());	
						   if ( !eventOrg1.isPresent() ){
								return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
							}
							try{	
						    User eventOrg = eventOrg1.get();
						    ClientOrganisation client = eventOrg.getClientOrganisation();
						    DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
							System.out.println("start updating");
							Object obj = parser.parse(eventJSON);
							JSONObject jsonObject = (JSONObject) obj;
							System.out.println("start lala");
							//long buildingId = (Long)jsonObject.get("buildingId");
							long eventId = (Long)jsonObject.get("id");
							System.out.println(eventId);
							
							JSONArray units = (JSONArray)jsonObject.get("units");
				            String unitsId = "";
				            for(int i = 0; i < units.size(); i++){
				            	System.out.println("test1");
				            	JSONObject unitObj = (JSONObject)units.get(i);		
				            	System.out.println("test2");
								long unitId = (Long)unitObj.get("id");
								System.out.println("test3");
								unitsId = unitsId+unitId + " ";
								System.out.println("test4");
							}
							System.out.println(unitsId);
							String event_title = (String)jsonObject.get("event_title");
							System.out.println(event_title);
							String event_content = (String)jsonObject.get("event_content");
							System.out.println(event_content);
							String event_description = (String)jsonObject.get("event_description");
							System.out.println(event_description);
							String event_approval_status = (String)jsonObject.get("event_approval_status");
							System.out.println(event_approval_status);
							Date event_start_date = sdf.parse((String)jsonObject.get("event_start_date"));
							System.out.println(event_start_date);
							Date event_end_date = sdf.parse((String)jsonObject.get("event_end_date"));
							System.out.println(event_end_date);
							//String event_period = (String)jsonObject.get("event_period");	
							String filePath = (String)jsonObject.get("filePath");
							System.out.println(filePath);
							System.out.println("end of controller");
							//Principal principal = rq.getUserPrincipal();
							//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
							boolean bl = eventExternalService.editEvent(client, eventOrg, eventId, unitsId, event_title, event_content, event_description, event_approval_status,
									 event_start_date, event_end_date, filePath);
							//levelService.editLevelInfo(levelId,levelNum, length, width, filePath);
							if(!bl){
								System.out.println("cannot update event");
								return new ResponseEntity<Void>(HttpStatus.CONFLICT);	
							}			
							else
							eventExternalService.updateEventOrganizerWithOnlyEventId(eventId);
						}
						catch (Exception e){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);
						}
						return new ResponseEntity<Void>(HttpStatus.OK);
					}
					
					
					
					
}

