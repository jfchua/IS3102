package application.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
//import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.AuditLog;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Icon;
import application.entity.PaymentPolicy;
import application.entity.SpecialRate;
import application.entity.User;
import application.entity.Vendor;
import application.exception.UserNotFoundException;
import application.repository.VendorRepository;
import application.service.BuildingService;
import application.service.ClientOrganisationService;
import application.service.PaymentPolicyService;
import application.service.SpecialRateService;
import application.service.UserService;
import application.repository.AuditLogRepository;

@Controller
@RequestMapping("/policy")
public class PaymentPolicyController {
	@Autowired
	private final PaymentPolicyService paymentPolicyService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final AuditLogRepository auditLogRepository;

	private JSONParser parser = new JSONParser();

	@Autowired
	public PaymentPolicyController(AuditLogRepository auditLogRepository, PaymentPolicyService paymentPolicyService, ClientOrganisationService clientOrganisationService,
			UserService userService) {
		super();
		this.paymentPolicyService = paymentPolicyService;
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.auditLogRepository = auditLogRepository;
	}

	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/viewPaymentPolicy", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<PaymentPolicy> viewPaymentPolicy(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<PaymentPolicy>(HttpStatus.CONFLICT);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
		    System.out.println("start view");
			PaymentPolicy payment = client.getPaymentPolicy();
			//System.out.println(payment.getDepositRate());   
			Gson gson2 = new Gson();
			String json = gson2.toJson(payment);
		    if(payment != null)
			   System.out.println(json);
		    return new ResponseEntity<PaymentPolicy>(payment, HttpStatus.OK);
			}
			catch (Exception e){
				return new ResponseEntity<PaymentPolicy>(HttpStatus.CONFLICT);
			}
			//return new ResponseEntity<Void>(HttpStatus.OK);
		}

	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
		@RequestMapping(value = "/getPaymentPolicy/{id}", method = RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<PaymentPolicy> getPaymentPolicy(@PathVariable("id") String vendorId, HttpServletRequest rq) {
			try{			
				long id = Long.parseLong(vendorId);
				PaymentPolicy policy = paymentPolicyService.getPaymentPolicy(id).get();
				Gson gson2 = new Gson();
				String json = gson2.toJson(policy);
			    System.out.println(json);
			    return new ResponseEntity<PaymentPolicy>(policy, HttpStatus.OK);
			}
			catch (Exception e){
				return new ResponseEntity<PaymentPolicy>(HttpStatus.CONFLICT);
			}
		}

		//Security filters for inputs needs to be added
		//This method takes in a string which contains the attributes of the event to be added.
		//Call $http.post(URL,stringToAdd);
		@RequestMapping(value = "/addPaymentPolicy", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<Void> addPaymentPolicy(@RequestBody String rateJSON,
				HttpServletRequest rq) throws UserNotFoundException {
			System.out.println("start adding");
			Principal principal = rq.getUserPrincipal();
			Optional<User> usr = userService.getUserByEmail(principal.getName());
			if ( !usr.isPresent() ){
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
			}
			try{
				ClientOrganisation client = usr.get().getClientOrganisation();
				System.out.println(client.getOrganisationName());
				Object obj = parser.parse(rateJSON);
				JSONObject jsonObject = (JSONObject) obj;
	            Double rate = (Double)jsonObject.get("depositRate");
	            System.out.println(rate);
	            System.out.println("rate2");
				int subNum = ((Long)jsonObject.get("subsequentNumber")).intValue();
				//String description = (String)jsonObject.get("description");	
				int due = ((Long)jsonObject.get("dueDays")).intValue();
				int period = ((Long)jsonObject.get("interimPeriod")).intValue();
				boolean bl = paymentPolicyService.createPaymentPolicy(client, rate, subNum, due, period);
				System.out.println("adding rate " + rate);
				if(!bl){
					System.out.println("invalid rate");
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
					
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			return new ResponseEntity<Void>(HttpStatus.OK);	
		}	

		//This method takes in a String which is the ID of the event to be deleted
				// Call $http.post(URL,(String)id);
				@RequestMapping(value = "/deletePaymentPolicy", method = RequestMethod.POST)
				@ResponseBody
				public ResponseEntity<Void> deletePaymentPolicy(@RequestBody String rateJSON, HttpServletRequest rq) throws UserNotFoundException {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return new ResponseEntity<Void>(HttpStatus.CONFLICT); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();
						System.out.println("Start deleting");
						Object obj = parser.parse(rateJSON);
						JSONObject jsonObject = (JSONObject) obj;
						long rateId = (Long)jsonObject.get("id");
						System.out.println(rateId);	
						boolean bl=paymentPolicyService.deletePaymentPolicy(client, rateId);
						System.out.println(bl);	
					}
					catch (Exception e){
						return new ResponseEntity<Void>(HttpStatus.CONFLICT);
					}
					return new ResponseEntity<Void>(HttpStatus.OK);
				}	
			
				//This method takes in a JSON format which contains an object with 5 attributes
				//Long/String id, int levelNum, int length, int width, String filePath
				//Call $httpPost(Url,JSONData);
				@RequestMapping(value = "/updatePaymentPolicy", method = RequestMethod.POST)
				@ResponseBody
				public ResponseEntity<Void> updatePaymentPolicy(@RequestBody String rateJSON, HttpServletRequest rq) throws UserNotFoundException {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return new ResponseEntity<Void>(HttpStatus.CONFLICT); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();
						System.out.println("start updating");
						Object obj = parser.parse(rateJSON);
						JSONObject jsonObject = (JSONObject) obj;
						long rateId = (Long)jsonObject.get("id");
						Double rate = (Double)jsonObject.get("depositRate");
			            System.out.println(rate);
			            System.out.println("rate2");
						int subNum = ((Long)jsonObject.get("subsequentNumber")).intValue();
						int due = ((Long)jsonObject.get("dueDays")).intValue();
						int period = ((Long)jsonObject.get("interimPeriod")).intValue();
						boolean bl = paymentPolicyService.updatePaymentPolicy(client, rateId, rate, subNum, due, period);
						System.out.println("editing rate " + rateId);
						if(!bl){
							return new ResponseEntity<Void>(HttpStatus.CONFLICT);
						}
					}
					catch (Exception e){
						return new ResponseEntity<Void>(HttpStatus.CONFLICT);
					}
					return new ResponseEntity<Void>(HttpStatus.OK);
				}
				
		
		
}
