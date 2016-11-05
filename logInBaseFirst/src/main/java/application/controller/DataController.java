package application.controller;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
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
	@RequestMapping(value = "/occupancyAgainstUnit", method = RequestMethod.POST)
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
			//get units id
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
            //start, end date and duration
			Date start = sdf.parse((String)jsonObject.get("start"));
			System.out.println(start);
			Date end = sdf.parse((String)jsonObject.get("end"));
			long diff = end.getTime() - start.getTime();
			long duration = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
			Double duration1 = Double.valueOf(duration);
			System.err.println("duration is " + duration1);
			//calculate percentage for each unit
			NumberFormat formatter = new DecimalFormat("#0.00");    
			Iterator iter = units.iterator();				
			while (iter.hasNext()){
				Long id = Long.parseLong((String)iter.next());
				Unit unit = unitService.getUnitById(id).get();
			    Set<BookingAppl> bookings = unit.getBookings();
			    System.err.println("booking size is " + bookings.size());
			    JSONObject obj1 = new JSONObject();
				obj1.put("unit", id);	
				Double totalH = 0.0;
				for(BookingAppl b : bookings){
					System.out.println(b.getEvent_start_date_time());
					System.out.println(b.getEvent_end_date_time());
					if(b.getEvent_end_date_time().after(start) && b.getEvent_end_date_time().before(end) 
							&& b.getEvent_start_date_time().before(start)){
						System.out.println("Scenario 1");
						long diffB = b.getEvent_end_date_time().getTime() - start.getTime();
						long durationB = TimeUnit.HOURS.convert(diffB, TimeUnit.MILLISECONDS);
						totalH += Double.valueOf(durationB);	
						System.err.println(totalH);
					}
					else if(b.getEvent_start_date_time().after(start) && b.getEvent_end_date_time().before(end)){
						System.out.println("Scenario 2");
						long diffB = b.getEvent_end_date_time().getTime() - b.getEvent_start_date_time().getTime();
						long durationB = TimeUnit.HOURS.convert(diffB, TimeUnit.MILLISECONDS);
						totalH += Double.valueOf(durationB);	
						System.err.println("totalH " + totalH);
					}
					else if(b.getEvent_start_date_time().before(end) && b.getEvent_start_date_time().after(start)
							&& b.getEvent_end_date_time().after(end)){
						System.out.println("Scenario 3");
						long diffB = end.getTime() - b.getEvent_start_date_time().getTime();
						long durationB = TimeUnit.HOURS.convert(diffB, TimeUnit.MILLISECONDS);
						totalH += Double.valueOf(durationB);	
					}
					else if(b.getEvent_start_date_time().before(start) && b.getEvent_end_date_time().after(end)){
						System.out.println("Scenario 4");
						totalH += duration1;	
					}
				}
				System.err.println("booked hours are"+totalH);
				obj1.put("percent", formatter.format(totalH/duration1));	
				jArray.add(obj1);
			}		
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
					int[] monthsCount= {0,0,0};//last one is currentMonth
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
								
					JSONArray arrayToReturn = new JSONArray(); 
					SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
					for(int i=monthsCount.length-1;i>=0;i--){
						JSONObject oneMonth = new JSONObject();
						oneMonth.put("count", monthsCount[i]);
						cal = Calendar.getInstance();	
						cal.add(Calendar.MONTH, -i);
						Date result = cal.getTime();						
						oneMonth.put("label", ft.format(result));
						arrayToReturn.add(oneMonth);
						
					}
					
				
					
						//JSONObject bd = new JSONObject(); 
						//bd.put("error", "cannot fetch"); 
					
				
					
				
					return new ResponseEntity<String>(arrayToReturn.toString(), HttpStatus.OK);	
				}
				catch (Exception e){
					System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}
	}

}