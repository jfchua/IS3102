package application.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

//import org.hibernate.metamodel.source.annotations.xml.mocker.SchemaAware.SecondaryTableSchemaAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Area;
import application.entity.Category;
import application.entity.Event;
import application.entity.Level;
import application.entity.Unit;
import application.entity.User;
import application.exception.EventNotFoundException;
import application.exception.UserNotFoundException;
import application.service.BookingService;
import application.service.EventExternalService;
import application.service.TicketingService;
import application.service.UserService;



@Controller
public class TicketingController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookingController.class);
	private final EventExternalService eventExternalService;
	private final BookingService bookingService;
	private final UserService userService;
	private final TicketingService ticketingService;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();

	@Autowired
	public TicketingController(TicketingService ticketingService, EventExternalService eventService, BookingService bookingService,
			UserService userService) {
		super();
		this.eventExternalService = eventService;
		this.bookingService = bookingService;
		this.userService = userService;
		this.ticketingService = ticketingService;
	}

	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getBooking(@RequestBody String category,HttpServletRequest rq) throws UserNotFoundException {
		System.err.println("add cat");

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>("User not found!", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		try{
			System.err.println("before try");
			Object obj1 = parser.parse(category);
			System.err.println("after try" + obj1.toString());
			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");
			double price = -1;
			System.err.println("gotten eventid " + eventId);
			String name = (String)jsonObject.get("name");
			Object pricet = jsonObject.get("price");
			if ( pricet instanceof Double){
				price = (double)pricet;
			}
			else if ( pricet instanceof Integer){
				price = (int)pricet;
			}
			else if ( pricet instanceof Long){
				System.err.println("its of type long");
				long theprice = (long)jsonObject.get("price");
				price = (int)theprice;
			}
			System.err.println("price is now " + price);
			long numTicketst = (long)jsonObject.get("numTickets");
			int numTickets = (int)numTicketst;
			System.err.println("gotten info of " + name + " " + price + " " + numTickets + " event id: " + eventId);

			boolean bl = ticketingService.addCategory(eventId, name, price, numTickets);
			if ( bl ){
				return new ResponseEntity<String>(HttpStatus.OK);
			}
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in adding category" + e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(geeson.toJson("Server error in getting categories"),HttpStatus.INTERNAL_SERVER_ERROR);

	}	

	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/viewTicketCategories", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getCategories(@RequestBody String eventId,HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>("User not found!", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		try{

			Object obj1 = parser.parse(eventId);
			JSONObject jsonObject = (JSONObject) obj1;
			Long id = (Long)jsonObject.get("eventId");
			Set<Category> catToReturn = ticketingService.getCategories(id);

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Event.class);
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
			String json = gson2.toJson(catToReturn);
			System.out.println("CAT TO RETURN IS " + json);
			return new ResponseEntity<String>(json,HttpStatus.OK);


		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting categories"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/deleteCategory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> delCat(@RequestBody String category,HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>("User not found!", HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		try{
		Object obj1 = parser.parse(category);
		JSONObject jsonObject = (JSONObject) obj1;
		Long id = (Long)jsonObject.get("eventId");
		System.err.println("deleting id of : "  + id);
		ticketingService.deleteCat(id);
		return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch ( Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in deleting categories"),HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}
}

