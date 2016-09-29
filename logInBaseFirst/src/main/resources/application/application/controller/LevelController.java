package application.controller;
import java.util.Collection;
import java.util.Set;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.reflect.TypeToken;
import application.domain.Building;
import application.service.user.BuildingService;
import application.domain.Level;
import application.domain.Unit;
import application.service.user.LevelService;

@Controller
@RequestMapping("/level")
public class LevelController {
	@Autowired
	//private final BuildingService buildingService;
	private final LevelService levelService;
	
	private JSONParser parser = new JSONParser();

	@Autowired
	public LevelController(LevelService levelService) {
		super();
		//this.buildingService = buildingService;
		this.levelService = levelService;
	}
	
	// Call this method using $http.get and you will get a JSON format containing an array of level objects.
		// Each object (building) will contain... long id, .
			@RequestMapping(value = "/viewLevels", method = RequestMethod.POST)
			@ResponseBody
			public String viewLevels(@RequestBody String buildingId, HttpServletRequest rq) {
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				try{
				Object obj = parser.parse(buildingId);
				JSONObject jsonObject = (JSONObject) obj;
				long building = (Long)jsonObject.get("id");
				Set<Level> levels = levelService.getAllLevels(building);
				//Gson gson = new Gson();
				//String json = gson.toJson(levels);
			    //System.out.println("Returning levels with json of : " + json);
				//return json;
				
				Gson gson2 = new GsonBuilder()
					    .setExclusionStrategies(new ExclusionStrategy() {
					        public boolean shouldSkipClass(Class<?> clazz) {
					            return (clazz == Building.class)|| (clazz == Unit.class);
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
					
			
			    
			    String json = gson2.toJson(levels);
			    System.out.println(json);
			    return json;
				}
				catch (Exception e){
					return "cannot fetch";
				}
				//return new ResponseEntity<Void>(HttpStatus.OK);
			}
		
			//Security filters for inputs needs to be added
			//This method takes in a string which contains the attributes of the level to be added.
			//Call $http.post(URL,stringToAdd);
			@RequestMapping(value = "/addLevel", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<Void> addLevel(@RequestBody String levelJSON,
					HttpServletRequest rq) {

				try{
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					Object obj = parser.parse(levelJSON);
					JSONObject jsonObject = (JSONObject) obj;
					//Object obj1 = parser.parse(buildingJSON);
					//JSONObject jsonObject1 = (JSONObject) obj1;
					
					int levelNum = Integer.valueOf((String)jsonObject.get("levelNum"));
					int length = Integer.valueOf((String)jsonObject.get("length"));
					int width = Integer.valueOf((String)jsonObject.get("width"));
					String filePath = (String)jsonObject.get("filePath");
					
					long buildingId = (Long)jsonObject.get("id");
					
					Level level = levelService.create(levelNum, length, width, filePath);
					levelService.updateBuilding(buildingId, level.getId());
					System.out.println("adding level " + levelNum);
				}
				catch (Exception e){
					System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
				return new ResponseEntity<Void>(HttpStatus.OK);
			
			}	
			
			//This method takes in a String which is the ID of the level to be deleted
			// Call $http.post(URL,(String)id);
			@RequestMapping(value = "/deleteLevel", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<Void> deleteLevel(@RequestBody String levelJSON, HttpServletRequest rq) {

				try{
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					Object obj = parser.parse(levelJSON);
					JSONObject jsonObject = (JSONObject) obj;
					long buildingId = (Long)jsonObject.get("buildingId");
					long levelId = (Long)jsonObject.get("levelId");
					levelService.deleteLevel(levelId, buildingId);
				}
				catch (Exception e){
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
			
			//This method takes in a JSON format which contains an object with 5 attributes
			//Long/String id, int levelNum, int length, int width, String filePath
			//Call $httpPost(Url,JSONData);
			@RequestMapping(value = "/updateLevel", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<Void> updateLevel(@RequestBody String levelJSON, HttpServletRequest rq) {

				try{
					Object obj = parser.parse(levelJSON);
					JSONObject jsonObject = (JSONObject) obj;
					long buildingId = (Long)jsonObject.get("buildingId");
					long levelId = (Long)jsonObject.get("levelId");
					int levelNum = Integer.valueOf((String)jsonObject.get("levelNum"));
					int length = Integer.valueOf((String)jsonObject.get("length"));
					int width = Integer.valueOf((String)jsonObject.get("width"));
					String filePath = (String)jsonObject.get("filePath");			
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					levelService.editLevelInfo(levelId,levelNum, length, width, filePath);
					levelService.updateBuilding(buildingId, levelId);
				}
				catch (Exception e){
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
			
			
}
