package application.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
//import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.AuditLog;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.SpecialRate;
import application.entity.User;
import application.entity.Vendor;
import application.exception.UserNotFoundException;
import application.repository.VendorRepository;
import application.service.BuildingService;
import application.service.ClientOrganisationService;
import application.service.SpecialRateService;
import application.service.UserService;
import application.repository.AuditLogRepository;

@Controller
@RequestMapping("/rate")
public class SpecialRateController {
	@Autowired
	private final SpecialRateService specialRateService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final AuditLogRepository auditLogRepository;
	private Gson geeson= new Gson();
	private JSONParser parser = new JSONParser();

	@Autowired
	public SpecialRateController(AuditLogRepository auditLogRepository, SpecialRateService specialRateService, ClientOrganisationService clientOrganisationService,
			UserService userService) {
		super();
		this.specialRateService = specialRateService;
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.auditLogRepository = auditLogRepository;
	}

	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/viewAllRates", method = RequestMethod.GET)
	@ResponseBody
	public String viewAllRates(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
		    System.out.println("start view");
			Set<SpecialRate> rates = client.getSpecialRates();
			System.out.println("There are " + rates.size() + " rates");   
			Gson gson2 = new Gson();
			String json = gson2.toJson(rates);
		    System.out.println(json);
		    return json;
			}
			catch (Exception e){
				return "cannot fetch";
			}
			//return new ResponseEntity<Void>(HttpStatus.OK);
		}

	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
		@RequestMapping(value = "/getRate/{id}", method = RequestMethod.GET)
		@ResponseBody
		public String getRate(@PathVariable("id") String vendorId, HttpServletRequest rq) {
			try{
			
				long id = Long.parseLong(vendorId);
				SpecialRate rate = specialRateService.getSpecialRateById(id).get();
				Gson gson2 = new Gson();
				String json = gson2.toJson(rate);
			    System.out.println(json);
			    return json;
			}
			catch (Exception e){
				return "cannot fetch";
			}
		}

		//Security filters for inputs needs to be added
		//This method takes in a string which contains the attributes of the event to be added.
		//Call $http.post(URL,stringToAdd);
		@RequestMapping(value = "/addSpecialRate", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<String> addSpecialRate(@RequestBody String rateJSON,
				HttpServletRequest rq) throws UserNotFoundException {
			System.out.println("start adding");
			Principal principal = rq.getUserPrincipal();
			Optional<User> usr = userService.getUserByEmail(principal.getName());
			if ( !usr.isPresent() ){
				return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
			}
			try{
				ClientOrganisation client = usr.get().getClientOrganisation();
				System.out.println(client.getOrganisationName());
				Object obj = parser.parse(rateJSON);
				JSONObject jsonObject = (JSONObject) obj;
	            Double rate = Double.valueOf((String)jsonObject.get("rate"));
	            System.out.println(rate);
	            System.out.println("rate2");
				String period = (String)jsonObject.get("period");
				String description = (String)jsonObject.get("description");			
				boolean bl = specialRateService.createSpecialRate(client, rate,description, period);
				System.out.println("adding rate " + rate);
				if(!bl){
					System.out.println("invalid rate");
					return new ResponseEntity<String>(geeson.toJson("Period has already existed, please choose another period."), HttpStatus.INTERNAL_SERVER_ERROR);
				}
					
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<String>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<String>(HttpStatus.OK);	
		}	

		//This method takes in a String which is the ID of the event to be deleted
				// Call $http.post(URL,(String)id);
				@RequestMapping(value = "/deleteRate", method = RequestMethod.POST)
				@ResponseBody
				public ResponseEntity<Void> deleteRate(@RequestBody String rateJSON, HttpServletRequest rq) throws UserNotFoundException {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return new ResponseEntity<Void>(HttpStatus.CONFLICT); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();
						System.out.println("Start deleting");
						Object obj = parser.parse(rateJSON);
						JSONObject jsonObject = (JSONObject) obj;
						long rateId = (Long)jsonObject.get("id");
						System.out.println(rateId);	
						boolean bl=specialRateService.deleteSpecialRate(client, rateId);
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
				@RequestMapping(value = "/updateRate", method = RequestMethod.POST)
				@ResponseBody
				public ResponseEntity<String> updateRate(@RequestBody String rateJSON, HttpServletRequest rq) throws UserNotFoundException {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return new ResponseEntity<String>(HttpStatus.CONFLICT); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();
						System.out.println("start updating");
						Object obj = parser.parse(rateJSON);
						JSONObject jsonObject = (JSONObject) obj;
						long rateId = (Long)jsonObject.get("id");
						Double rate = Double.valueOf((String)jsonObject.get("rate"));
			            System.out.println(rate);
			            System.out.println("rate2");
						String period = (String)jsonObject.get("period");
						String description = (String)jsonObject.get("description");		
						boolean bl = specialRateService.updateSpecialRate(client, rateId, rate, description, period);
						System.out.println("editing rate " + rateId);
						if(!bl){
							return new ResponseEntity<String>(geeson.toJson("Period has already existed, please choose another period."), HttpStatus.INTERNAL_SERVER_ERROR);
						}
					}
					catch (Exception e){
						return new ResponseEntity<String>(HttpStatus.CONFLICT);
					}
					return new ResponseEntity<String>(HttpStatus.OK);
				}
				
		
		
}
