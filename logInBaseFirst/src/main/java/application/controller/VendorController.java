package application.controller;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.ClientOrganisation;
import application.entity.User;
import application.entity.Vendor;
import application.exception.InvalidEmailException;
import application.exception.UserNotFoundException;
import application.exception.VendorNotFoundException;
import application.repository.VendorRepository;
import application.service.ClientOrganisationService;
import application.service.UserService;
import application.service.VendorService;

@Controller
@RequestMapping("/vendor")
public class VendorController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private final VendorService vendorService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private VendorRepository vendorRepository;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();

	@Autowired
	public VendorController(VendorService vendorService, ClientOrganisationService clientOrganisationService,
			VendorRepository vendorRepository, UserService userService) {
		super();
		this.vendorService = vendorService;	
		this.clientOrganisationService = clientOrganisationService;
		this.vendorRepository = vendorRepository;
		this.userService = userService;
	}
	/*
	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(sendMessageFormValidator);
	}*/

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/getVendor/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getVendor(@PathVariable("id") String vendorId, HttpServletRequest rq) {
		try{

			long id = Long.parseLong(vendorId);
			Vendor vendor = vendorService.getVendorById(id).get();
			Gson gson2 = new Gson();
			String json = gson2.toJson(vendor);
			System.out.println(json);
			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}


	@PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_PROPERTY')")
	//Security filters for inputs needs to be added
	//This method takes in a string which contains the attributes of the event to be added.
	//Call $http.post(URL,stringToAdd);
	@RequestMapping(value = "/addVendor", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addVendor(@RequestBody String vendorJSON,
			HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start adding");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println(client.getOrganisationName());
			Object obj = parser.parse(vendorJSON);
			JSONObject jsonObject = (JSONObject) obj;
			String email = (String)jsonObject.get("email");
			System.out.println("email1");
			System.out.println(email);
			System.out.println("email2");
			String name = (String)jsonObject.get("name");
			String registration = (String)jsonObject.get("registration");
			String description = (String)jsonObject.get("description");
			String contact = (String)jsonObject.get("contact");	
			String regex = "^[0-9]{9}[A-Z]{1}$";
			Pattern pat = Pattern.compile(regex);
			Matcher get = pat.matcher(registration);		
			if(!get.matches()){
				return new ResponseEntity<String>(geeson.toJson("Please enter a valid registration number."),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			boolean bl = vendorService.createVendor(client, email, name, registration, description, contact);
			System.out.println("adding vendor " + name);
			if(!bl){
				return new ResponseEntity<String>(geeson.toJson("Server error in creating vendor"),HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
		catch (InvalidEmailException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);	
	}	

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewAllVendors",  method = RequestMethod.GET)
	@ResponseBody
	public String viewAllVendors(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR"; //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("start view");
			Set<Vendor> vendors = client.getVendors();
			System.out.println("There are " + vendors.size() + " vendors");   
			Gson gson2 = new Gson();
			String json = gson2.toJson(vendors);
			System.out.println(json);
			return json;
		}
		catch (Exception e){
			return "Error fetching vendors";
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//This method takes in a String which is the ID of the event to be deleted
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteVendor", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteVendor(@RequestBody String vendorJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("Start deleting");
			Object obj = parser.parse(vendorJSON);
			JSONObject jsonObject = (JSONObject) obj;
			long vendorId = (Long)jsonObject.get("vendorId");
			System.out.println(vendorId);	
			boolean bl=vendorService.deleteVendor(client, vendorId);
			System.out.println(bl);	
		}
		catch ( VendorNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in deleting vendor"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}	

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//This method takes in a JSON format which contains an object with 5 attributes
	//Long/String id, int levelNum, int length, int width, String filePath
	//Call $httpPost(Url,JSONData);
	@RequestMapping(value = "/updateVendor", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateVendor(@RequestBody String vendorJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("start updating");
			Object obj = parser.parse(vendorJSON);
			JSONObject jsonObject = (JSONObject) obj;
			long vendorId = (Long)jsonObject.get("id");
			String email = (String)jsonObject.get("email");
			String name = (String)jsonObject.get("name");
			String registration = (String)jsonObject.get("registration");
			String description = (String)jsonObject.get("description");
			String contact = (String)jsonObject.get("contact");	
			boolean bl = vendorService.editVendor( vendorId, email, name, registration, description, contact);
			System.out.println("editing vendor " + name);
			if(!bl){
				return new ResponseEntity<String>(HttpStatus.CONFLICT);
			}
		}
		catch ( VendorNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( InvalidEmailException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
