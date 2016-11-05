package application.controller;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.json.simple.JSONObject;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

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
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Maintenance;
import application.entity.Square;
import application.entity.Unit;
import application.entity.User;
import application.enumeration.EventType;
import application.exception.UserNotFoundException;
import application.service.UnitService;
import application.service.UserService;
import application.service.EventExternalService;
import application.service.EventService;


@Controller
@RequestMapping("/dataVisual")

public class DataController {

	@Autowired
	private final UnitService unitService;
	private final EventExternalService eventExternalService;
	private final EventService eventService;
	private final UserService userService;
	private JSONParser parser = new JSONParser();

	@Autowired
	public DataController(UnitService unitService,EventExternalService eventExternalService, UserService userService,EventService eventService) {
		this.unitService = unitService;
		this.eventExternalService=eventExternalService;
		this.userService =userService;
		this.eventService =eventService;
	}
	    
	    //use JSON OBJECT obj.put to put the various data into a JSON array
		@RequestMapping(value = "/occupancyAgainstUnit", method = RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<String> occupancyAgainstUnit( @RequestBody String infoJSON, HttpServletRequest rq)  throws UserNotFoundException {
			System.out.println("start adding");
			DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println("start adding");
			Principal principal = rq.getUserPrincipal();
			System.out.println(principal.getName());
			Optional<User> user1 = userService.getUserByEmail(principal.getName());
			//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
			if (!user1.isPresent()){
				return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
			}
			try{
				JSONArray jArray = new JSONArray();
				User user = user1.get();
				ClientOrganisation client = user.getClientOrganisation();
				System.out.println(user.getName());
				Object obj = parser.parse(infoJSON);
				JSONObject jsonObject = (JSONObject) obj;
				JSONArray unitsId = (JSONArray)jsonObject.get("units");
	            Set<String> units = new HashSet<String>();
	            for(int i = 0; i < unitsId.size(); i++){
	            	JSONObject unitObj = (JSONObject)unitsId.get(i);		
	            	System.out.println(unitObj.toString());
					long unitId = (Long)unitObj.get("id");
					System.out.println(unitId);
					units.add(String.valueOf(unitId));
					System.out.println("unit set size is " + units.size());
				}
				Date event_start_date = sdf.parse((String)jsonObject.get("event_start_date"));
				System.out.println(event_start_date);
				Date event_end_date = sdf.parse((String)jsonObject.get("event_end_date"));
				Iterator iter = units.iterator();				
				while (iter.hasNext()){
					Long id = (Long)iter.next();
					Unit unit = unitService.getUnitById(id).get();
					
				}
				//Event event = eventExternalService.createEvent(client, eventOrg, unitsId, event_title, event_content, event_description, 
					//	event_approval_status, event_start_date, event_end_date, filePath);
			
				return new ResponseEntity<String>(jArray.toString(), HttpStatus.OK);	
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<String>(HttpStatus.CONFLICT);
			}
}          
		@PreAuthorize("hasAnyAuthority('ROLE_USER')")
	    //use JSON OBJECT obj.put to put the various data into a JSON array
			@RequestMapping(value = "/eventCountAgainstEventType", method = RequestMethod.GET)
			@ResponseBody
			public ResponseEntity<String> eventCountAgainstEventType( HttpServletRequest rq)  throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> user1 = userService.getUserByEmail(principal.getName());
				//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
				if (!user1.isPresent()){
					return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
				}
				try{
					
					User user = user1.get();
					ClientOrganisation client = user.getClientOrganisation();
					Set<Event> events=eventService.getAllEvents(client);
					
					
						//JSONObject bd = new JSONObject(); 
						//bd.put("error", "cannot fetch"); 
					int countCONCERT=0;
					int countCONFERENCE=0;
					int countFAIR=0;
					int countFAMILY=0;
					int countLIFESTYLE=0;
					int countSEMINAR=0;		
					
					for(Event event:events){
						if(event.getEventType().equals(EventType.CONCERT)){
							countCONCERT++;
							System.out.println("CONCERT for event id "+event.getId()+" "+event.getEventType());
							}
						else if(event.getEventType().equals(EventType.CONFERENCE)){
								countCONFERENCE++;
								System.out.println("CONFERENCE for event id "+event.getId()+" "+event.getEventType());
							}
						else if(event.getEventType().equals(EventType.FAIR)){
								countFAIR++;
								System.out.println("FAIR for event id "+event.getId()+" "+event.getEventType());
							}
						else if(event.getEventType().equals(EventType.FAMILY)){
								countFAMILY++;
								System.out.println("FAMILY for event id "+event.getId()+" "+event.getEventType());
							}
						else if(event.getEventType().equals(EventType.LIFESTYLE)){
								countLIFESTYLE++;
								System.out.println("LIFESTYLE for event id "+event.getId()+" "+event.getEventType());
							}
						else if(event.getEventType().equals(EventType.SEMINAR)){
								countSEMINAR++;
								System.out.println("SEMINAR for event id "+event.getId()+" "+event.getEventType());
							}else{
								System.out.println("ERROR!!! cannot read event id for event id "+event.getId()+" "+event.getEventType());
							}
						}				
					JSONObject eventTypeCount = new JSONObject(); 
					eventTypeCount.put("concert", countCONCERT); 				
					eventTypeCount.put("conference", countCONFERENCE);				
					eventTypeCount.put("fair", countFAIR); 						
					eventTypeCount.put("family", countFAMILY); 				
					eventTypeCount.put("lifestyle", countLIFESTYLE); 
					eventTypeCount.put("seminar", countSEMINAR); 
				
					
				
					return new ResponseEntity<String>(eventTypeCount.toString(), HttpStatus.OK);	
				}
				catch (Exception e){
					System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}
	}  
		@PreAuthorize("hasAnyAuthority('ROLE_USER')")
	    //use JSON OBJECT obj.put to put the various data into a JSON array
			@RequestMapping(value = "/eventCountAgainstTime", method = RequestMethod.GET)
			@ResponseBody
			public ResponseEntity<String> eventCountAginstTime( HttpServletRequest rq)  throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> user1 = userService.getUserByEmail(principal.getName());
				//Optional<EventOrganizer> eventOrg1 = eventOrganizerService.getEventOrganizerByEmail(principal.getName());
				if (!user1.isPresent()){
					return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
				}
				try{
					
					User user = user1.get();
					ClientOrganisation client = user.getClientOrganisation();
					Set<Event> events=eventService.getAllEvents(client);
					Set<Event> eventsOfTheTime=new HashSet<Event>();
					Calendar cal = Calendar.getInstance();				
					int currentMonth = cal.get(Calendar.MONTH);
					int currentMonthCount=0;
					int[] monthsCount= {0,0,0};
					for (Event event:events){
						cal.setTime(event.getEvent_start_date());
						if((cal.get(Calendar.MONTH))==currentMonth){
							monthsCount[2]++;
						}else if((cal.get(Calendar.MONTH))==(currentMonth-1)){
							monthsCount[1]++;
						}else if((cal.get(Calendar.MONTH))==(currentMonth-2)){
							monthsCount[0]++;
						}
					}
					String dataToreturn="";
					for(int i=0;i<monthsCount.length;i++){
						dataToreturn+=(monthsCount[i]+",");
					}
					JSONObject objToreturn = new JSONObject(); 
					objToreturn.put("countsForThreeMonth", dataToreturn);
					objToreturn.put("name", "Number of Events");
						//JSONObject bd = new JSONObject(); 
						//bd.put("error", "cannot fetch"); 
					
				
					
				
					return new ResponseEntity<String>(objToreturn.toString(), HttpStatus.OK);	
				}
				catch (Exception e){
					System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}
	}

}