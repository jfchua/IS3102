package application.controller;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
//import org.json.JSONArray;
//import org.hibernate.metamodel.source.annotations.xml.mocker.SchemaAware.SecondaryTableSchemaAware;
//import org.json.JSONObject;
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

import application.entity.BookingAppl;
import application.entity.Building;
import application.entity.Category;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Feedback;
import application.entity.Level;
import application.entity.PaymentPlan;
import application.entity.Ticket;
import application.entity.User;
import application.exception.EventNotFoundException;
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
public class TicketingController {
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
	public TicketingController(BuildingRepository buildingRepository, CategoryRepository categoryRepository, FeedbackRepository feedbackRepository,EngagementService engagementService, EventService eventService, TicketingService ticketingService, EventExternalService eeventService, BookingService bookingService,
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
							return (clazz == Event.class)||(clazz == Ticket.class);
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
			if ( categoryRepository.findOne(id).getTickets().size() > 0){
				return new ResponseEntity<String>(geeson.toJson("Unable to delete category, there exists one or more tickets already purchased in this category!"),HttpStatus.INTERNAL_SERVER_ERROR);

			}
			ticketingService.deleteCat(id);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch ( Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in deleting categories"),HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	/*
	 * 
	 *			JSONArray obj = new JSONArray();
	 *				
	 *			for ( Event e : events ){
	 *				JSONObject list = new JSONObject();
	 *				list.put("id", e.getId());
	 *				list.put("title", e.getEvent_title());
	 *				obj.put(list);
	 *			}
	 */
	@RequestMapping(value = "/tixViewAllEvents",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewAllEvents(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			System.out.println("start view");
			Set<Event> events = eventService.getAllEvents();
			System.err.println("There are " + events.size() + " events");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Category.class)||(clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			String json = gson2.toJson(events);
			String json2 = gson2.toJson("Server error in getting all the events");
			System.out.println(json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tixViewEvent",  method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> viewEvent(@RequestBody String eventId,HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try{
			Object obj1 = parser.parse(eventId);
			JSONObject jsonObject = (JSONObject) obj1;
			Long id = (Long)jsonObject.get("eventId");
			System.err.println("gotten eventid of : "  + id);

			String eventDetails = ticketingService.getEventDataAsJson(id);
			return new ResponseEntity<String>(eventDetails,HttpStatus.OK);
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tixViewEventCat",  method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> viewEventCat(@RequestBody String eventId,HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

		try{
			Gson gson = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Event.class)||(clazz == Ticket.class)||(clazz == BookingAppl.class);
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
			Object obj1 = parser.parse(eventId);
			JSONObject jsonObject = (JSONObject) obj1;
			String id = (String)jsonObject.get("eventId");
			System.err.println("gotten eventid of : "  + Long.valueOf(id));

			Set<Category> cats = ticketingService.getCategories(Long.valueOf(id));
			return new ResponseEntity<String>(gson.toJson(cats),HttpStatus.OK);
		}
		catch ( EventNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tixBuyTicket",  method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> tixBuyTicket(@RequestBody String ticketsJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.err.println("inside tix buy ticket");
		try{

			Gson gson = new Gson();
			System.err.println("before wrapt");
			System.err.println(ticketsJSON);

			Object obj1 = parser.parse(ticketsJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			JSONArray id = (JSONArray)jsonObject.get("ticketsJSON");
			String paymentId = (String) jsonObject.get("paymentId");
			for ( Object j : id){
				JSONObject ticketInfo = (JSONObject) j;
				Long numTickets = (Long)ticketInfo.get("numTickets");
				Long categoryId = (Long)ticketInfo.get("categoryId");
				System.err.println("CALLED TICKETING SERVICE ONCE");
				ticketingService.generateTicket(usr.get(), paymentId, numTickets.intValue(), categoryId);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		//catch ( EventNotFoundException e){
		//	return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		//}
		catch (Exception e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tixFeedback",  method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> tixFeedback(@RequestBody String ticketsJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.err.println("inside tix buy ticket");
		try{

			Gson gson = new Gson();

			Object obj1 = parser.parse(ticketsJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			String feedbackCategory = (String)jsonObject.get("category");
			String feedbackMessage = (String)jsonObject.get("feedback");
			engagementService.setFeedback(usr.get(), feedbackCategory, feedbackMessage);

			return new ResponseEntity<String>(HttpStatus.OK);
		}
		//catch ( EventNotFoundException e){
		//	return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		//}
		catch (Exception e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}


	@RequestMapping(value = "/tixGetFeedback",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> tixGetFeedback(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.err.println("inside tix buy ticket");
		try{

			Gson gson = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class);
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
			List<Feedback> list = feedbackRepository.findAll();


			return new ResponseEntity<String>(gson.toJson(list),HttpStatus.OK);
		}
		//catch ( EventNotFoundException e){
		//	return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		//}
		catch (Exception e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}





	@RequestMapping(value = "/tixGetTix",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> tixGetTix(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.err.println("inside tix get ticket");
		try{

			Gson gson = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Category.class);
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
			Set<Ticket> ticketsToReturn = usr.get().getTickets();
			System.err.println("return tix original" + gson.toJson(usr.get().getTickets() ));
			Date todayDate = new Date();
			Set<Ticket> toDelete = new HashSet<>();

			for ( Ticket t : ticketsToReturn){
				if (t.getEnd_date().before(todayDate)){
					toDelete.add(t);
				}
			}
			ticketsToReturn.removeAll(toDelete);
			System.err.println("return tix: " + gson.toJson(ticketsToReturn));
			return new ResponseEntity<String>(gson.toJson(usr.get().getTickets()),HttpStatus.OK);
		}
		//catch ( EventNotFoundException e){
		//	return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		//}
		catch (Exception e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}


	@RequestMapping(value = "/tixCheckTix",  method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> tixCheckTix(@RequestBody String ticketsJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String toReturn = "OK";
		System.err.println("inside tix buy ticket");
		try{

			Gson gson = new Gson();
			System.err.println("before wrapt");
			System.err.println(ticketsJSON);

			JSONArray id = (JSONArray)parser.parse(ticketsJSON);
			System.err.println(id.toString());
			for ( Object j : id){
				System.err.println(j.toString());
				JSONObject ticketInfo = (JSONObject) j;
				Long numTickets = (Long)ticketInfo.get("numTickets");
				Long categoryId = (Long)ticketInfo.get("categoryId");
				System.err.println("CALLED TICKETING SERVICE ONCE");
				String catName = categoryRepository.findOne(categoryId).getCategoryName();
				int res = ticketingService.checkTickets(numTickets.intValue(), categoryId);
				if ( res != -1 ){
					toReturn += ("Category: " + catName + " has only " + res + " tickets remaining for purchase.");
				}
			}
			return new ResponseEntity<String>(geeson.toJson(toReturn.substring(2)),HttpStatus.OK);


		}
		//catch ( EventNotFoundException e){
		//	return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);

		//}
		catch (Exception e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/tixViewBuildings", method = RequestMethod.GET)
	@ResponseBody
	public String viewBuildings(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			Set<Building> buildings = buildingRepository.fetchAllBuildings();
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
			System.out.println("BUILDING IS " + json);

			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}


	@PreAuthorize("hasAnyAuthority('ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateCategory(@RequestBody String category,HttpServletRequest rq) throws UserNotFoundException {
		System.err.println("update cat");

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
			Long catId = (Long)jsonObject.get("categoryId");
			double price = -1;
			System.err.println("gotten catid " + catId);
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
			System.err.println("gotten info of " + name + " " + price + " " + numTickets + " cat id: " + catId);

			boolean bl = ticketingService.updateCategory(catId, name, price, numTickets);
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




	/*



	/*	
	 * SCHEDULE TASK IN DELETING EVERYTHING OF FILE TYPE...
	System.out.println(rq.getSession().getServletContext().getRealPath("/"));
	File dir = new File(rq.getSession().getServletContext().getRealPath("/"));
	File[] directoryListing = dir.listFiles();
	if (directoryListing != null) {
	    for (File child : directoryListing) {
	      System.err.println("file is " + child.getAbsolutePath() + " " + child.getName() + child.lastModified());
	    }
	  } else {
	    System.err.println("not a dir");
	  }*/
}

