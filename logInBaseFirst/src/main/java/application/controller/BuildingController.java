package application.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
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
import application.domain.Level;
import application.domain.User;
import application.repository.VendorRepository;
import application.repository.AuditLogRepository;
import application.domain.AuditLog;
import application.domain.Building;
import application.domain.ClientOrganisation;
import application.service.user.BuildingService;
import application.service.user.ClientOrganisationService;
import application.service.user.UserService;

@Controller
@RequestMapping("/building")
public class BuildingController {
	@Autowired
	private final BuildingService buildingService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final AuditLogRepository auditLogRepository;

	private JSONParser parser = new JSONParser();

	@Autowired
	public BuildingController(AuditLogRepository auditLogRepository, BuildingService buildingService, ClientOrganisationService clientOrganisationService,
			UserService userService) {
		super();
		this.buildingService = buildingService;
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.auditLogRepository = auditLogRepository;
	}
 
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY', 'ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/viewBuildings", method = RequestMethod.GET)
	@ResponseBody
	public String viewBuildings(HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Building> buildings = client.getBuildings();
			//Gson gson = new Gson();
			//String json = gson.toJson(buildings);
			//System.out.println("Returning buildings with json of : " + json);
			//return json;	
			System.out.println(buildings);
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Level.class);
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



			String json = gson2.toJson(buildings);
			System.out.println("BUILDING IS " + json);

			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}	

	 @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/getBuilding/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getBuilding(@PathVariable("id") String buildingId, HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return null; 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();	
			long id = Long.parseLong(buildingId);
			boolean bl =buildingService.checkBuilding(client, id);
			Building build = buildingService.getBuildingById(id).get();
			if(bl){
				Gson gson2 = new GsonBuilder()
						.setExclusionStrategies(new ExclusionStrategy() {
							public boolean shouldSkipClass(Class<?> clazz) {
								return (clazz == Level.class);
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
				String json = gson2.toJson(build);
				System.out.println("BUILDING IS " + json);

				return json;
			}
			else
				return "cannot fetch";	
		}
		catch (Exception e){
			return "cannot fetch";
		}


	}
	 @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//Security filters for inputs needs to be added
	//This method takes in a string which contains the attributes of the building to be added.
	//Call $http.post(URL,stringToAdd);
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/addBuilding", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addBuilding(@RequestBody String buildingJSON,HttpServletRequest rq) {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(buildingJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			String name = (String)jsonObject.get("name");
			String address = (String)jsonObject.get("address");
			String postalCode = (String)jsonObject.get("postalCode");
			System.out.println(postalCode);
			String city = (String)jsonObject.get("city");
			int numFloor = ((Long)jsonObject.get("numFloor")).intValue();
			String filePath = (String)jsonObject.get("filePath");

			boolean bl = buildingService.create(client, name, address, postalCode, city, numFloor, filePath);
			System.out.println("adding building " + name);
			if(bl){
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Add Building: " + name);
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
			}
			else
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	
	 @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//This method takes in a String which is the ID of the building to be deleted
	// Call $http.post(URL,(String)id);
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/deleteBuilding", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> deleteBuilding(@RequestBody String buildingId,HttpServletRequest rq) {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println(buildingId + " delete!!");
			Object obj = parser.parse(buildingId);
			JSONObject jsonObject = (JSONObject) obj;
			long id = (Long)jsonObject.get("id");
			boolean bl = buildingService.deleteBuilding(client,id);
			System.out.println("delete building " + id);
			System.out.println(bl);
			if ( bl ){
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Delete Building: id" + id);
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
	}
	 @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//This method takes in a JSON format which contains an object with 7 attributes
	//Long/String id, String name, String address, int postalCode, String city, 
	//int numFloor, String filePath;
	//Call $httpPost(Url,JSONData);
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/updateBuilding", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateBuilding(@RequestBody String buildingId,HttpServletRequest rq) {
		System.out.println("start1111");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();		
			Object obj = parser.parse(buildingId);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println("start");
			long id = (Long)jsonObject.get("id");
			String name = (String)jsonObject.get("name");
			System.out.println("after name");
			String address = (String)jsonObject.get("address");
			System.out.println("after address");
			String postalCode = (String)jsonObject.get("postalCode");
			System.out.println("after post");
			String city = (String)jsonObject.get("city");
			System.out.println("after city");
			int numFloor = ((Long)jsonObject.get("numFloor")).intValue();
			String filePath = (String)jsonObject.get("filePath");
			//Principal principal = rq.getUserPrincipal();
			//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			boolean bl = buildingService.editBuildingInfo(client,id, name, address, postalCode, city, numFloor, filePath);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Property");
			al.setAction("Update Building: " + buildingService.getBuildingById(id).get().getName());
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}


}
