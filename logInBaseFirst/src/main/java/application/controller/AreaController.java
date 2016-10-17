package application.controller;
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

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.Event;
import application.entity.Maintenance;
import application.entity.Square;
import application.service.AreaService;
import application.service.EventService;


@Controller
@RequestMapping("/event")

public class AreaController {

	@Autowired
	private final AreaService areaService;
	private final EventService eventService;
	private JSONParser parser = new JSONParser();

	@Autowired
	public AreaController(AreaService areaService,EventService eventService) {
		this.areaService = areaService;
		this.eventService=eventService;
	}
	
	
		
		//for view only, call view areas; for load and edit, call viewAreas first and then call saves areas;
		@RequestMapping(value = "/viewAreas", method = RequestMethod.POST)
		@ResponseBody
		public String viewAreas( @RequestBody String event, HttpServletRequest rq)  {
			System.out.println("event json"+event);
			try{
			
				Object obj = parser.parse(event);
				System.out.println("Event obj "+obj);
				JSONObject jsonObject = (JSONObject) obj;
				
				System.out.println("Event jsonObject "+jsonObject);
				long eventId = (Long)jsonObject.get("id");
			
				System.out.println("Event id "+eventId);
				//long eventId = Long.parseLong(event);
				Set<Area> areas = areaService.getAreasByEventId(eventId);
				System.out.println("Event areas "+areas);
				Gson gson2 = new GsonBuilder()
					    .setExclusionStrategies(new ExclusionStrategy() {
					        public boolean shouldSkipClass(Class<?> clazz) {
					            return (clazz == BookingAppl.class);
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
					
			
			    
			    String json = gson2.toJson(areas);
			    System.out.println(json);
			    return json;
				}
				catch (Exception e){
					String message="{\"error\":\"cannot fetch\"}";
					Gson gson = new Gson();
					return gson.toJson(message);
				}			
}           
		
		
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
				System.out.println("Test: 33 "+areaName);
				//int dimensionWidth=Integer.valueOf((String)areaObj.get("width"));
				//int dimensionLength=Integer.valueOf((String)areaObj.get("length"));
				//System.out.println((int) (long) areaObj.get("dimensionWidth")+" "+Integer.valueOf((String)areaObj.get("dimensionWidth")));
				String description =(String)areaObj.get("description");
				
				if (areaId==0){
					System.out.println("Add new area");
					Area area=areaService.createAreaOnEvent(eventId,left, top, height,  width,  color,  type,areaName,description);
					areaIds.add(area.getId());
					System.out.println("AreaController Test: added new area, id "+area.getId());
				}else if((areaService.editAreaInfo(areaId,left, top, height,  width,  color,  type,areaName,description))==true){
					areaIds.add(areaId);		
					System.out.println("AreaController Test: existed/edited area, id "+areaId);
				}else{
					
					System.out.println("Area Controller: error, id "+areaId);
				}
						
				}//end for 
				System.out.println("Test: 6");
				if(areaService.deleteAreasFromEvent(areaIds,eventId)==false){
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

}