package application.controller;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Level;
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
						//System.out.println("CONCERT for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.CONFERENCE)){
							countCONFERENCE++;
						//	System.out.println("CONFERENCE for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.FAIR)){
							countFAIR++;
						//	System.out.println("FAIR for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.FAMILY)){
							countFAMILY++;
						//	System.out.println("FAMILY for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.LIFESTYLE)){
							countLIFESTYLE++;
						//	System.out.println("LIFESTYLE for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.SEMINAR)){
							countSEMINAR++;
						//	System.out.println("SEMINAR for event id "+event.getId()+" "+event.getEventType());
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

			Calendar cal = Calendar.getInstance();	
			/*
			int currentMonth = cal.get(Calendar.MONTH);
			
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
			*/
			String[] arrayType={"concert","conference","fair","family","lifestyle","seminar"};
			cal = Calendar.getInstance();	
			Date end=cal.getTime();
			cal.add(Calendar.MONTH, -2);
			Date start = cal.getTime();
			List<Set<Event>> countsByMonths=countEventByMonths( events, start, end);

			System.out.println("test output 235");
			JSONArray arrayToReturn = new JSONArray(); 
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
			cal.setTime(start);	
			for(Set<Event> countByMonth:countsByMonths){
				JSONObject oneMonth = new JSONObject();
				oneMonth.put("count", countByMonth.size());
				Date label=cal.getTime();
				oneMonth.put("label", ft.format(label));
				arrayToReturn.add(oneMonth);
				List<Set<Event>> countByMonthByTypes=countAgainstEventType(countByMonth);
				int indexForType=0;						
				for(Set<Event> countByMonthType:countByMonthByTypes){			
					oneMonth.put(arrayType[indexForType],countByMonthType.size());
					indexForType++;
				}
				cal.add(Calendar.MONTH, 1);

			}
			System.out.println("test output 245");


			/*
				for(int i=monthsCount.length-1;i>=0;i--){
					JSONObject oneMonth = new JSONObject();
					oneMonth.put("count", monthsCount[i]);

					cal = Calendar.getInstance();	
					cal.setTime();
					Date result = cal.getTime();						
					oneMonth.put("label", ft.format(result));
					arrayToReturn.add(oneMonth);

				}
			 */

			//JSONObject bd = new JSONObject(); 
			//bd.put("error", "cannot fetch"); 




			return new ResponseEntity<String>(arrayToReturn.toString(), HttpStatus.OK);	
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}

		 //use JSON OBJECT obj.put to put the various data into a JSON array
		@RequestMapping(value = "/occupancyAgainstTime", method = RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<String> occupancyAgainstTime(HttpServletRequest rq)  throws UserNotFoundException {
			System.out.println("start adding");
			DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
			//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			System.out.println("start adding");
			Principal principal = rq.getUserPrincipal();
			System.out.println(principal.getName());
			Optional<User> user1 = userService.getUserByEmail(principal.getName());
			if (!user1.isPresent()){
				return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
			}
			try{
				JSONArray jArray = new JSONArray();
				User user = user1.get();
				ClientOrganisation client = user.getClientOrganisation();
				System.out.println(user.getName());
				Set<Unit> units = new HashSet<Unit>();
			    Set<Building> buildings = client.getBuildings();
			    for(Building b : buildings){
			    	Set<Level> levels = b.getLevels();
			    	for(Level l : levels){
			    		Set<Unit> unitFromL = l.getUnits();
			    		for(Unit u : unitFromL){
			    			units.add(u);
			    		}
			    	}
			    }
	            
	            //start, end date and duration
			    Calendar cal = Calendar.getInstance();
			    int year = cal.get(Calendar.YEAR);
			    cal.set(Calendar.YEAR,year);			    
			    //int numOfDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
				//Double duration1 = Double.valueOf(numOfDays*24);
				SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM");
			    //System.err.println("duration is " + duration1);
				//calculate percentage for each unit
				NumberFormat formatter = new DecimalFormat("#0.00");    
				Double[] count = new Double[12];
				for(int i = 0; i < 12; i ++){	
					cal.set(Calendar.MONTH, i);			
				    cal.set(Calendar.DAY_OF_MONTH,31);
				    Date end = cal.getTime();
				    cal.set(Calendar.MONTH, i);	
				    cal.set(Calendar.DAY_OF_MONTH,1);
				    Date start = cal.getTime();
				    JSONObject obj1 = new JSONObject();
					obj1.put("month", ft.format(cal.getTime()));	
					Double totalH = 0.0;
					long diff = end.getTime() - start.getTime();
					long duration = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
					Double duration1 = Double.valueOf(duration);
					System.err.println("duration is " + duration1 + " for month " + i);
					
					for(Unit unit : units){
				    Set<BookingAppl> bookings = unit.getBookings();
				    System.err.println("booking size is " + bookings.size());		    				
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
				}	
			   obj1.put("percent", formatter.format(totalH/duration1));	
			   System.out.println(formatter.format(totalH/duration1));
			   jArray.add(obj1);
			}		
				return new ResponseEntity<String>(jArray.toString(), HttpStatus.OK);	
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<String>(HttpStatus.CONFLICT);
			}
	}        

	
		private List<Set<Event>> countAgainstEventType( Set<Event> events)  {
			Set<Event> countCONCERT=new HashSet<Event>();
			Set<Event> countCONFERENCE=new HashSet<Event>();
			Set<Event> countFAIR=new HashSet<Event>();
			Set<Event> countFAMILY=new HashSet<Event>();
			Set<Event> countLIFESTYLE=new HashSet<Event>();
			Set<Event> countSEMINAR=new HashSet<Event>();		


				for(Event event:events){
					if(event.getEventType().equals(EventType.CONCERT)){
						countCONCERT.add(event);
						System.out.println("CONCERT for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.CONFERENCE)){
							countCONFERENCE.add(event);
							System.out.println("CONFERENCE for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.FAIR)){
							countFAIR.add(event);
							System.out.println("FAIR for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.FAMILY)){
							countFAMILY.add(event);
							System.out.println("FAMILY for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.LIFESTYLE)){
							countLIFESTYLE.add(event);
							System.out.println("LIFESTYLE for event id "+event.getId()+" "+event.getEventType());
						}
					else if(event.getEventType().equals(EventType.SEMINAR)){
							countSEMINAR.add(event);
							System.out.println("SEMINAR for event id "+event.getId()+" "+event.getEventType());
						}else{
							System.out.println("ERROR!!! cannot read event id for event id "+event.getId()+" "+event.getEventType());
						}
					}				

				List<Set<Event>> counts=new ArrayList<Set<Event>>();
				counts.add(countCONCERT);
				counts.add(countCONFERENCE);
				counts.add(countFAIR);
				counts.add(countFAMILY);
				counts.add(countLIFESTYLE);
				counts.add(countSEMINAR);
					//{countCONCERT,countCONFERENCE,countFAIR,countFAMILY,countLIFESTYLE,countSEMINAR};
				return counts;
			
}  
		private List<Set<Event>> countEventByMonths( Set<Event> events, Date start, Date end)  {
			List<Set<Event>> counts= new ArrayList<Set<Event>>();
			Calendar cal = Calendar.getInstance();	
			cal.setTime(end);
			int yearEnd=cal.get(Calendar.YEAR);
			int monthEnd=cal.get(Calendar.MONTH);
			cal.setTime(start);
			int yearStart=cal.get(Calendar.YEAR);
			int monthStart=cal.get(Calendar.MONTH);
			while(yearStart<=yearEnd&&monthStart<=monthEnd){
				
				
				Set<Event> count=countEventAMonth(events,yearStart,monthStart);
				counts.add(count);
				cal.add(Calendar.MONTH, 1);
				yearStart=cal.get(Calendar.YEAR);
				monthStart=cal.get(Calendar.MONTH);
			}
		
				return counts;
			
}
		private Set<Event> countEventAMonth( Set<Event> events, int year, int month)  {
			Set<Event> counts= new HashSet<Event>();
			Calendar cal = Calendar.getInstance();	
			
			for (Event event:events){
				cal.setTime(event.getEvent_start_date());
				if((cal.get(Calendar.YEAR))==year&&(cal.get(Calendar.MONTH))==month){
					counts.add(event);
				}
			}
				return counts;
			
}


}