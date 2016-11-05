package application.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
//import org.hibernate.metamodel.source.annotations.xml.mocker.SchemaAware.SecondaryTableSchemaAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.*;
import application.exception.EventNotFoundException;
import application.exception.InvalidAttachmentException;
import application.exception.UserNotFoundException;
import application.service.BookingService;
import application.service.EmailService;
import application.service.EventExternalService;
import application.service.EventOrganizerService;
import application.service.EventService;
import application.service.MessageService;
import application.service.PaymentPlanService;
import application.service.UserService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Controller
@RequestMapping("/payment")
public class PaymentPlanController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentPlanController.class);
	@Autowired
	private ApplicationContext context;
	private final EventExternalService eventExternalService;
	private final PaymentPlanService paymentPlanService;
	private final UserService userService;
	private final EventService eventService;
	private final MessageService messageService;
	private final EmailService emailService;
	//private final EventCreateFormValidator eventCreateFormValidator;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();
	@Autowired
	public PaymentPlanController(EventExternalService eventExternalService, PaymentPlanService paymentPlanService,
			UserService userService, MessageService messageService, EmailService emailService, EventService eventService) {
		super();
		this.eventExternalService = eventExternalService;
		this.paymentPlanService = paymentPlanService;;
		this.userService = userService;
		this.messageService = messageService;
		this.emailService = emailService;
		this.eventService = eventService;
	}

	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewAllPaymentPlans",  method = RequestMethod.GET)
	@ResponseBody
	public String viewAllPaymentPlans(HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start view");
		Principal principal = rq.getUserPrincipal();
		Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
		if ( !eventOrg1.isPresent() ){
			return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{	
			User user = eventOrg1.get();
			ClientOrganisation client = user.getClientOrganisation();
			System.out.println(user.getId());
			//long id = Long.parseLong(bId);
			Set<PaymentPlan> plans = paymentPlanService.viewAllPaymentPlan(client, user);	
			System.out.println("There are " + plans.size() + " plans under this client organisation");
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
							//(f.getDeclaringClass() == Level.class && f.getUnits().equals("units"));
						}
					})
					/**
					 * Use serializeNulls method if you want To serialize null values 
					 * By default, Gson does not serialize null values
					 */
					.serializeNulls()
					.create();	    
			String json = gson2.toJson(plans);
			System.out.println(json);
			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}
	//Security filters for inputs needs to be added
	//This method takes in a string which contains the attributes of the building to be added.
	//Call $http.post(URL,stringToAdd);
	@RequestMapping(value = "/addPaymentPlan", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addPaymentPlan(@RequestBody String paymentJSON,HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(paymentJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			Long eventId = (Long)jsonObject.get("eventId");
			Event event = eventExternalService.getEventById(eventId).get();
			User org = event.getEventOrg();
			System.out.println(eventId);
			Double total = Double.valueOf((String)jsonObject.get("total"));
			System.out.println(total);
			Double deposit = Double.valueOf((String)jsonObject.get("deposit"));
			System.out.println(deposit);
			int subseNumber = ((Long)jsonObject.get("subsequent_number")).intValue();
			System.out.println(subseNumber);
			//Double subsequent = (Double)jsonObject.get("subsequent");
			boolean bl = paymentPlanService.createPaymentPlan(client, user, eventId, total, deposit, subseNumber);
			System.out.println("adding payment plan " + total);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			else{
				String  subject = "Payment plan for event ID " + eventId + " created!";
				String msg = "Your event ID " + eventId + " has a new payment plan with total amount "+total+" .";	   
				emailService.sendEmail(org.getEmail(), subject, msg);
				messageService.sendMessage(user, org, subject, msg);
			}

		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	


	@RequestMapping(value = "/checkRent", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> checkRent(@RequestBody String eventJSON,
			HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start check rent for event");
		Principal principal = rq.getUserPrincipal();
		System.out.println(principal.getName());
		Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
		if ( !eventOrg1.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User eventOrg = eventOrg1.get();
			ClientOrganisation client = eventOrg.getClientOrganisation();
			System.out.println(eventOrg.getName());
			Object obj = parser.parse(eventJSON);
			JSONObject jsonObject = (JSONObject) obj;
			Long eventId = (Long)jsonObject.get("event");	
			Double price = paymentPlanService.checkRent(client, eventId);
			PaymentPolicy payPol = client.getPaymentPolicy();

			NumberFormat formatter = new DecimalFormat("#0.00");     
			//System.out.println(formatter.format(4.0));
			JSONObject obj1 = new JSONObject();
			obj1.put("id", eventId);
			obj1.put("total", formatter.format(price));
			obj1.put("before", formatter.format(price/1.07));
			obj1.put("deposit", formatter.format(payPol.getDepositRate()*price));
			obj1.put("subsequent", formatter.format((1-payPol.getDepositRate())*price/payPol.getSubsequentNumber()));
			obj1.put("subsequentNumber",payPol.getSubsequentNumber());
			//String json = gson2.toJson(event);
			//System.out.println("EVENT IS " + json);
			System.out.println(obj1.toString());
			//System.out.println(price);
			//Gson gson2 = new Gson();
			//String json = gson2.toJson(price);
			return new ResponseEntity<String>(obj1.toString(), HttpStatus.OK);	
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}	
	}	

	@RequestMapping(value = "/updatePaymentPlan", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updatePaymentPlan(@RequestBody String paymentJSON,HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(paymentJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			Long paymentId = (Long)jsonObject.get("id");
			System.out.println(paymentId);
			Double depositRate = Double.valueOf((String)jsonObject.get("depositRate"));
			System.out.println(depositRate);
			int subseNumber = ((Long)jsonObject.get("subsequent_number")).intValue();
			System.out.println(subseNumber);
			//Double subsequent = (Double)jsonObject.get("subsequent");
			boolean bl = paymentPlanService.updatePaymentPlan(client, user, paymentId, depositRate, subseNumber);
			System.out.println("success or not?" + bl);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewApprovedEvents",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewApprovedEvents(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("Server error, user was not found"),HttpStatus.INTERNAL_SERVER_ERROR);	}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Event> events = eventService.getAllApprovedEvents(client);
			System.err.println("There are " + events.size() + " approved events");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Category.class)|| (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			Set<Event> eventsR = new HashSet<Event>();
			for(Event e: events){
				if(e.getPaymentPlan()==null)
					eventsR.add(e);
			}
			String json = gson2.toJson(eventsR);
			System.out.println(json);
			return new ResponseEntity<String>(json,HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting all events"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/viewAllOutstandingBalance", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewAllOustandingBalance(HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		JSONArray jArray = new JSONArray();
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<User> eventOrgs = userService.getExternalUsers(client);
			//System.err.println("HELLOOOOOOOOOOOOO");	
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz==Event.class)||(clazz==Role.class)||(clazz==ToDoTask.class)||(clazz==Message.class)
									||(clazz==ClientOrganisation.class);
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
			NumberFormat formatter = new DecimalFormat("#0.00");    
			for(User u : eventOrgs){
				JSONObject obj1 = new JSONObject();
				obj1.put("id", u.getId());
				//System.out.println("event org name is "+u.getName());
				obj1.put("name", u.getName());
				obj1.put("email", u.getEmail());
				if(!paymentPlanService.getOutstandingById(u.getId()).isEmpty()){
				String[] arr = paymentPlanService.getOutstandingById(u.getId()).split(" ");
				obj1.put("total",arr[0]);
				obj1.put("outstanding",arr[1]);
				obj1.put("paid",arr[2]);
				}
				else{
					obj1.put("total", "");
					obj1.put("outstanding", "");
					obj1.put("paid", "");
				}
				jArray.add(obj1);
			}
			System.out.println("finish getting all orgs oustanding amount");
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(jArray.toString(), HttpStatus.OK);
	}	


	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/viewListOfEvents/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewListOfEvents(@PathVariable("id") String orgId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();	   
			long id = Long.parseLong(orgId);
			Set<Event> events= paymentPlanService.getEventsByOrgId(client, id);
			System.out.println("There are X events and X is "+events.size());
			JSONArray jArray = new JSONArray();
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			for(Event ev : events){
				JSONObject obj1 = new JSONObject();
				obj1.put("id", ev.getId());
				System.out.println("event title is "+ev.getEvent_title());
				obj1.put("paymentStatus", String.valueOf(ev.getPaymentStatus()));
				System.out.println(ev.getPaymentStatus());
				PaymentPlan pay= ev.getPaymentPlan();
				obj1.put("rent",pay.getTotal());
				System.out.println("TOTAL1");
				obj1.put("ticket",pay.getTicketRevenue());
				System.out.println("TOTAL2");
				Double balance = pay.getPayable();
				obj1.put("balance",balance);
				System.out.println("TOTAL3" + balance);
				jArray.add(obj1);
			}
			System.out.println("finishing getting list of events");
			return new ResponseEntity<String>(jArray.toString(),HttpStatus.OK);	
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}


	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/viewListOfPaymentPlans/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Set<PaymentPlan>> viewListOfPaymentPlans(@PathVariable("id") String orgId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Set<PaymentPlan>>(HttpStatus.CONFLICT);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();	   
			long id = Long.parseLong(orgId);
			Set<Event> events= paymentPlanService.getEventsByOrgId(client, id);
			System.out.println("There are X events and X is "+events.size());
			Set<PaymentPlan> payments = new HashSet<PaymentPlan>();
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			for(Event ev : events){
				if(ev.getPaymentPlan() != null){
				PaymentPlan p = ev.getPaymentPlan();
				if(!p.getPayable().equals(0.00))
					payments.add(p);
				}
			}
			System.out.println("finishing getting list of payment plans");
			return new ResponseEntity<Set<PaymentPlan>>(payments,HttpStatus.OK);	
		}
		catch (Exception e){
			return new ResponseEntity<Set<PaymentPlan>>(HttpStatus.CONFLICT);
		}
	}



	@RequestMapping(value = "/getPaymentPlan/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getPaymentPlan(@PathVariable("id") String planId, HttpServletRequest rq) throws UserNotFoundException{
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();	
			long id = Long.parseLong(planId);
			PaymentPlan policy = paymentPlanService.getPaymentPlanById(id).get();
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
							//(f.getDeclaringClass() == Level.class && f.getUnits().equals("units"));
						}
					})
					/**
					 * Use serializeNulls method if you want To serialize null values 
					 * By default, Gson does not serialize null values
					 */
					.serializeNulls()
					.create();	
			NumberFormat formatter = new DecimalFormat("#0.00");   
			JSONObject obj1 = new JSONObject();
			obj1.put("id", policy.getId());
			obj1.put("total", formatter.format(policy.getTotal()));
			obj1.put("subsequent", formatter.format(policy.getSubsequent()));
			obj1.put("nextPayment",formatter.format(policy.getNextPayment()));
			obj1.put("nextInvoice",formatter.format(policy.getNextInvoice()));
			System.out.println("TOTAL2");
			return new ResponseEntity<String>(obj1.toString(), HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}	

	@RequestMapping(value = "/updateReceivedPayment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateReceivedPayment(@RequestBody String paymentJSON,HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(paymentJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			Long paymentId = (Long)jsonObject.get("id");
			System.out.println(paymentId);
			Double amount = (Double)jsonObject.get("amountPaid");
			System.out.println("amount: "+ amount);
			String cheque = (String)jsonObject.get("cheque");
			System.out.println("cheque: "+cheque);
			String nextInvoice = (String)jsonObject.get("nextInvoice");
			System.out.println("invoice: "+nextInvoice);
			//Double subsequent = (Double)jsonObject.get("subsequent");
			boolean bl = paymentPlanService.updateAmountPaidByOrg(client, user, paymentId, cheque, amount, nextInvoice);
			System.out.println("success or not?" + bl);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/viewAllEventsWithTicket", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewAllEventsWithTicket(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();	   	
			Set<Event> events= eventExternalService.getEventsWithTicket(client);
			System.out.println("There are X events and X is "+events.size());
			JSONArray jArray = new JSONArray();
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class)
									||(clazz == Category.class);
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
			NumberFormat formatter = new DecimalFormat("#0.00");   
			for(Event ev : events){
				JSONObject obj1 = new JSONObject();
				obj1.put("id", ev.getId());
				System.out.println("event title is "+ev.getEvent_title());
				obj1.put("email", ev.getEventOrg().getEmail());
				System.out.println(ev.getEventOrg().getEmail());
				obj1.put("title", ev.getEvent_title());
				System.out.println(ev.getEvent_title());
				obj1.put("name", ev.getEventOrg().getName());
				System.out.println(ev.getEventOrg().getName());
				Set<Category> cats = ev.getCategories();
				Double revenue = 0.00;
				int numTotal = 0;
				if(!cats.isEmpty()){
					for(Category c : cats){
						Set<Ticket> tics = c.getTickets();
						revenue += c.getPrice()*tics.size();
						numTotal += tics.size();
					}
				}
				obj1.put("ticket",formatter.format(revenue));
				System.out.println("TOTAL2" + formatter.format(revenue));
				obj1.put("number",String.valueOf(numTotal));
				System.out.println("TOTAL2" + numTotal);
				if ( ev.getPaymentPlan() != null ){
					PaymentPlan pay = ev.getPaymentPlan();
					Double balance = pay.getPayable();
					obj1.put("outstanding",formatter.format(balance));
					System.out.println("TOTAL3" + balance);
				}
				jArray.add(obj1);
			}
			System.out.println("finishing getting list of events" + jArray.toString());
			return new ResponseEntity<String>(jArray.toString(),HttpStatus.OK);	
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}

	@RequestMapping(value = "/getPaymentViaEvent/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getPaymentViaEvent(@PathVariable("id") String planId, HttpServletRequest rq) throws UserNotFoundException{
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}

		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();	
			long id = Long.parseLong(planId);
			PaymentPlan policy = paymentPlanService.getPaymentPlanByEvent(client, id);
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
							//(f.getDeclaringClass() == Level.class && f.getUnits().equals("units"));
						}
					})
					/**
					 * Use serializeNulls method if you want To serialize null values 
					 * By default, Gson does not serialize null values
					 */
					.serializeNulls()
					.create();	
			NumberFormat formatter = new DecimalFormat("#0.00"); 
			JSONObject obj1 = new JSONObject();
			obj1.put("id", policy.getId());
			obj1.put("total", String.valueOf(policy.getTotal()));
			obj1.put("payable",formatter.format(policy.getPayable()));
			obj1.put("ticketRevenue",formatter.format(policy.getTicketRevenue()));
			return new ResponseEntity<String>(obj1.toString(), HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}	

	@RequestMapping(value = "/updateTicketRevenue", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateTicketRevenue(@RequestBody String paymentJSON,HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(paymentJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			Long paymentId = (Long)jsonObject.get("id");
			System.out.println(paymentId);
			Double amount = Double.valueOf((String)jsonObject.get("ticket"));
			//String cheque = (String)jsonObject.get("cheque");
			System.out.println("ticket: "+ amount);
			//Double subsequent = (Double)jsonObject.get("subsequent");
			boolean bl = paymentPlanService.updateTicketRevenue(client, user, paymentId, amount);
			System.out.println("success or not?" + bl);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	

	@RequestMapping(value = "/updateOutgoingPayment", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateOutgoingPayment(@RequestBody String paymentJSON,HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("startADD");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User user = usr.get();
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(paymentJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			Long paymentId = (Long)jsonObject.get("id");
			System.out.println(paymentId);
			Double amount = Double.valueOf((String)jsonObject.get("toBePaid"));
			System.out.println("ticket: "+ amount);
			String cheque = (String)jsonObject.get("cheque");
			//Double subsequent = (Double)jsonObject.get("subsequent");
			boolean bl = paymentPlanService.updateOutgoingPayment(client, user, paymentId, amount, cheque);
			System.out.println("success or not?" + bl);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/getPaymentHistory/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getPaymentHistory(@PathVariable("id") String orgId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();	   
			long id = Long.parseLong(orgId);
			Set<Payment> payments= paymentPlanService.getPaymentsByOrgId(client, id);
			System.err.println("There are X events and X is "+ payments.size());
			JSONArray jArray = new JSONArray();
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class)||(clazz == BookingAppl.class)||(clazz == PaymentPlan.class);
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
			NumberFormat formatter = new DecimalFormat("#0.00"); 
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
			for(Payment p: payments){
				if(p.getCheque()!=null){
					JSONObject obj1 = new JSONObject();
					obj1.put("id", p.getId());	
					String[] arr1 = String.valueOf(sdf.format(p.getPaid())).split(" ");
					System.err.println("payment date is "+ arr1[0]);
					obj1.put("date", arr1[0]);	    
					obj1.put("plan",p.getPlan());
					System.err.println("TOTAL1");	
					obj1.put("amount",formatter.format(p.getAmount()));
					System.out.println("TOTAL2");
					obj1.put("cheque",p.getCheque());
					System.out.println("TOTAL3");
					obj1.put("invoice",p.getInvoice());
					jArray.add(obj1);
				}
			}
			System.out.println("finishing getting list of payments");
			return new ResponseEntity<String>(jArray.toString(),HttpStatus.OK);	
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}



	@RequestMapping(value = "/downloadInvoice", method = RequestMethod.POST, produces = "application/pdf")
	public void downloadInvoice(@RequestBody String info,HttpServletRequest request,HttpServletResponse response) throws JRException, IOException, UserNotFoundException, InvalidAttachmentException {
		System.err.println("Enter");
		InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/Invoice.jasper");
		response.setContentType("application/pdf");
		Principal principal = request.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		ClientOrganisation client = usr.get().getClientOrganisation();
		//response.setHeader("Content-disposition", "attachment; filename=Invoice.pdf");
		//ServletOutputStream outputStream = response.getOutputStream();
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		Object obj;
		System.err.println("before try");
		try {
			/*
	if(info == null)
	System.out.println("********** info is null");

	if(parser == null)
	System.out.println("********** parser is null");

	System.out.println("********** HERE");*/

			obj = parser.parse(info);
			JSONObject jsonObject = (JSONObject) obj;
			Long eventId = (Long)jsonObject.get("id");
			System.err.println("display id " + eventId);
			PaymentPlan p = paymentPlanService.getPaymentPlanByEvent(client,eventId);
			Event event = p.getEvent();
			User user = event.getEventOrg();
			sb.append(" P.ID = ");
			sb.append(p.getId());
			System.err.println("Query parameter is : " + sb.toString());
			parameters.put("criteria", sb.toString());

			Connection conn = null;
			try {
				DataSource ds = (DataSource)context.getBean("dataSource");
				conn = ds.getConnection();
				System.out.println(conn.toString());
			} catch (SQLException e) {
				System.out.println("************* ERROR: " + e.getMessage());
				e.printStackTrace();
			}
			String path = request.getSession().getServletContext().getRealPath("/");
			System.err.println("path is " + path);
			path += "Invoice" + p.getId() + ".pdf";
			File f = new File(path);
			int counter = 1;
			String invoice = "";
			if ( !f.exists()){
				parameters.put("number", String.valueOf(p.getId()));
				invoice = String.valueOf(p.getId());
			}
			else{
				while ( f.exists() && !f.isDirectory() ){
					counter++;
					path = request.getSession().getServletContext().getRealPath("/");
					path += "Invoice" + p.getId() + "-" + counter + ".pdf";	

					f = new File(path);
					invoice = p.getId() + "-" + counter;
				}
				parameters.put("number", p.getId() + "-" + counter );
			}
			System.out.println("invoice is "+invoice);
			boolean bl = paymentPlanService.generatePayment(client, p.getId(), invoice);
			System.out.println("*******GENERATE PAYMENT????"+bl);
			System.err.println("path is " + path);
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			JasperRunManager.runReportToPdfStream(jasperStream, fileOutputStream, parameters,conn);
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("FLUSHED OUT THE LOG");
			//User usr = userService.getUserByEmail(principal.getName()).get();
			//ClientOrganisation client = usr.getClientOrganisation();
			PaymentPolicy paypol = client.getPaymentPolicy();
			String email = "Please pay the amount stated in the invoice within "+ paypol.getNumOfDueDays() +
					" days.";
			emailService.sendEmailWithAttachment(user.getEmail(), "Invoice for payment plan id "+ p.getId(), email , path);


		} catch (ParseException e1) {
			System.out.println("at /download invoice there was an error parsing the json string received");
			e1.printStackTrace();
		}
	}

	@RequestMapping(value = "/downloadInvoiceForUpdate", method = RequestMethod.POST, produces = "application/pdf")
	public void downloadInvoiceForUpdate(@RequestBody String info,HttpServletRequest request,HttpServletResponse response) throws JRException, IOException, UserNotFoundException, InvalidAttachmentException {
		System.out.println("Enter");
		InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/Invoice.jasper");
		response.setContentType("application/pdf");
		Principal principal = request.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		ClientOrganisation client = usr.get().getClientOrganisation();
		//response.setHeader("Content-disposition", "attachment; filename=Invoice.pdf");
		//ServletOutputStream outputStream = response.getOutputStream();
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		Object obj;
		try {

			obj = parser.parse(info);
			JSONObject jsonObject = (JSONObject) obj;
			Long paymentId = (Long)jsonObject.get("id");
			PaymentPlan p = paymentPlanService.getPaymentPlanById(paymentId).get();
			Event event = p.getEvent();
			User user = event.getEventOrg();
			sb.append(" P.ID = ");
			sb.append(paymentId);
			System.err.println("Query parameter is : " + sb.toString());
			parameters.put("criteria", sb.toString());

			Connection conn = null;
			try {
				DataSource ds = (DataSource)context.getBean("dataSource");
				conn = ds.getConnection();
				System.out.println(conn.toString());
			} catch (SQLException e) {
				System.out.println("************* ERROR: " + e.getMessage());
				e.printStackTrace();
			}
			String path = request.getSession().getServletContext().getRealPath("/");
			//System.err.println("path is " + path);
			//path += "Invoice" + paymentId + ".pdf";
			
			//String path = request.getSession().getServletContext().getRealPath("/");
			//System.err.println("path is " + path);
			//path += "Invoice" + p.getId() + ".pdf";
			//File f = new File(path);
			
			
			int counter = 2;
			String invoice = "";
			PaymentPlan pay = paymentPlanService.getPaymentPlanById(paymentId).get();
			Set<Payment> pays = pay.getPayments();
			if ( pays.size()==1){
				invoice = String.valueOf(paymentId + "-" + counter);
				parameters.put("number", invoice);			
			}
			else{
				Iterator iter = pays.iterator();
				while (iter.hasNext()){
					counter++;							
				}
				invoice = (paymentId + "-" + counter);		
				//invoice = String.valueOf(paymentId + "-" + pays.size());
				System.err.println(invoice);
				parameters.put("number", invoice);
			}
			path += "Invoice" + invoice + ".pdf";
			File f = new File(path);
			System.out.println("invoice is "+invoice);
			boolean bl = paymentPlanService.updatePayment(client, paymentId, invoice);
			System.out.println("*******GENERATE PAYMENT????"+bl);
			System.err.println("path is " + path);
			FileOutputStream fileOutputStream = new FileOutputStream(path);
			JasperRunManager.runReportToPdfStream(jasperStream, fileOutputStream, parameters,conn);
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("FLUSHED OUT THE LOG");
			//User usr = userService.getUserByEmail(principal.getName()).get();
			//ClientOrganisation client = usr.getClientOrganisation();
			PaymentPolicy paypol = client.getPaymentPolicy();
			String email = "Please pay the amount stated in the invoice within "+ paypol.getNumOfDueDays() +
					" days.";
			emailService.sendEmailWithAttachment(user.getEmail(), "Invoice for payment plan id "+paymentId, email , path);


		} catch (ParseException e1) {
			System.out.println("at /download invoice there was an error parsing the json string received");
			e1.printStackTrace();
		}
	}

	@RequestMapping(value = "/downloadReport", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public void downloadReport(HttpServletRequest request,HttpServletResponse response) throws JRException, IOException, UserNotFoundException {
		System.out.println("Enter");
		InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/payment.jasper");
		response.setContentType("application/pdf");
		Principal principal = request.getUserPrincipal();
		response.setHeader("Content-disposition", "attachment; filename=payment.pdf");
		ServletOutputStream outputStream = response.getOutputStream();
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		Object obj;
		Calendar cal = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
		//DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date d1 = cal.getTime();
		String[] arr1 = String.valueOf(sdf.format(d1)).split(" ");
		//System.out.println("FIRST DAY IS "+ str1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date d2 = cal.getTime();
		String[] arr2 = String.valueOf(sdf.format(d2)).split(" ");
		//System.out.println("LAST DAY IS "+ str2);

		sb.append("WHERE (PAID >= '");
		sb.append(arr1[0] + " 00:00:00 ' AND PAID <= '");

		//sb.append(arr1[0] +" AND PAID <= " + arr2[0]);
		sb.append(arr2[0] +  " 23:59:59 ')");
		System.err.println("Query parameter is : " + sb.toString());
		parameters.put("criteria", sb.toString());
		Connection conn = null;
		try {
			DataSource ds = (DataSource)context.getBean("dataSource");
			conn = ds.getConnection();
			System.out.println("************* ERROR: ");
			System.out.println(conn.toString());
		} catch (SQLException e) {
			System.out.println("************* ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		JasperRunManager.runReportToPdfStream(jasperStream, outputStream, parameters,conn);
		outputStream.flush();
		outputStream.close();
		System.out.println("FLUSHED OUT THE LOG");

	}


}

