package application.controller;

import java.security.Principal;
import java.util.HashSet;
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

import application.domain.*;
import application.service.user.LevelService;
import application.service.user.UnitService;

@Controller
@RequestMapping("/property")
public class UnitController {
	@Autowired
	private final UnitService unitService;
	private final LevelService levelService;
	private JSONParser parser = new JSONParser();

	@Autowired
	public UnitController(UnitService unitService,LevelService levelService) {
		this.unitService = unitService;
		this.levelService=levelService;
	}
	
	
		//Security filters for inputs needs to be added
		//This method takes in a string which contains the attributes of the building to be added.
		//Call $http.post(URL,stringToAdd);
	//for create new floor plan, can use both saveUnits or create Units
		@RequestMapping(value = "/addUnits", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> addUnits(@RequestBody String unitsJSON,HttpServletRequest rq) {

			try{
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				Object obj = parser.parse(unitsJSON);
				JSONObject text = (JSONObject) obj;
				long levelId = Long.valueOf((String)text.get("id"));
				JSONObject array = (JSONObject)text.get("Units");
				JSONArray units = (JSONArray)array.get("Unit");
				System.out.println();
				for(int i = 0; i < units.size(); i++){
				JSONObject unitObj = (JSONObject)units.get(i);
				int left = Integer.valueOf((String)unitObj.get("left"));
				int top = Integer.valueOf((String)unitObj.get("top"));
				int height = Integer.valueOf((String)unitObj.get("height"));
				int width = Integer.valueOf((String)unitObj.get("width"));
				String color = (String)unitObj.get("color");
				String type = (String)unitObj.get("type");
			
				Unit unit=unitService.createUnitOnLevel(levelId,left, top, height,  width,  color,  type,"#01-02",10,10,true,"10");
				//Unit unit=unitService.createUnit(left, top, height,  width,  color,  type,"#01-02",10,10,true,"10");
				System.out.println("adding unit " + unit.getId());
				}
			
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}	
		//for view only, call view units; for load and edit, call viewUnits first and then call saves units;
				@RequestMapping(value = "/getUnitsId", method = RequestMethod.POST)
				@ResponseBody
				public String getUnitsId( @RequestBody String units, HttpServletRequest rq)  {
					System.out.println("level json"+units);
					try{
						System.out.println("Sending notification...");
						Object obj = parser.parse(units);
						JSONObject jsonObject = (JSONObject) obj;
						JSONArray unitsId = (JSONArray)jsonObject.get("id");
						//int len = unitId.length();
						//unitId = unitId.substring(1, len-1);
						//String[] unitsId = unitId.split(",");
						System.out.println("before the loop");
						String toBeReturned = "";
						System.out.println("before the loop2");
						System.out.println(unitsId.size());
						for(int i = 0; i < unitsId.size(); i++){
							System.out.println((Long)unitsId.get(i));
							toBeReturned = toBeReturned+(Long)unitsId.get(i) + " ";
							System.out.println(toBeReturned);
						}
						Gson gson = new Gson();
					    String json = gson.toJson(toBeReturned);
					    System.out.println(json);
					    return json;
						}
						catch (Exception e){
							return "cannot fetch units id";
						}
				}
		
		//for view only, call view units; for load and edit, call viewUnits first and then call saves units;
		@RequestMapping(value = "/viewUnits", method = RequestMethod.POST)
		@ResponseBody
		public String viewUnits( @RequestBody String level, HttpServletRequest rq)  {
			System.out.println("level json"+level);
			try{
			
				Object obj = parser.parse(level);
				System.out.println("Level obj "+obj);
				JSONObject jsonObject = (JSONObject) obj;
				
				System.out.println("Level jsonObject "+jsonObject);
				long levelId = (Long)jsonObject.get("id");
			
				System.out.println("Level id "+levelId);
				//long levelId = Long.parseLong(level);
				Set<Unit> units = unitService.getUnitsByLevelId(levelId);
				System.out.println("Level units "+units);
				Gson gson2 = new GsonBuilder()
					    .setExclusionStrategies(new ExclusionStrategy() {
					        public boolean shouldSkipClass(Class<?> clazz) {
					            return (clazz == Level.class) || (clazz == Maintenance.class)||(clazz == BookingAppl.class);
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
					
			
			    
			    String json = gson2.toJson(units);
			    System.out.println(json);
			    return json;
				}
				catch (Exception e){
					JSONObject bd = new JSONObject(); 
					bd.put("error", "cannot fetch"); 
					
				    System.out.println("Returning building id : " + bd.toString());
					return bd.toString();
				}
		
}            //for load and edit, call viewUnits first and then call saves units; for create new floor plan, can use both saveUnits or create Units
		@RequestMapping(value = "/saveUnits", method = RequestMethod.POST)
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<Void> saveUnits(@RequestBody String json,HttpServletRequest rq)  {
			
			try{
				System.out.println("Test: 1 "+json);
				Object obj = parser.parse(json);
				System.out.println("Test: 1 obj"+obj);
				JSONObject text = (JSONObject) obj;
				System.out.println("Test: 1 text"+text);
				//long levelId = Long.valueOf((String)text.get("id"));
				long levelId = (Long)text.get("id");
				System.out.println("Test: 1 levelId"+levelId);
				JSONObject array = (JSONObject)text.get("Units");
				System.out.println("Test: 2 array object"+array);
				JSONArray units = (JSONArray)array.get("Unit");
				System.out.println("Test: 2 array untis array"+array.size());
				Set<Long> unitIds=new HashSet<Long>();//list of ids of units that are still on floor plan
				System.out.println("Test: 2 size");
				
				for(int i = 0; i <units.size(); i++){
					System.out.println("Test: 3 for loop"+i);
				JSONObject unitObj = (JSONObject)units.get(i);
				System.out.println("Test: 3 unitObj "+unitObj);
				//long unitId = Long.valueOf((String)unitObj.get("id"));
				long unitId = (Long)unitObj.get("id");
				System.out.println("Test: 3 unitId "+unitId);
				JSONObject squareJson=(JSONObject)unitObj.get("square");
				System.out.println("Test: 3 squareJson"+squareJson);
			//	System.out.println("Test: 31 left"+Integer.valueOf((String)squareJson.get("left"))+" "+Integer.valueOf(squareJson.get("left"));
				//int left = Integer.valueOf((String)squareJson.get("left"));
				System.out.println("Test: 3 squareJson ");
			int left = (int) (long) squareJson.get("left");
			System.out.println("Test: 3 squareJson left "+left);
				//int top = Integer.valueOf((String)squareJson.get("top"));
			int top = (int) (long) squareJson.get("top");
				//int height = Integer.valueOf((String)squareJson.get("height"));
				//int width = Integer.valueOf((String)squareJson.get("width"));
			int height = (int) (long) squareJson.get("height");
			int width = (int) (long) squareJson.get("width");
				System.out.println("Test: 32");
				String color = (String)squareJson.get("color");
				String type = (String)squareJson.get("type");
				System.out.println("Test: 33");
				String unitNumber =(String)unitObj.get("unitNumber");
				System.out.println("Test: 33 "+unitNumber);
				//int dimensionWidth=Integer.valueOf((String)unitObj.get("width"));
				//int dimensionLength=Integer.valueOf((String)unitObj.get("length"));
				//System.out.println((int) (long) unitObj.get("dimensionWidth")+" "+Integer.valueOf((String)unitObj.get("dimensionWidth")));
				System.out.println("test");
				int dimensionWidth = (int) (long) unitObj.get("width");
				System.out.println(dimensionWidth+"width dismension");
				int dimensionLength = (int) (long) unitObj.get("length");
				boolean rentable=false;
				if(type.equals("rect")){
					
					rentable=true;
					System.out.println("Shape is rect: " +rentable);
				}
				String description =(String)unitObj.get("description");
				
				if (unitId==0){
					System.out.println("Add new unit");
					Unit unit=unitService.createUnitOnLevel(levelId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description);
					unitIds.add(unit.getId());
					System.out.println("UnitController Test: added new unit, id "+unit.getId());
				}else if((unitService.editUnitInfo(unitId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description))==true){
					unitIds.add(unitId);		
					System.out.println("UnitController Test: existed/edited unit, id "+unitId);
				}else{
					
					System.out.println("Unit Controller: error, id "+unitId);
				}
						
				}//end for 
				System.out.println("Test: 6");
				if(unitService.deleteUnitsFromLevel(unitIds,levelId)==false){
					System.out.println("Test 61 error: cannot delete units");
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}else{
					System.out.println("Test 61 successful: units deleted/updated");
				}
				System.out.println("Test 7");
				}
				catch (Exception e){
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
			System.out.println("End Save");
			ResponseEntity<Void> v = new ResponseEntity<Void>(HttpStatus.OK);
			return v;
		
}

}