package application.controller;

import java.io.File;
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
import application.exception.UserNotFoundException;
import application.service.BookingService;
import application.service.EmailService;
import application.service.EventExternalService;
import application.service.EventOrganizerService;
import application.service.MessageService;
import application.service.PaymentPlanService;
import application.service.UserService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Controller
@RequestMapping("/payment")
public class PaymentPlanController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentPlanController.class);
	private ApplicationContext context;
	private final EventExternalService eventExternalService;
	private final PaymentPlanService paymentPlanService;
	private final UserService userService;
	private final MessageService messageService;
	private final EmailService emailService;
	//private final EventCreateFormValidator eventCreateFormValidator;
	private JSONParser parser = new JSONParser();
	
	@Autowired
	public PaymentPlanController(EventExternalService eventService, PaymentPlanService paymentPlanService,
			UserService userService, MessageService messageService, EmailService emailService) {
		super();
		this.eventExternalService = eventService;
		this.paymentPlanService = paymentPlanService;;
		this.userService = userService;
		this.messageService = messageService;
		this.emailService = emailService;
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
						    obj1.put("total", price);
						    obj1.put("deposit", formatter.format(payPol.getDepositRate()*price));
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
							System.err.println("HELLOOOOOOOOOOOOO");	
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
							for(User u : eventOrgs){
								JSONObject obj1 = new JSONObject();
								obj1.put("id", u.getId());
								System.out.println("event org name is "+u.getName());
							    obj1.put("name", u.getName());
							    obj1.put("email", u.getEmail());
							    obj1.put("outstanding",paymentPlanService.getOutstandingById(u.getId()));
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
									PaymentPlan p = ev.getPaymentPlan();
									payments.add(p);
								}
								 System.out.println("finishing getting list of events");
								return new ResponseEntity<Set<PaymentPlan>>(payments,HttpStatus.OK);			
						}
						catch (Exception e){
							return new ResponseEntity<Set<PaymentPlan>>(HttpStatus.CONFLICT);
						}
					}
					
					
					
					@RequestMapping(value = "/getPaymentPlan/{id}", method = RequestMethod.GET)
					@ResponseBody
					public ResponseEntity<PaymentPlan> getPaymentPlan(@PathVariable("id") String planId, HttpServletRequest rq) throws UserNotFoundException{
						System.out.println("startADD");
						Principal principal = rq.getUserPrincipal();
						Optional<User> usr = userService.getUserByEmail(principal.getName());
						if ( !usr.isPresent() ){
							return new ResponseEntity<PaymentPlan>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
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
						    return new ResponseEntity<PaymentPlan>(policy, HttpStatus.OK);
						}
						catch (Exception e){
							return new ResponseEntity<PaymentPlan>(HttpStatus.CONFLICT);
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
							//Double subsequent = (Double)jsonObject.get("subsequent");
							boolean bl = paymentPlanService.updateAmountPaidByOrg(client, user, paymentId, cheque, amount);
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
								    obj1.put("email", ev.getEventOrg().getEmail());
								    System.out.println(ev.getEventOrg().getEmail());
								    PaymentPlan pay= ev.getPaymentPlan();
								    obj1.put("ticket",pay.getTicketRevenue());
								    System.out.println("TOTAL2");
								    Double balance = pay.getPayable();
								    obj1.put("outstanding",balance);
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

					@RequestMapping(value = "/getPaymentViaEvent/{id}", method = RequestMethod.GET)
					@ResponseBody
					public ResponseEntity<PaymentPlan> getPaymentViaEvent(@PathVariable("id") String planId, HttpServletRequest rq) throws UserNotFoundException{
						System.out.println("startADD");
						Principal principal = rq.getUserPrincipal();
						Optional<User> usr = userService.getUserByEmail(principal.getName());
						if ( !usr.isPresent() ){
							return new ResponseEntity<PaymentPlan>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
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
						    return new ResponseEntity<PaymentPlan>(policy, HttpStatus.OK);
						}
						catch (Exception e){
							return new ResponseEntity<PaymentPlan>(HttpStatus.CONFLICT);
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
							//Double subsequent = (Double)jsonObject.get("subsequent");
							boolean bl = paymentPlanService.updateOutgoingPayment(client, user, paymentId, amount);
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
							System.out.println("There are X events and X is "+ payments.size());
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
								for(Payment p: payments){
									JSONObject obj1 = new JSONObject();
									obj1.put("id", p.getId());
									System.out.println("payment id is "+p.getId());
								    obj1.put("date", String.valueOf(p.getPaid()));								    
								    obj1.put("plan",p.getPlan());
								    System.out.println("TOTAL1");
								    obj1.put("amount",p.getAmount());
								    System.out.println("TOTAL2");
								    obj1.put("cheque",p.getCheque());
								    System.out.println("TOTAL3");
									jArray.add(obj1);
								}
								 System.out.println("finishing getting list of payments");
								return new ResponseEntity<String>(jArray.toString(),HttpStatus.OK);			
						}
						catch (Exception e){
							return new ResponseEntity<String>(HttpStatus.CONFLICT);
						}
					}
					
					
					
					@RequestMapping(value = "/downloadInvoice", method = RequestMethod.POST, produces = "application/pdf")
					@ResponseBody
					public void downloadInvoice(String info,HttpServletRequest request,HttpServletResponse response) throws JRException, IOException, UserNotFoundException {
						System.out.println("Enter");
						InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/Invoice.jasper");
						response.setContentType("application/pdf");
						Principal principal = request.getUserPrincipal();
						response.setHeader("Content-disposition", "attachment; filename=Invoice.pdf");
						ServletOutputStream outputStream = response.getOutputStream();
						HashMap<String,Object> parameters = new HashMap<String,Object>();
						StringBuilder sb = new StringBuilder();
						sb.append(" ");
						Object obj;
						try {
							obj = parser.parse(info);
							JSONObject jsonObject = (JSONObject) obj;
							Long paymentId = (Long)jsonObject.get("id");
                            PaymentPlan p = paymentPlanService.getPaymentPlanById(paymentId).get();
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
							sb.append(" P.ID = ");
							sb.append(paymentId);
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
						} catch (ParseException e1) {
							System.out.println("at /download invoice there was an error parsing the json string received");
							e1.printStackTrace();
						}
					}
					

					@RequestMapping(value = "/downloadReport", method = RequestMethod.POST, produces = "application/pdf")
					@ResponseBody
					public void downloadReport(HttpServletRequest request,HttpServletResponse response) throws JRException, IOException, UserNotFoundException {
						System.out.println("Enter");
						InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/Invoice.jasper");
						response.setContentType("application/pdf");
						Principal principal = request.getUserPrincipal();
						response.setHeader("Content-disposition", "attachment; filename=Invoice.pdf");
						ServletOutputStream outputStream = response.getOutputStream();
						HashMap<String,Object> parameters = new HashMap<String,Object>();
						StringBuilder sb = new StringBuilder();
						sb.append(" ");
						Object obj;
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
						Date d1 = cal.getTime();
						System.out.println("FIRST DAY IS "+d1);
						cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
						Date d2 = cal.getTime();
						System.out.println("LAST DAY IS "+d2);
					
							sb.append("WHERE PAID >= ");
							sb.append(d1);
							sb.append(" AND PAID <=");
							sb.append(d2);
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

