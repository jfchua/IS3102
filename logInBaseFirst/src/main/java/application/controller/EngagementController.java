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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.AuditLog;
import application.entity.Beacon;
import application.entity.Building;
import application.entity.Discount;
import application.entity.Level;
import application.entity.User;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
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
	private final AuditLogRepository auditLogRepository;
	Gson gson = new Gson();
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();

	@Autowired
	public EngagementController(AuditLogRepository auditLogRepository,BuildingRepository buildingRepository, CategoryRepository categoryRepository, FeedbackRepository feedbackRepository,EngagementService engagementService, EventService eventService, TicketingService ticketingService, EventExternalService eeventService, BookingService bookingService,
			UserService userService) {
		super();
		this.eventExternalService = eeventService;
		this.auditLogRepository = auditLogRepository;
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
	public ResponseEntity<String> viewDiscount(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {

		/*		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		try{

			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;

			String id = (String)jsonObject.get("discount");

			Discount dis = engagementService.getDiscount(id);
			System.out.println(discount + dis.getDiscountMessage());
			//Gson gson = new Gson();
			//String json = gson.toJson(buildings);
			//System.out.println("Returning buildings with json of : " + json);
			//return json;	



			String json = gson.toJson(dis);
			System.out.println("discount IS " + json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}
		catch (Exception e){
			System.err.println(e.getMessage());
			Discount d = new Discount();
			d.setDiscountMessage("Sorry, the QR code is invalid");
			d.setQRCode("filler");
			d.setRetailerName(" ");
			return new ResponseEntity<String>(gson.toJson(d),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/addDiscount", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addDiscount(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			System.out.println(discount);
			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");
			String name = (String)jsonObject.get("retailerName");
			String msg = (String)jsonObject.get("message");

			boolean bl = engagementService.addDiscount(usr.get().getEmail(),eventId, name, msg);
			if ( !bl ){
				return new ResponseEntity<String>(gson.toJson("Server error in adding discount"),HttpStatus.INTERNAL_SERVER_ERROR);

			}
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Event");
			al.setAction("Add discount for event ID:  " + eventId + " for " + name);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(gson.toJson("Server error in adding discount"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getDiscounts", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getDiscounts(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {

		/*		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		try{

			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");

			Set<Discount> discounts = engagementService.getDiscounts(eventId);
			Gson gson = new Gson();

			return new ResponseEntity<String>(gson.toJson(discounts),HttpStatus.OK);
		}
		catch (Exception e){

			return new ResponseEntity<String>(gson.toJson("Server error in getting discount"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/deleteDiscount", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteDiscount(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{

			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");

			boolean discounts = engagementService.deleteDiscount(usr.get(), Long.valueOf(eventId));
			if ( !discounts){
				return new ResponseEntity<String>(gson.toJson("Server error in getting discount"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Gson gson = new Gson();
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Event");
			al.setAction("Delete discount of ID:  " + eventId);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
			return new ResponseEntity<String>(gson.toJson(discounts),HttpStatus.OK);
		}
		catch (Exception e){
			System.out.println("Error at controller delete discount" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in getting discount"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/updateDiscount", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateDiscount(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{

			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;
			Long discountId = (Long)jsonObject.get("discountId");
			String name = (String)jsonObject.get("retailerName");
			String msg = (String)jsonObject.get("message");

			boolean discounts = engagementService.updateDiscount(discountId, name, msg);
			if ( !discounts){
				return new ResponseEntity<String>(gson.toJson("Server error in updating discount"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Gson gson = new Gson();
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Event");
			al.setAction("Update discount of ID:  " + discountId);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
			return new ResponseEntity<String>(gson.toJson(discounts),HttpStatus.OK);
		}
		catch (Exception e){
			System.out.println("Error at controller updating discount" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in updating discount"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(value = "/deleteBeacon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteBeacon(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{

			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;
			//Long eventId = (Long)jsonObject.get("eventId");
			Long beaconId = (Long)jsonObject.get("beaconId");

			boolean discounts = engagementService.deleteBeacon(usr.get(), beaconId);
			if ( !discounts){
				return new ResponseEntity<String>(gson.toJson("Server error in deleting beacon"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Event");
			al.setAction("Delete beacon of ID:  " + beaconId);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e){
			System.out.println("Error at controller delete beacon" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in deleting beacon"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/updateBeacon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateBeacon(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			System.err.println("inside update");
			Object obj1 = parser.parse(discount);
			JSONObject jsonObject = (JSONObject) obj1;
			Long beaconId = (Long)jsonObject.get("beaconId");
			String msg = (String)jsonObject.get("message");
			System.err.println("finish update" + beaconId);
			boolean bl = engagementService.updateBeacon(beaconId, msg);
			if ( !bl){
				return new ResponseEntity<String>(gson.toJson("Server error in updating beacon"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Event");
			al.setAction("Update beacon of ID:  " + beaconId);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e){
			System.out.println("Error at controller updating beacon" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in updating beacon"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/addBeacon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addBeacon(@RequestBody String discount, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			//System.err.println("*********Start");
			Object obj1 = parser.parse(discount);

			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");
			String uuid = (String)jsonObject.get("beaconId");
			String msg = (String)jsonObject.get("message");
			//System.err.println("error"+msg);
			boolean bl = engagementService.addBeacon(eventId, uuid, msg);
			if ( !bl ){
				return new ResponseEntity<String>(gson.toJson("Server error in adding beacon"),HttpStatus.INTERNAL_SERVER_ERROR);

			}
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Event");
			al.setAction("Add beacon of UUID:  " + uuid + " to event of ID: " + eventId);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(gson.toJson("Server error in adding beacon"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getBeacons", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getBeacons(@RequestBody String beacon, HttpServletRequest rq) throws UserNotFoundException {

		/*		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		try{

			Object obj1 = parser.parse(beacon);

			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");

			Set<Beacon> beacons = engagementService.getBeacons(eventId);
			Gson gson = new Gson();

			return new ResponseEntity<String>(gson.toJson(beacons),HttpStatus.OK);
		}
		catch (Exception e){

			return new ResponseEntity<String>(gson.toJson("Server error in getting discount"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getBeaconMessage", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> getBeaconMessage(@RequestBody String beacon, HttpServletRequest rq) throws UserNotFoundException {

		/*		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
		try{

			Object obj1 = parser.parse(beacon);

			JSONObject jsonObject = (JSONObject) obj1;
			String eventId = (String)jsonObject.get("beaconUUID");

			String message = engagementService.getBeaconMessage(eventId);
			Gson gson = new Gson();

			return new ResponseEntity<String>(gson.toJson(message),HttpStatus.OK);
		}
		catch (Exception e){

			return new ResponseEntity<String>(gson.toJson("Server error in getting beacon message"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
