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
@RequestMapping("/BI")

public class DataController {

	@Autowired
	private final AreaService areaService;
	private final EventService eventService;
	private JSONParser parser = new JSONParser();

	@Autowired
	public DataController(AreaService areaService,EventService eventService) {
		this.areaService = areaService;
		this.eventService=eventService;
	}
	
	
		
//use JSON OBJECT obj.put to put the various data into a JSON array
		/*@RequestMapping(value = "/dataVisual", method = RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<String> viewAreas( @RequestBody String event, HttpServletRequest rq)  {
			
			
			event.category.getTickets().getSize();
			return;
}      */     
		
		
		

}