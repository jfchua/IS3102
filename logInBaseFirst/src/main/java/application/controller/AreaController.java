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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.Category;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Maintenance;
import application.entity.PaymentPlan;
import application.entity.Square;
import application.entity.User;
import application.exception.UserNotFoundException;
import application.service.AreaService;
import application.service.EventService;
import application.service.UserService;


@Controller
@RequestMapping("/area")

public class AreaController {

	@Autowired
	private final AreaService areaService;
	private final EventService eventService;
	private final UserService userService;
	
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();
	
	@Autowired
	public AreaController(AreaService areaService,EventService eventService,UserService userService) {
		this.areaService = areaService;
		this.eventService=eventService;
		this.userService=userService;
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE','ROLE_PROPERTY')")
		//for view only, call view areas; for load and edit, call viewAreas first and then call saves areas;
		@RequestMapping(value = "/viewAreas", method = RequestMethod.POST)
		@ResponseBody
		public String viewAreas(@RequestBody String booking, HttpServletRequest rq) {
		System.out.println("AreaController: Start Viewing Areas");
	
			try{
				Object obj = parser.parse(booking);
				
				JSONObject jsonObject = (JSONObject) obj;
				
				System.out.println("Event jsonObject "+jsonObject);
				long bookingId = (Long)jsonObject.get("id");
				System.out.println(bookingId);
				
				System.out.println("bookingId id "+bookingId);
				//long eventId = Long.parseLong(event);
				Set<Area> areas = areaService.getAreasByBookingId(bookingId);
				for(Area area: areas){
					area.setBooking(null);
				}
				
				Gson gson2 = new GsonBuilder()
						.setExclusionStrategies(new ExclusionStrategy() {
							public boolean shouldSkipClass(Class<?> clazz) {
								return false;
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
				String json = gson2.toJson(areas);
				//String json2 = gson2.toJson("Server error in getting all the events");
			return json;	
				}
				catch (Exception e){
					
					return "";
				}			
}           
		
	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE','ROLE_PROPERTY')")
		//for load and edit, call viewAreas first and then call saves areas; 
		@RequestMapping(value = "/saveAreas", method = RequestMethod.POST)
		@ResponseBody
		@ResponseStatus(HttpStatus.OK)
		public ResponseEntity<Void> saveAreas(@RequestBody String json,HttpServletRequest rq)  {
			
			try{
				System.out.println("Test: 1 "+json);
				Object obj = parser.parse(json);
				System.out.println("Test: 1 obj"+obj);
				JSONObject text = (JSONObject) obj;
				System.out.println("Test: 1 text"+text);
				//long eventId = Long.valueOf((String)text.get("id"));
				long eventId = (Long)text.get("id");
				System.out.println("Test: 1 eventId"+eventId);
				JSONObject array = (JSONObject)text.get("Areas");
				System.out.println("Test: 2 array object"+array);
				JSONArray areas = (JSONArray)array.get("Area");
				System.out.println("Test: 2 array untis array"+array.size());
				Set<Long> areaIds=new HashSet<Long>();//list of ids of areas that are still on floor plan
				System.out.println("Test: 2 size");
				
				for(int i = 0; i <areas.size(); i++){
					System.out.println("Test: 3 for loop"+i);
				JSONObject areaObj = (JSONObject)areas.get(i);
				System.out.println("Test: 3 areaObj "+areaObj);
				//long areaId = Long.valueOf((String)areaObj.get("id"));
				long areaId = (Long)areaObj.get("id");
				System.out.println("Test: 3 areaId "+areaId);
				JSONObject squareJson=(JSONObject)areaObj.get("square");
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
				String areaName =(String)areaObj.get("areaName");
				int col = (int) (long) areaObj.get("col");
				int row = (int) (long) areaObj.get("row");
				int sizex = (int) (long) areaObj.get("sizeX");
				int sizey = (int) (long) areaObj.get("sizeY");
				System.out.println("Test: 33 "+areaName);
				//int dimensionWidth=Integer.valueOf((String)areaObj.get("width"));
				//int dimensionLength=Integer.valueOf((String)areaObj.get("length"));
				//System.out.println((int) (long) areaObj.get("dimensionWidth")+" "+Integer.valueOf((String)areaObj.get("dimensionWidth")));
				String description =(String)areaObj.get("description");
				
				if (areaId==0){
					System.out.println("Add new area");
					Area area=areaService.createAreaOnBooking(eventId, col,  row,  sizex, sizey,left, top, height,  width,  color,  type,areaName,description);
					areaIds.add(area.getId());
					System.out.println("AreaController Test: added new area, id "+area.getId());
				}else if((areaService.editAreaInfo(areaId, col,  row,  sizex, sizey,left, top, height,  width,  color,  type,areaName,description))==true){
					areaIds.add(areaId);		
					System.out.println("AreaController Test: existed/edited area, id "+areaId);
				}else{
					
					System.out.println("Area Controller: error, id "+areaId);
				}
						
				}//end for 
				System.out.println("Test: 6");
				if(areaService.deleteAreasFromBooking(areaIds,eventId)==false){
					System.out.println("Test 61 error: cannot delete areas");
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}else{
					System.out.println("Test 61 successful: areas deleted/updated");
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
	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE','ROLE_PROPERTY')")
	@RequestMapping(value = "/addArea", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addArea(@RequestBody String idObj,HttpServletRequest rq) {

		
		try{
			
			
			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("bookingId"));
			long bookingId = (Long)jsonObject.get("bookingId");
			
			
			
			
			if(areaService.addAreaOnBooking(bookingId) ){
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
	
	
	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE','ROLE_PROPERTY')")
	@RequestMapping(value = "/addDefaultIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addDefaultIcon(@RequestBody String idObj,HttpServletRequest rq) {

		
		try{
			
			
			Object obj = parser.parse(idObj);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println((Long)jsonObject.get("bookingId"));
			long bookingId = (Long)jsonObject.get("bookingId");
			String type = (String)jsonObject.get("type");
			
			
			
			if(areaService.addDefaultIconOnBooking(bookingId,type) ){
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
	
}