package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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

import application.entity.AuditLog;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Icon;
import application.entity.Message;
import application.entity.PaymentPolicy;
import application.entity.Role;
import application.entity.SpecialRate;
import application.entity.Ticket;
import application.entity.ToDoTask;
import application.entity.UnitAttributeType;
import application.entity.User;
import application.entity.Vendor;
import application.enumeration.Subscription;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OldPasswordInvalidException;
import application.exception.OldSecurityInvalidException;
import application.exception.OrganisationNameAlreadyExistsException;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.ClientOrganisationRepository;
import application.repository.RoleRepository;
import application.repository.UserRepository;
import application.service.ClientOrganisationService;
import application.service.TicketingService;
import application.service.UserService;

@Controller
public class UserController {

	// private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	//private final UserService userService;
	private final ClientOrganisationService clientOrganisationService;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final UserService userService;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final TicketingService ticketingService;
	private final AuditLogRepository auditLogRepository;

	private JSONParser parser = new JSONParser();
	//private final UserCreateFormValidator userCreateFormValidator;

	@Autowired
	public UserController(TicketingService ticketingService, AuditLogRepository auditLogRepository, ClientOrganisationRepository clientOrganisationRepository, ClientOrganisationService clientOrganisationService,UserService userService, RoleRepository roleRepository, UserRepository userRepository) {
		//this.userService = userService;
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.ticketingService = ticketingService;
		this.roleRepository =  roleRepository;
		this.userRepository = userRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.auditLogRepository = auditLogRepository;
		//  this.userCreateFormValidator = userCreateFormValidator;
	}
	// ----------------------- START SUPER ADMIN METHODS WITH ROLE CHECKING ------------------------
	// TODO: CHECK ROLE FOR SUPERADMIN
	// TODO: ADD CLIENT ORGANISATION SUBSCRIPTIONS.
	// This method adds a client organisation.
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/addClientOrganisation", method = RequestMethod.POST)
	@ResponseBody //RESPONSE ENTITY TO RETURN A USER BUT NO JACKSON
	public ResponseEntity<String> addNewClientOrganisation(@RequestBody String clientOrgJSON) {

		//Assume client org has ClientOrg name, One admin user which is to be created.
		try{
			
			Object obj = parser.parse(clientOrgJSON);
			JSONObject jsonObject = (JSONObject) obj;
			DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
			String name = (String)jsonObject.get("name");
			String email = (String)jsonObject.get("email");
			String nameAdmin = (String)jsonObject.get("nameAdmin");
			Double fee = Double.valueOf((String)jsonObject.get("fee"));
			JSONArray rolesArr = (JSONArray)jsonObject.get("subscription");
			Date start = sdf.parse((String)jsonObject.get("start"));
			Date end = sdf.parse((String)jsonObject.get("end"));
			String address = (String)jsonObject.get("address");
			String postal = (String)jsonObject.get("postal");
			String phone = (String)jsonObject.get("phone");
			Set<Subscription> subsToAdd = new HashSet<Subscription>();
			for(int i = 0; i < rolesArr.size(); i++){
				subsToAdd.add(Subscription.valueOf((String)rolesArr.get(i)));
			}

			System.out.println("adding new client organisation" + name + " with it admin user: " + email);
			//if ( !clientOrganisationService.createNewClientOrganisation(name, email,subsToAdd,nameAdmin) ) return new ResponseEntity<String>( HttpStatus.CONFLICT);
			clientOrganisationService.createNewClientOrganisation(name, email,subsToAdd,nameAdmin, fee, start, end, address, postal, phone); 
			System.out.println( "CREATED CLIENT ORGANISATION : " + userService.getUserByEmail(email).get().getClientOrganisation().getOrganisationName() );
			//TEST FOR CREATING NEW USER
			/*Set<Role> userRolesToAddIn2 = new HashSet<Role>();
 			userRolesToAddIn2.add(roleRepository.getRoleByName("ROLE_USER"));
 			userService.createNewUser(userService.getUserByEmail(email).get().getClientOrganisation(), "chuajinfa@gmail.com", userRolesToAddIn2);
 			System.out.println("CREATED USER FOR CHUAJINFA with client org" + userService.getUserByEmail(email).get().getClientOrganisation().getOrganisationName());*/

		}
		catch ( InvalidEmailException e ){
			Gson g = new Gson();
			String json = g.toJson(e.getMessage());
			System.out.println("EEPTOIN" + json);
			return new ResponseEntity<String>(json,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( EmailAlreadyExistsException e ){
			Gson g = new Gson();
			String json = g.toJson(e.getMessage());
			System.out.println("EEPTOIN" + json);
			return new ResponseEntity<String>(json,HttpStatus.INTERNAL_SERVER_ERROR);

		}
		catch ( OrganisationNameAlreadyExistsException e){
			Gson g = new Gson();
			String json = g.toJson(e.getMessage());
			System.out.println("EEPTOIN" + json);
			return new ResponseEntity<String>(json,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( MailException e ){
			Gson g = new Gson();
			return new ResponseEntity<String>(g.toJson("Server error in sending email to the new user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			Gson g = new Gson();
			String json = g.toJson(e.getMessage());
			System.out.println("EEPTOIN" + e.toString() + "   " + json);
			return new ResponseEntity<String>(json,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/viewClientOrgs", method = RequestMethod.GET)
	@ResponseBody //RESPONSE ENTITY TO RETURN A USER BUT NO JACKSON
	public String viewClientOrgs(HttpServletRequest rq) {

		//Assume client org has ClientOrg name, One admin user which is to be created.
		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Collection<ClientOrganisation> orgList = clientOrganisationService.getAllClientOrganisations();

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {

						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == PaymentPolicy.class  ||clazz == SpecialRate.class  ||clazz == Building.class  
									||clazz == UnitAttributeType.class  ||clazz == Icon.class  ||clazz == User.class  
									|| clazz == Vendor.class);
						}

						/**
						 * Custom field exclusion goes here
						 */

						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							// TODO Auto-generated method stub
							return false;
						}

					})

					.serializeNulls()
					.create();
    
			String json = gson2.toJson(orgList);
			System.out.println(json);
			return json;
		}
		catch ( Exception e) {
			System.err.println("Error at UserController get all clientOrgs" + e.toString());
			return "ERROR";
		}
	}

	//This method returns details of all client organizations except user and vendor information
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/viewAllClientOrganisations", method = RequestMethod.GET)
	@ResponseBody
	public String viewAllClientOrganisations(HttpServletRequest rq) throws UserNotFoundException {
		Gson gson2 = new Gson();
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName()); //Extra checkings for role admin could be done
		if ( !usr.isPresent() ){
			return gson2.toJson("User does not exist"); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}

		//Assume client org has ClientOrg name, One admin user which is to be created.
		try{
			Collection<ClientOrganisation> orgsToReturn = clientOrganisationService.getAllClientOrganisations();

			Gson gson = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						//No Vendors or users inside the organisation should be returned
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == PaymentPolicy.class  ||clazz == SpecialRate.class  ||clazz == Building.class  
									||clazz == UnitAttributeType.class  ||clazz == Icon.class  ||clazz == User.class  
									|| clazz == Vendor.class);
						}

						/**
						 * Custom field exclusion goes here
						 */

						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							// TODO Auto-generated method stub
							return false;
						}

					})
					.serializeNulls()
					.create();

			String json = gson.toJson(orgsToReturn);
			System.out.println("Returning organisations: " + json);
			return json;   

		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return gson2.toJson("Error viewing client organisations");
		}
	}

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/deleteClientOrg", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteClientOrg(@RequestBody String userJSON, HttpServletRequest rq) {

		try{
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			Long id = (Long)jsonObject.get("id");

			System.out.println("CLIENT ORG ID TO BE DELETED: " + id);
			//userRepository.delete(userToDelete);
			clientOrganisationService.deleteClientOrg(id);
			System.err.println("deledted");

		}
		catch ( ClientOrganisationNotFoundException e){
			Gson g = new Gson();
			String json = g.toJson(e.getMessage());
			System.out.println("EEPTOIN" + json);
			return new ResponseEntity<String>(json,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e){
			System.out.println("ERROR IN DELETING Client Org" + e.getMessage());
			Gson g = new Gson();
			String json = g.toJson(e.getMessage());
			return new ResponseEntity<String>(json,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/updateClientOrg", method = RequestMethod.POST)
	@ResponseBody //SERVICE CLASS
	public ResponseEntity<Void> updateClientOrgs(@RequestBody String userJSON, HttpServletRequest rq) {
		System.out.println("ERROR here GGGGG");
		try{
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;

			//Name to find client org
			System.out.println("ERROR here 1");
			String name = (String)jsonObject.get("name");
			//System.out.println("ERROR here 2");
			//String newname = (String)jsonObject.get("newname");



			//new subsys
			JSONArray subsysArr = (JSONArray)jsonObject.get("subsys");
			//System.err.println("Name is " + name + "NEW NAME IS " + newname + "subsys is: " + subsysArr.toString());

			//List<Subscription> sysToAdd = new ArrayList<Subscription>();
			Set<Subscription> sysToAdd = new HashSet<Subscription>();
			for(int i = 0; i < subsysArr.size(); i++){
				sysToAdd.add(Subscription.valueOf((String)subsysArr.get(i)));
			}

			System.out.println("EDITING REACHED HERE LINE 209 GETTING CLIENT ORG OF NAME " + name);
			ClientOrganisation orgToEdit = clientOrganisationRepository.getClientOrgByName(name);
			/*System.err.println("ERROR here" + orgToEdit.getOrganisationName()+" " + name + " " + newname);
			if(newname.equals("")){
				orgToEdit.setOrganisationName(name);
			}
			else {			
				orgToEdit.setOrganisationName(newname);
			}*/
			orgToEdit.setSystemSubscriptions(sysToAdd);
			clientOrganisationRepository.saveAndFlush(orgToEdit);
			System.out.println("GG");
			//System.out.println("ORGANIZATION TO BE EDITED: " + OrgToEdit + "   " +name);
			//userToEdit.setRoles(roles);
		}catch(Exception e){
			System.out.println("ERROR IN EDITING USER");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/updateOrgDetails", method = RequestMethod.POST)
	@ResponseBody //SERVICE CLASS
	public ResponseEntity<Void> updateOrgDetails(@RequestBody String userJSON, HttpServletRequest rq) {
		System.out.println("ERROR here GGGGG");
		try{
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
			//Name to find client org
			System.err.println("ERROR here 1");
			Long id = (Long)jsonObject.get("id");
			String address = (String)jsonObject.get("address");
			String name = (String)jsonObject.get("name");
			System.err.println("ERROR here 2");
			String postal = (String)jsonObject.get("postal");
			System.err.println("postal" +postal);
			String phone = (String)jsonObject.get("phone");
			System.err.println("phone" +phone);
			Date start_date = sdf.parse((String)jsonObject.get("start_date"));
			System.err.println("start" +start_date);
			Date end_date = sdf.parse((String)jsonObject.get("end_date"));
			System.err.println("end" +end_date);
			Double fee = Double.valueOf((String)jsonObject.get("fee"));

			System.out.println("EDITING REACHED HERE LINE 209 GETTING CLIENT ORG OF ID " + id);
			ClientOrganisation orgToEdit = clientOrganisationRepository.findOne(id);
			System.out.println("CLIENT ORG NAME IS " + orgToEdit.getOrganisationName());				
			orgToEdit.setOrganisationName(name);
			orgToEdit.setAddress(address);
			orgToEdit.setPostal(postal);
			orgToEdit.setPhone(phone);
			orgToEdit.setFee(fee);
			orgToEdit.setStart_date(start_date);
			orgToEdit.setEnd_date(end_date);
			clientOrganisationRepository.saveAndFlush(orgToEdit);
			System.out.println("CHANGES SAVED");
		}catch(Exception e){
			System.out.println("ERROR IN EDITING CLIENT ORG");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}






	// ---------------------------------- END SUPER ADMIN METHODS ---------------------------------------
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERADMIN')")
	//Takes in userJSON with information in the object {email:"email", roles:[arrayofroles]}
	@RequestMapping(value = "user/addNewUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addNewUser(@RequestBody String userJSON, HttpServletRequest rq) {
		Gson gson = new Gson();
		//Assume client org has ClientOrg name, One admin user which is to be created.
		try{



			/*		JSONParser parser = new JSONParser();

   			//get email
   			Object object = parser.parse(userJSON);
   			JSONObject jsonObject = (JSONObject) object;
   			String email = (String)jsonObject.get("email");


   			//get roles
   			JSONArray role = new JSONArray("userJSON");
   			List<String> list = new ArrayList<String>();
   			for(int i = 0; i < role.length(); i++){
   			    list.add(role.getJSONObject(i).getString("name"));
   			}*/

			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			String email = (String)jsonObject.get("email");
			String name = (String)jsonObject.get("name");
			JSONArray rolesArr = (JSONArray)jsonObject.get("roles");
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Set<Role> userRolesToAddIn = new HashSet<Role>();
			List<String> roles = new ArrayList<String>();
			for(int i = 0; i < rolesArr.size(); i++){
				roles.add((String)rolesArr.get(i));
			}
			for (String r : roles ){
				System.out.println(r);
				Role tempR = roleRepository.getRoleByName(r.toUpperCase());
				userRolesToAddIn.add(tempR);		
				System.err.println(r);
			}



			if ( !userService.createNewUser(currUser.getClientOrganisation(), name,email, userRolesToAddIn) ) return new ResponseEntity<String>(gson.toJson("Error creating new user"),HttpStatus.INTERNAL_SERVER_ERROR);
			//if ( !u.createNewClientOrganisation(name, email) ) return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			//System.out.println( "CREATED CLIENT ORGANISATION : " + userService.getUserByEmail(email).get().getClientOrganisation().getOrganisationName() );

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("User Management");
			al.setAction("Added new user: " + email);
			al.setUser(currUser);
			al.setUserEmail(currUser.getEmail());
			auditLogRepository.save(al);

		}
		catch ( InvalidEmailException e ){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( EmailAlreadyExistsException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( MailException e ){
			return new ResponseEntity<String>(gson.toJson("Server error in sending email to the new user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.out.println("ERROR " + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in adding new user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/viewAllUsers", method = RequestMethod.GET)
	@ResponseBody
	public String viewAllUsers(HttpServletRequest rq) {
		Object obj;
		System.out.println("Start of view");
		try {
			Principal principal = rq.getUserPrincipal();
			System.out.println("got user princ");

			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			System.out.println("got useremail");

			Collection<User> userlist = userService.viewAllUsers(currUser.getClientOrganisation());
			System.out.println("got userlist");

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {

						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Ticket.class) || (clazz == Message.class || clazz == ClientOrganisation.class || clazz == ToDoTask.class || clazz == Event.class);
						}

						/**
						 * Custom field exclusion goes here
						 */

						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							// TODO Auto-generated method stub
							return false;
						}

					})

					.serializeNulls()
					.create();

			String json = gson2.toJson(userlist);
			System.out.println(json);
			return json;
		} catch ( Exception e){
			System.out.println("Exception caught at UserController @ viewAllUsers" + e.toString());
		}
		Gson gson = new Gson();
		String json = gson.toJson("NOT OK");
		return gson.toJson(json);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/deleteUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteUser(@RequestBody String userJSON, HttpServletRequest rq) {
		Gson gson = new Gson();
		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			String email = (String)jsonObject.get("email");
			System.err.println("email: " + email);
			//	Principal principal = rq.getUserPrincipal();
			User userToDelete = (User)userService.getUserByEmail(email).get();
			System.out.println("USER TO BE DELETED: " + userToDelete.getEmail());
			//userRepository.delete(userToDelete);

			auditLogRepository.deleteAuditLogsOfUserId(userToDelete.getId());
			userService.deleteUser(userToDelete);
			System.err.println("Deleted");

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("User Management");
			al.setAction("Deleted user: " + userToDelete.getEmail());
			al.setUser(currUser);
			al.setUserEmail(currUser.getEmail());
			auditLogRepository.save(al);

		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e){
			System.out.println("ERROR IN DELETING USER");
			return new ResponseEntity<String>(gson.toJson("Server error in deleting user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_SUPERADMIN')")
	@RequestMapping(value = "user/updateUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateUser(@RequestBody String userJSON, HttpServletRequest rq) {
		Gson gson = new Gson();
		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();

			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			//email to find user
			String email = (String)jsonObject.get("email");
			//New name to be saved
			String name = (String)jsonObject.get("name");

			//find user
			User userToEdit = (User)userService.getUserByEmail(email).get();

			//new roles
			JSONArray rolesArr = (JSONArray)jsonObject.get("roles");
			System.err.println("Email is " + email + "Name is " + name + "Roles is: " + rolesArr.toString());

			//	Principal principal = rq.getUserPrincipal();
			//	User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Set<Role> userRolesToAddIn = new HashSet<Role>();
			List<String> roles = new ArrayList<String>();
			for(int i = 0; i < rolesArr.size(); i++){
				roles.add((String)rolesArr.get(i));
			}

			for (String r : roles ){
				Role tempR = roleRepository.getRoleByName(r);
				userRolesToAddIn.add(tempR);		
				System.err.println(r);
			}

			System.out.println("EDITING REACHED HERE");

			System.err.println("Setting name from" + userToEdit.getName() + "to" + name);
			//userToEdit.setName(name);
			//userRepository.saveAndFlush(userToEdit);
			//userToEdit.setRoles(userRolesToAddIn);
			//  userRepository.saveAndFlush(userToEdit);
			//System.err.println("set " + userToEdit.getName());
			//System.err.println(userToEdit.getName());

			if ( !userService.editUser(name, userToEdit, userRolesToAddIn) ){
				return new ResponseEntity<String>(gson.toJson("Server error in editing user"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//System.out.println("GG2");

			System.out.println("USER TO BE EDITED: " + userToEdit + "   " +email);
			//userToEdit.setRoles(roles);

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("User Management");
			al.setAction("Updated user: " + email);
			al.setUser(currUser);
			al.setUserEmail(currUser.getEmail());
			auditLogRepository.save(al);




		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e){
			System.out.println("ERROR IN EDITING USER");
			return new ResponseEntity<String>(gson.toJson("Server error in editing user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "user/editUserProfile", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> editUserProfile(@RequestBody String userJSON, HttpServletRequest rq) {
		Gson gson = new Gson();
		try{
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			String name = (String)jsonObject.get("name");

			System.out.println("NEW NAME TO CHANGE TO: " + name);
			//userRepository.delete(userToDelete);


			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();

			currUser.setName(name);
			userRepository.saveAndFlush(currUser);

		}catch(Exception e){
			System.out.println("ERROR IN EDITING USER PROFILE" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in editing user profile"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	@RequestMapping(value = "user/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> changePassword(@RequestBody String userJSON, HttpServletRequest rq) {
		Gson gson = new Gson();
		try{
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			String pass = (String)jsonObject.get("password");
			String oldpass = (String)jsonObject.get("oldpassword");
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			if (currUser.getSecurity() == null || currUser.getSecurityQuestion()  == null)
				return new ResponseEntity<String>(gson.toJson("Please set a security question and answer before resetting password"),HttpStatus.INTERNAL_SERVER_ERROR);
			userService.checkOldPassword(currUser.getId(),oldpass);
			if ( !userService.changePassword(currUser.getId(), pass) ){
				return new ResponseEntity<String>(gson.toJson("Server error in changing password"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			//String hashedPassword = encoder.encode(pass);
			//currUser.setPasswordHash(hashedPassword); //add salt?
			////userRepository.saveAndFlush(currUser);

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("User Management");
			al.setAction("Changed password");
			al.setUser(currUser);
			al.setUserEmail(currUser.getEmail());
			auditLogRepository.save(al);

		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( OldPasswordInvalidException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e){
			System.out.println("ERROR IN CHANGING PASSWORD" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in changing password"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}
	
	//Change security question
	@RequestMapping(value = "user/changeSecurity", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> changeSecurity(@RequestBody String userJSON, HttpServletRequest rq) {
		Gson gson = new Gson();
		try{
			Object obj = parser.parse(userJSON);
			JSONObject jsonObject = (JSONObject) obj;
			String security = (String)jsonObject.get("security");
			String oldSecurity = (String)jsonObject.get("oldsecurity");
			String question = (String)jsonObject.get("question");
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			userService.checkOldSecurity(currUser.getId(),oldSecurity);
			if ( !userService.changeSecurity(currUser.getId(), security, question) ){
				return new ResponseEntity<String>(gson.toJson("Server error in changing security answer"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			//BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			//String hashedPassword = encoder.encode(pass);
			//currUser.setPasswordHash(hashedPassword); //add salt?
			////userRepository.saveAndFlush(currUser);

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("User Management");
			al.setAction("Changed password");
			al.setUser(currUser);
			al.setUserEmail(currUser.getEmail());
			auditLogRepository.save(al);

		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( OldSecurityInvalidException e){
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch(Exception e){
			System.out.println("ERROR IN CHANGING SECURITY ANSWER" + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in changing security answer"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);

	}
	///////GETTING USER PROFILE START
	@RequestMapping(value = "user/getProfileRoles", method = RequestMethod.GET)
	@ResponseBody
	public String getProfile( HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Set<Role> toReturn = currUser.getRoles();
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {

						public boolean shouldSkipClass(Class<?> clazz) {
							return ( clazz == User.class );
						}

						/**
						 * Custom field exclusion goes here
						 */

						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							// TODO Auto-generated method stub
							return false;
						}

					})

					.serializeNulls()
					.create();

			String json = gson2.toJson(toReturn);
			System.out.println(json);
			return json;	

		}catch(Exception e){
			System.out.println("ERROR IN GETTING PROFILE" + e.getMessage());
			Gson gson = new Gson();
			String json = gson.toJson("ERROR");
			return gson.toJson(json);
		}

	}

	@RequestMapping(value = "user/getProfileDetails", method = RequestMethod.GET,produces="text/plain")
	@ResponseBody
	public String getProfileDetails( HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {

						public boolean shouldSkipClass(Class<?> clazz) {
							return ( clazz == Message.class || clazz == Role.class || clazz == ClientOrganisation.class);
						}

						/**
						 * Custom field exclusion goes here
						 */

						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							// TODO Auto-generated method stub
							return false;
						}

					})

					.serializeNulls()
					.create();

			String json = gson2.toJson(currUser);
			System.out.println(json);
			return json;		

		}catch(Exception e){
			System.out.println("ERROR IN GETTING PROFILE" + e.getMessage());
			return "ERROR";
		}

	}
	//GETTING USER PROFILE END

	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "user/viewCurrentUser",  method = RequestMethod.GET)
	@ResponseBody
	public String viewCurrentUser(HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		try{
			Optional<User> usrOpt = userService.getUserByEmail(principal.getName());
			if (usrOpt.isPresent()){

				User usr=usrOpt.get();
				ClientOrganisation clientObj = usr.getClientOrganisation();
				String client=clientObj.getOrganisationName();
				String theme=clientObj.getThemeColour();
				System.out.println("start view");
				Set<Role> roles=usr.getRoles();
				JSONArray rolsArray = new JSONArray();

				String userName=principal.getName();
				String name=usr.getName();
				String roleString="";
				for(Role role:roles){
					roleString+=role.getName()+" ";
					JSONObject roleJson = new JSONObject();
					String roleString1=role.getName();
					if(!roleString1.equals("ROLE_USER")){
						roleString1=(roleString1.substring(5,roleString1.length())).toLowerCase();
						if(roleString1.equals("superadmin")){
							roleString1="Algattas admin";
						}else if(roleString1.equals("admin")){
							roleString1="IT admin";
						}else if(roleString1.equals("event")){
							roleString1="managing events";
						}else if(roleString1.equals("property")){
							roleString1="managing property";
						}else if(roleString1.equals("finance")){
							roleString1="managing finance";
						}else if(roleString1.equals("ticketing")){
							roleString1="managing tickets";
						}else if(roleString1.equals("exteve")){
							roleString1="organising events";
						}
						roleJson.put("role", roleString1);
						rolsArray.add(roleJson);
					}
				}
				JSONObject bd = new JSONObject(); 
				bd.put("client", client); 
				bd.put("theme", theme); 
				bd.put("role", roleString); 
				bd.put("name", name); 
				bd.put("userName", userName);
				bd.put("roles", rolsArray);
				//bd.put("roles", roles); 
				System.out.println("Returning user info : " + bd.toString());
				return bd.toString();
			}else{
				JSONObject err = new JSONObject(); 
				err.put("error", "error"); 
				System.out.println("Returning user info error : " + err.toString());
				return err.toString();

			}

		}catch (Exception e){
			JSONObject err = new JSONObject(); 
			err.put("error", "error"); 
			System.out.println("Returning building id : " + err.toString());
			return err.toString();

		}


	}
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
		// Each object (building) will contain... long id, .
		@RequestMapping(value = "user/viewClientOfUser",  method = RequestMethod.GET)
		@ResponseBody
		public String viewClientOfUser(HttpServletRequest rq) {
			Principal principal = rq.getUserPrincipal();
			try{
				Optional<User> usrOpt = userService.getUserByEmail(principal.getName());
				if (usrOpt.isPresent()){

					User usr=usrOpt.get();
					ClientOrganisation clientObj = usr.getClientOrganisation();
					String client=clientObj.getOrganisationName();
					
					JSONObject bd = new JSONObject(); 
					bd.put("client", client); 
					return bd.toString();
				}else{
					JSONObject err = new JSONObject(); 
					err.put("error", "error"); 
					System.out.println("Returning user info error : " + err.toString());
					return err.toString();

				}

			}catch (Exception e){
				JSONObject err = new JSONObject(); 
				err.put("error", "error"); 
				System.out.println("Returning building id : " + err.toString());
				return err.toString();

			}


		}

	/*
	 * Mobile app register new user;
	 * 
	 */
	@RequestMapping(value = "/registerNewUser",  method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> registerNewUser(@RequestBody String userJSON) {
		Gson gson = new Gson();
		System.out.println("enter r new user" + userJSON);
		try{
			Object obj = parser.parse(userJSON);
			
			JSONObject jsonObject = (JSONObject) obj;
			String email = (String)jsonObject.get("username");
			String name = (String)jsonObject.get("name");
			String password = (String)jsonObject.get("password");
			System.out.println("user of email and name is signging up for new account :" +  email + "  "  +name + " " + password);
			if ( !ticketingService.registerNewUser(name, email, password) ) return new ResponseEntity<String>(gson.toJson("Error creating new user"),HttpStatus.INTERNAL_SERVER_ERROR);


		}
		catch ( InvalidEmailException e ){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( EmailAlreadyExistsException e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(gson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( MailException e ){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in sending email to the new user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.out.println("ERROR " + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(gson.toJson("Server error in adding new user"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}


}




