package application.controller;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Event;
import application.entity.EventOrganizer;
import application.service.EventOrganizerService;

@Controller
@RequestMapping("/eventOrg")
public class EventOrganizerController {
	 private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerController.class);
	    private final EventOrganizerService eventOrgService;
		private JSONParser parser = new JSONParser();
	    @Autowired
	    public EventOrganizerController(EventOrganizerService eventOrgService) {
	    	super();
	        this.eventOrgService = eventOrgService;
	    }
	    
	 // Call this method using $http.get and you will get a JSON format containing a eventOrganizer
		// Each object (building) will contain... long id, other profile info
			@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
			@ResponseBody
			public String viewProfile(@RequestBody String orgId, HttpServletRequest rq) {
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				try{
					Object obj = parser.parse(orgId);
					JSONObject jsonObject = (JSONObject) obj;
					long id = (Long)jsonObject.get("id");
					EventOrganizer eventOrg = eventOrgService.getEventOrganizerById(id).get();
					//Gson gson = new Gson();
					//String json = gson.toJson(buildings);
					//System.out.println("Returning buildings with json of : " + json);
					//return json;	
					System.out.println(eventOrg);
					Gson gson2 = new GsonBuilder()
							.setExclusionStrategies(new ExclusionStrategy() {
								public boolean shouldSkipClass(Class<?> clazz) {
									return (clazz == Event.class);
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



					String json = gson2.toJson(eventOrg);
					System.out.println("EVENT ORGANIZER IS " + json);

					return json;
				}
				catch (Exception e){
					return "cannot fetch";
				}
			}
			
			//This method takes in a JSON format which contains an object with 5 attributes
			//Long/String id, int levelNum, int length, int width, String filePath
			//Call $httpPost(Url,JSONData);
			@RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<Void> updateProfile(@RequestBody String profileJSON, HttpServletRequest rq) {

				try{
					Object obj = parser.parse(profileJSON);
					JSONObject jsonObject = (JSONObject) obj;
					//long buildingId = (Long)jsonObject.get("buildingId");
					long orgId = (Long)jsonObject.get("eventOrgId");
					String firstName = (String)jsonObject.get("firstName");
					String lastName = (String)jsonObject.get("lastName");
					String phoneNo = (String)jsonObject.get("phoneNo");		
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					eventOrgService.updateProfile(orgId,firstName, lastName, phoneNo );
				}
				catch (Exception e){
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
	    
}
