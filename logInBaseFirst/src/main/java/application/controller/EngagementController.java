package application.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Building;
import application.entity.Discount;
import application.entity.Level;
import application.entity.User;
import application.exception.UserNotFoundException;
import application.repository.BuildingRepository;
import application.repository.CategoryRepository;
import application.repository.FeedbackRepository;
import application.service.BookingService;
import application.service.EngagementService;
import application.service.EventExternalService;
import application.service.EventService;
import application.service.TicketingService;
import application.service.UserService;



@Controller
public class EngagementController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
	private final EventExternalService eventExternalService;
	private final BookingService bookingService;
	private final UserService userService;
	private final TicketingService ticketingService;
	private final EventService eventService;
	private final EngagementService engagementService;
	private final FeedbackRepository feedbackRepository;
	private final CategoryRepository categoryRepository;
	private final BuildingRepository buildingRepository;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();

	@Autowired
	public EngagementController(BuildingRepository buildingRepository, CategoryRepository categoryRepository, FeedbackRepository feedbackRepository,EngagementService engagementService, EventService eventService, TicketingService ticketingService, EventExternalService eeventService, BookingService bookingService,
			UserService userService) {
		super();
		this.eventExternalService = eeventService;
		this.bookingService = bookingService;
		this.buildingRepository = buildingRepository;
		this.userService = userService;
		this.ticketingService = ticketingService;
		this.categoryRepository = categoryRepository;
		this.feedbackRepository = feedbackRepository;
		this.eventService = eventService;
		this.engagementService = engagementService;
	}
	
	@RequestMapping(value = "/viewDiscount", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> viewBuildings(String discount, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			Object obj1 = parser.parse(discount);
			JSONObject jsonObject = (JSONObject) obj1;
			String id = (String)jsonObject.get("discount");
			Discount dis = engagementService.getDiscount(id);
			//Gson gson = new Gson();
			//String json = gson.toJson(buildings);
			//System.out.println("Returning buildings with json of : " + json);
			//return json;	

			Gson gson = new Gson();

			String json = gson.toJson(dis);
			System.out.println("discount IS " + json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
}
