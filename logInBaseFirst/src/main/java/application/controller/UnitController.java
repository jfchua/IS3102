package application.controller;

import java.io.File;
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
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.service.LevelService;
import application.service.UnitService;
import application.service.UserService;

@Controller
@RequestMapping("/property")
public class UnitController {
	@Autowired
	private final UnitService unitService;
	private final LevelService levelService;
	private final UserService userService;
	private final AuditLogRepository auditLogRepository;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();
	
	@Autowired
	public UnitController(AuditLogRepository auditLogRepository, UnitService unitService,LevelService levelService,UserService userService) {
		this.unitService = unitService;
		this.levelService=levelService;
		this.auditLogRepository = auditLogRepository;
		this.userService=userService;
	}
	/*
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
				int col = (int) (long) unitObj.get("col");
				int row = (int) (long) unitObj.get("row");
				int sizex = (int) (long) unitObj.get("sizeX");
				int sizey = (int) (long) unitObj.get("sizeY");
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
					Unit unit=unitService.createUnitOnLevelWithIcon(levelId,iconId,left, top, height,  width,  color,  type,unitNumber,col,  row,  sizex, sizey,rentable,description);					
					System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());
				}else{
					System.out.println("UNIT IS USING DEFAULT ICON");
					if(type.equals("./svg/rect.svg")){						
						rentable=true;
						System.out.println("Shape is rect: rentable:" +rentable);
					}
					Unit unit=unitService.createUnitOnLevel(levelId,left, top, height,  width,  color,  type,unitNumber,col,  row,  sizex, sizey,rentable,description);
					System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());

				}		

			}catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		}	

	 */
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
				//System.out.println("********** unit: " + unit.getUnitNumber());
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

			System.out.println("************* ERROR: " + e.getMessage());
			e.printStackTrace();

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
	public ResponseEntity<Void> saveUnits(@RequestBody String json,HttpServletRequest rq) throws UserNotFoundException  {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
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
				int col = (int) (long) unitObj.get("col");
				int row = (int) (long) unitObj.get("row");
				int sizex = (int) (long) unitObj.get("sizeX");
				int sizey = (int) (long) unitObj.get("sizeY");

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
						Unit unit=unitService.createUnitOnLevelWithIcon(levelId,iconId,left, top, height,  width,  color,  type,unitNumber,col,  row,  sizex, sizey,rentable,description);
						unitIds.add(unit.getId());
						System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());

					}else{//SQUARE IS WITH DEFAULT ICON
						System.out.println("UNIT"+unitId+" IS USING DEFAULT ICON");
						System.out.println("TEST:6");
						String[] typeName1=type.split("/");
						System.out.println("TEST:6 "+typeName1[typeName1.length-1]);
						String[] typeName2=typeName1[typeName1.length-1].split("\\.");
						System.out.println("TEST:6 "+typeName2);
						String typeName3=typeName2[typeName2.length-2];
						System.out.println("type "+typeName3);

						if(typeName3.equals("rect")){						
							rentable=true;
							System.out.println("Shape is rect: " +rentable);

						}
						System.out.println("ADD NEW UNIT");
						Unit unit=unitService.createUnitOnLevel(levelId,left, top, height,  width,  color,  type,unitNumber,col,  row,  sizex, sizey,rentable,description);
						unitIds.add(unit.getId());
						System.out.println("UNITCONTROLLER: NEW UNIT IS ADDED. UNIT ID:"+unit.getId());


					}




				}else{//EDIT EXISTING UNIT

					if(unitService.editUnitInfo(unitId,left, top, height,  width,  color,  type,unitNumber,col,  row,  sizex, sizey,rentable,description)==true){
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
			/*if(unitService.deleteUnitsFromLevel(unitIds,levelId)==false){
					System.out.println("Test 61 error: cannot delete units");
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}else{
					System.out.println("Test 61 successful: units deleted/updated");
				}*/
			System.out.println("Test 7");
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Property");
			al.setAction("Save Units for level ID: " + levelId);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
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
	public ResponseEntity<String> deleteUnit(@RequestBody String idObj,HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{


			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("id"));
			long unitId = (Long)jsonObject.get("id");
			System.out.println((Long)jsonObject.get("levelId"));
			long levelId = (Long)jsonObject.get("levelId");
			System.out.println("unitcontroller 369"+ unitService.checkBookings(unitId));
			if(!unitService.checkBookings(unitId)){
				return new ResponseEntity<String>(geeson.toJson("Unit has prior bookings, cannot be deleted."),HttpStatus.CONFLICT);
			}
			else{
				if(unitService.deleteUnit(unitId,levelId)){
					System.out.println("DELETED");
					AuditLog al = new AuditLog();
					al.setTimeToNow();
					al.setSystem("Property");
					al.setAction("Delete Unit with ID: " + unitId);
					al.setUser(usr.get());
					al.setUserEmail(usr.get().getEmail());
					auditLogRepository.save(al);
					return new ResponseEntity<String>(HttpStatus.OK);
				}else{
					return new ResponseEntity<String>(geeson.toJson("Error in deleting unit"),HttpStatus.CONFLICT);
				}
			}
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error"),HttpStatus.CONFLICT);
		}

	}


	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/updateUnit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateUnit(@RequestBody String idObj,HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try{


			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("levelId"));
			long levelId = (Long)jsonObject.get("levelId");
			JSONObject unitJson=(JSONObject)jsonObject.get("unit");	
			long unitId = (Long)unitJson.get("id");				
			JSONObject squareJson=(JSONObject)unitJson.get("square");		
			int left = (int) (long) squareJson.get("left");
			int top = (int) (long) squareJson.get("top");
			int height = (int) (long) squareJson.get("height");
			int width = (int) (long) squareJson.get("width");
			String color = (String)squareJson.get("color");
			String type = (String)squareJson.get("type");
			String unitNumber =(String)unitJson.get("unitNumber");
			int col = (int) (long) unitJson.get("col");
			int row = (int) (long) unitJson.get("row");
			int sizex = (int) (long) unitJson.get("sizeX");
			int sizey = (int) (long) unitJson.get("sizeY");
			String description =(String)unitJson.get("description");
			boolean rentable =(boolean)unitJson.get("rentable");


			if(unitService.editUnitInfo(unitId,left,top, height,  width,  color, type, unitNumber, col,  row,  sizex, sizey,rentable, description)){
				System.out.println("EDITED");
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Update Unit with ID: " + unitId);
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else{
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

	}


	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/addUnit", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addUnit(@RequestBody String idObj,HttpServletRequest rq) {


		try{


			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("levelId"));
			long levelId = (Long)jsonObject.get("levelId");




			if(unitService.addUnitOnLevel(levelId) ){
				System.out.println("CREATED");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else{
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/addDefaultIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addDefaultIcon(@RequestBody String idObj,HttpServletRequest rq) {


		try{


			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("levelId"));
			long levelId = (Long)jsonObject.get("levelId");
			String type = (String)jsonObject.get("type");



			if(unitService.addDefaultIconOnLevel(levelId,type) ){
				System.out.println("CREATED");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else{
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/addCustIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addCustIcon(@RequestBody String idObj,HttpServletRequest rq) {


		try{


			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("levelId"));
			long levelId = (Long)jsonObject.get("levelId");
			long iconId = (Long)jsonObject.get("iconId");



			if(unitService.addCustIconOnLevel(levelId,iconId) ){
				System.out.println("CREATED");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}else{
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}

	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/getAllUnits", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Set<Unit>> getAllUnits( HttpServletRequest rq) throws UserNotFoundException  {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){

			return new ResponseEntity<Set<Unit>>(HttpStatus.CONFLICT);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			//Set<Icon> icons = iconService.getAllIconFromClientOrganisation(client);	
			Set<Building> buildings = client.getBuildings();
			Set<Unit> units=new HashSet<Unit>();
			for(Building building:buildings){
				Set<Level> levels=building.getLevels();
				for(Level level:levels){
					Set<Unit> unitsOfLevel=level.getUnits();
					for(Unit unitOfLevel:unitsOfLevel){
						unitOfLevel.getLevel().getBuilding().setLevels(null);
						unitOfLevel.getLevel().setUnits(null);;
						unitOfLevel.setBookings(null);
						unitOfLevel.setMaintenanceSchedule(null);
						unitOfLevel.setSquare(null);
						unitOfLevel.setUnitAttributeValues(null);
					}
					units.addAll(unitsOfLevel);
				}

			}


			return new ResponseEntity<Set<Unit>>(units,HttpStatus.OK);
		}
		catch (Exception e){

			return new ResponseEntity<Set<Unit>>(HttpStatus.CONFLICT);
		}

	}           
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/updateRent", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateRent(@RequestBody String unitObj,HttpServletRequest rq) {


		try{


			Object obj = parser.parse(unitObj);
			JSONObject jsonObject = (JSONObject) obj;
			JSONObject unitJson=(JSONObject)jsonObject.get("unit");	
			long unitId = (Long)unitJson.get("id");				
			System.out.println("test rent");
			Double rent;
			Object pricet = unitJson.get("rent");
			System.out.println("test rent2");
			if ( pricet instanceof Double){
				System.out.println("Type double");
				rent = (double)pricet;
			}
			else if ( pricet instanceof Integer){
				System.out.println("Type int");
				rent = (double)pricet;
			}
			else if ( pricet instanceof Long){
				System.out.println("type long");
				long theprice = (long)pricet;
				rent = Double.parseDouble(String.valueOf(theprice));
				System.out.println("rent is " + rent);
			}
			else{
				rent = -1.0;
			}

			//	Double rent =Double.parseDouble((String)unitJson.get("rent"));
			if ( rent < 0 ) { return new ResponseEntity<String>(geeson.toJson("Please enter a valid rental price."),HttpStatus.INTERNAL_SERVER_ERROR); }

			System.out.println(rent);
			if(!unitService.updateRent(unitId,rent))
				return new ResponseEntity<String>(geeson.toJson("Unable to update rent for a unit which is tied to event(s) without payment plan."),HttpStatus.INTERNAL_SERVER_ERROR);
		    return new ResponseEntity<String>(HttpStatus.OK);			
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in updating rent"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}



	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY','ROLE_EXTEVE')")
	//for view only, call view units; for load and edit, call viewUnits first and then call saves units;
	@RequestMapping(value = "/viewUnitsWithBookings", method = RequestMethod.POST)
	@ResponseBody
	public String viewUnitsWithBookings( @RequestBody String level, HttpServletRequest rq)  {
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
				System.out.println("********** unit: " + unit.getUnitNumber());
				Set<UnitAttributeValue> values = unit.getUnitAttributeValues();
				for(UnitAttributeValue value:values){
					value.setUnits(null);
					value.getUnitAttributeType().setUnitAttributeValues(null);
				}
				Set<BookingAppl> bookings = unit.getBookings();
				for(BookingAppl booking:bookings){
					booking.setUnit(null);
					booking.setAreas(null);
					booking.setEvent(null);
					booking.setOwner(null);

				}
				Set<MaintenanceSchedule> maints = unit.getMaintenanceSchedule();
				for(MaintenanceSchedule maint:maints){
					maint.setUnit(null);
					maint.setMaintenance(null);
				}
			}
			System.out.println("Level units "+units);
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



			String json = gson2.toJson(units);
			System.out.println(json);
			return json;
		}
		catch (Exception e){

			System.out.println("************* ERROR: " + e.getMessage());
			e.printStackTrace();

			JSONObject bd = new JSONObject(); 
			bd.put("error", "cannot fetch"); 

			System.out.println("Returning building id : " + bd.toString());
			return bd.toString();
		}


	}	









}