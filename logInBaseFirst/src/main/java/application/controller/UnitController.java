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

import application.entity.*;
import application.service.LevelService;
import application.service.UnitService;

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
	
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
		//Security filters for inputs needs to be added
		//This method takes in a string which contains the attributes of the building to be added.
		//Call $http.post(URL,stringToAdd);
	//for create new floor plan, can use both saveUnits or create Units
		@RequestMapping(value = "/addUnit", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> addUnit(@RequestBody String unitJSON,HttpServletRequest rq) {

			try{
				//Principal principal = rq.getUserPrincipal();
				//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				Object obj = parser.parse(unitJSON);
				JSONObject unitObj = (JSONObject) obj;
				//","type": "./svg/rect.svg","icon": ""}}
				long levelId = (Long)unitObj.get("levelId");	
				long unitId = (Long)unitObj.get("id");	
				String unitNumber =(String)unitObj.get("unitNumber");
				int dimensionLength = (int) (long) unitObj.get("length");
				int dimensionWidth = (int) (long) unitObj.get("width");
				String description =(String)unitObj.get("description");
				
				JSONObject squareJson=(JSONObject)unitObj.get("square");			
				int left = (int) (long) squareJson.get("left");
				int top = (int) (long) squareJson.get("top");
				int height = (int) (long) squareJson.get("height");
				int width = (int) (long) squareJson.get("width");
				String color = (String)squareJson.get("color");			
				String type = (String)squareJson.get("type");
					
				boolean rentable=false;
				JSONObject iconJson;
				Long iconId;				
				
				if(type.equals("")){//SQUARE IS WITH ICON OBJECT
					System.out.println("UNIT IS WITH ICON OBJECT");
				
					iconJson=(JSONObject)squareJson.get("icon");
					iconId = (Long)iconJson.get("id");	
					String iconType=(String)iconJson.get("iconType");
					if(iconType.equals("RECT")){
						rentable=true;
						System.out.println("Customised shape is rect: rentable" +rentable);
					}
					Unit unit=unitService.createUnitOnLevelWithIcon(levelId,iconId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description);					
					System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());
				}else{
					System.out.println("UNIT IS USING DEFAULT ICON");
					if(type.equals("./svg/rect.svg")){						
						rentable=true;
						System.out.println("Shape is rect: rentable:" +rentable);
					}
					Unit unit=unitService.createUnitOnLevel(levelId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description);
					System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());
					
				}		

			}catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}	
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
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
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY','ROLE_EXTEVE')")
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
				for(Unit unit:units){									
					Set<UnitAttributeValue> values = unit.getUnitAttributeValues();
					for(UnitAttributeValue value:values){
						value.setUnits(null);
						value.getUnitAttributeType().setUnitAttributeValues(null);
					}
					}
				System.out.println("Level units "+units);
				Gson gson2 = new GsonBuilder()
					    .setExclusionStrategies(new ExclusionStrategy() {
					        public boolean shouldSkipClass(Class<?> clazz) {
					            return (clazz == Level.class) || (clazz == MaintenanceSchedule.class)||(clazz == BookingAppl.class);
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
		
			
}           

	
    //for load and edit, call viewUnits first and then call saves units; for create new floor plan, can use both saveUnits or create Units
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")	
	@RequestMapping(value = "/saveUnits", method = RequestMethod.POST)
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<Void> saveUnits(@RequestBody String json,HttpServletRequest rq)  {
			
			try{
				//GET LEVEL ID AND UNITS
				Object obj = parser.parse(json);			
				JSONObject text = (JSONObject) obj;			
				long levelId = (Long)text.get("id");				
				JSONObject array = (JSONObject)text.get("Units");				
				JSONArray units = (JSONArray)array.get("Unit");				
				Set<Long> unitIds=new HashSet<Long>();//list of ids of units that are still on floor plan
				
				//LOOP THROUGH EACH UNIT
				for(int i = 0; i <units.size(); i++){	
                    
                    
                    
						JSONObject unitObj = (JSONObject)units.get(i);				
						long unitId = (Long)unitObj.get("id");				
						JSONObject squareJson=(JSONObject)unitObj.get("square");		
						int left = (int) (long) squareJson.get("left");
						int top = (int) (long) squareJson.get("top");
						int height = (int) (long) squareJson.get("height");
						int width = (int) (long) squareJson.get("width");
						String color = (String)squareJson.get("color");
						String unitNumber =(String)unitObj.get("unitNumber");
						int dimensionWidth = (int) (long) unitObj.get("width");
						int dimensionLength = (int) (long) unitObj.get("length");
						String description =(String)unitObj.get("description");
						
						//boolean defaultIcon=true;
						boolean rentable=false;
						JSONObject iconJson;
						Long iconId;
						String type = (String)squareJson.get("type");
						System.out.println("TYPE:"+type);
                    
                        if (unitId==0){//CREATE NEW UNIT
							     if(type.equals("")){//SQUARE IS WITH ICON OBJECT
                                        System.out.println("UNIT"+unitId+" IS WITH ICON OBJECT");
                                        //	defaultIcon=false;
                                        System.out.println("TEST:1");
                                        iconJson=(JSONObject)squareJson.get("icon");
                                        System.out.println("TEST:2");
                                        iconId = (Long)iconJson.get("id");	
                                        System.out.println("TEST:3");
                                        String iconType=(String)iconJson.get("iconType");
                                        System.out.println("TEST:4");
                                            if(iconType.equals("RECT")){
                                                System.out.println("TEST:5");
                                                rentable=true;
                                                System.out.println("Customised shape is rect: " +rentable);
                                            }
						                  System.out.println("ADD NEW UNIT WITH ICON");
                                        Unit unit=unitService.createUnitOnLevelWithIcon(levelId,iconId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description);
                                        unitIds.add(unit.getId());
                                        System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());
						
						}else{//SQUARE IS WITH DEFAULT ICON
							         System.out.println("UNIT"+unitId+" IS USING DEFAULT ICON");
						              System.out.println("TEST:6");
						              if(type.equals("./svg/rect.svg")){						
							         rentable=true;
							         System.out.println("Shape is rect: " +rentable);
                            
						              }
                                        System.out.println("ADD NEW UNIT");
                                        Unit unit=unitService.createUnitOnLevel(levelId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description);
                                        unitIds.add(unit.getId());
                                        System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());
					
						
						}
								
									
							
							
						}else{//EDIT EXISTING UNIT
							
								if(unitService.editUnitInfo(unitId,left, top, height,  width,  color,  type,unitNumber,dimensionLength,dimensionWidth,rentable,description)==true){
									unitIds.add(unitId);		
									System.out.println("UNITCONTROLLER: UNIT IS EDITED. UNIT ID:"+unitId);
								}else{
									System.out.println("UNITCONTROLLER: ERROR. UNIT ID:"+unitId);
									//need to return error response
									//return new ResponseEntity<Void>(HttpStatus.CONFLICT);
								}
							
						}
                    
                    
						
						
						
				}//end for 
				System.out.println("Test: 6");
				//this part maybe can delete already
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
	
	
		@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
		@RequestMapping(value = "/deleteUnit", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> deleteUnit(@RequestBody String idObj,HttpServletRequest rq) {

			
			try{
				
				
				Object obj = parser.parse(idObj);
				JSONObject jsonObject = (JSONObject) obj;
				System.out.println((Long)jsonObject.get("id"));
				long unitId = (Long)jsonObject.get("id");
				System.out.println((Long)jsonObject.get("levelId"));
				long levelId = (Long)jsonObject.get("levelId");
				
				if(unitService.deleteUnit(unitId,levelId)){
					System.out.println("DELETED");
					return new ResponseEntity<Void>(HttpStatus.OK);
				}else{
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
			}
			catch (Exception e){
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			
		}
		
		
		
}