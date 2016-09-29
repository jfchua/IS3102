package application.controller;

import java.util.Set;
//import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import application.domain.Level;
import application.domain.Building;
import application.service.user.BuildingService;

@Controller
@RequestMapping("/building")
public class BuildingController {
	@Autowired
	private final BuildingService buildingService;
	
	private JSONParser parser = new JSONParser();

	@Autowired
	public BuildingController(BuildingService buildingService) {
		super();
		this.buildingService = buildingService;
	}
	
	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
		@RequestMapping(value = "/viewBuildings", method = RequestMethod.GET)
		@ResponseBody
		public String viewBuildings(HttpServletRequest rq) {
			//Principal principal = rq.getUserPrincipal();
			//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			try{
			Set<Building> buildings = buildingService.getAllBuildings();
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
		    System.out.println(json);
		   
		    return json;
			}
			catch (Exception e){
				return "cannot fetch";
			}
		}
		
		//Security filters for inputs needs to be added
		//This method takes in a string which contains the attributes of the building to be added.
		//Call $http.post(URL,stringToAdd);
		@RequestMapping(value = "/addBuilding", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> addBuilding(@RequestBody String buildingJSON,HttpServletRequest rq) {

			try{
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				Object obj = parser.parse(buildingJSON);
				JSONObject jsonObject = (JSONObject) obj;

				String name = (String)jsonObject.get("name");
				String address = (String)jsonObject.get("address");
				int postalCode = Integer.valueOf((String)jsonObject.get("postalCode"));
				String city = (String)jsonObject.get("city");
				int numFloor = Integer.valueOf((String)jsonObject.get("numFloor"));
				String filePath = (String)jsonObject.get("filePath");
				
				Building build = buildingService.create(name, address, postalCode, city, numFloor, filePath);
				System.out.println("adding building " + name);
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}	
		
		//This method takes in a String which is the ID of the building to be deleted
		// Call $http.post(URL,(String)id);
		@RequestMapping(value = "/deleteBuilding", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> deleteBuilding(@RequestBody String buildingId,HttpServletRequest rq) {

			try{
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				Object obj = parser.parse(buildingId);
				JSONObject jsonObject = (JSONObject) obj;
				long id = (Long)jsonObject.get("id");
				buildingService.deleteBuilding(id);
				System.out.println("delete building " + id);
			}
			catch (Exception e){
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		
		//This method takes in a JSON format which contains an object with 7 attributes
		//Long/String id, String name, String address, int postalCode, String city, 
		//int numFloor, String filePath;
		//Call $httpPost(Url,JSONData);
		@RequestMapping(value = "/updateBuilding", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> updateBuilding(@RequestBody String buildingId,HttpServletRequest rq) {

			try{
				Object obj = parser.parse(buildingId);
				JSONObject jsonObject = (JSONObject) obj;
				long id = (Long)jsonObject.get("id");
				String name = (String)jsonObject.get("name");
				String address = (String)jsonObject.get("address");
				int postalCode = Integer.valueOf((String)jsonObject.get("postalCode"));
				String city = (String)jsonObject.get("city");
				int numFloor = Integer.valueOf((String)jsonObject.get("numFloor"));
				String filePath = (String)jsonObject.get("filePath");
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				buildingService.editBuildingInfo(id, name, address, postalCode, city, numFloor, filePath);
			}
			catch (Exception e){
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
	

}
