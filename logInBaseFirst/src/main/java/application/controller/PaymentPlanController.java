package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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

import application.domain.*;
import application.domain.validator.EventCreateFormValidator;
import application.service.user.BookingService;
import application.service.user.EventExternalService;
import application.service.user.EventOrganizerService;
import application.service.user.PaymentPlanService;
import application.service.user.UserService;
//import application.service.user.EventService;
//import application.service.user.UserService;

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
					public String viewAllPaymentPlans(HttpServletRequest rq) {
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
					public ResponseEntity<Void> addPaymentPlan(@RequestBody String paymentJSON,HttpServletRequest rq) {
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
							Double total = (Double)jsonObject.get("total");
							Double deposit = (Double)jsonObject.get("deposit");
							int subseNumber = (Integer)jsonObject.get("subsequent_number");
							Double subsequent = (Double)jsonObject.get("subsequent");

							boolean bl = paymentPlanService.createPaymentPlan(client, user, eventId, total, deposit, subseNumber, subsequent);
							System.out.println("adding payment plan " + total);
							/*if(bl){
								AuditLog al = new AuditLog();
								al.setTimeToNow();
								al.setSystem("Property");
								al.setAction("Add Building: " + name);
								al.setUser(usr.get());
								al.setUserEmail(usr.get().getEmail());
								auditLogRepository.save(al);
							}
							else*/
							if(!bl)
								return new ResponseEntity<Void>(HttpStatus.CONFLICT);
						}
						catch (Exception e){
							System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);
						}
						return new ResponseEntity<Void>(HttpStatus.OK);
					}	
						
}

