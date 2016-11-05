package application.controller;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import application.exception.UserNotFoundException;
import application.service.UnitService;
import application.service.UserService;
import application.service.EventExternalService;
import application.service.EventService;


@Controller
@RequestMapping("/BI")

public class DataController {

	@Autowired
	private final UnitService unitService;
	private final EventExternalService eventExternalService;
	private final UserService userService;
	private JSONParser parser = new JSONParser();

	@Autowired
	public DataController(UnitService unitService,EventExternalService eventExternalService, UserService userService) {
		this.unitService = unitService;
		this.eventExternalService=eventExternalService;
		this.userService =userService;
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
		
		
		

}