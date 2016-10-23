package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
//import org.hibernate.metamodel.source.annotations.xml.mocker.SchemaAware.SecondaryTableSchemaAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import application.exception.UserNotFoundException;
import application.service.BookingService;
import application.service.EventExternalService;
import application.service.EventOrganizerService;
import application.service.PaymentPlanService;
import application.service.UserService;

@Controller
@RequestMapping("/payment")
public class PaymentPlanController {
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentPlanController.class);
	private final EventExternalService eventExternalService;
	private final PaymentPlanService paymentPlanService;
	private final UserService userService;
	//private final EventCreateFormValidator eventCreateFormValidator;
	private JSONParser parser = new JSONParser();
	
	@Autowired
	public PaymentPlanController(EventExternalService eventService, PaymentPlanService paymentPlanService,
			UserService userService) {
		super();
		this.eventExternalService = eventService;
		this.paymentPlanService = paymentPlanService;;
		this.userService = userService;
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
					
}

