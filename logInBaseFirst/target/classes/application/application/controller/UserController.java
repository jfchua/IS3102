package application.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

import application.domain.ClientOrganisation;
import application.domain.Role;
import application.domain.User;
import application.domain.Vendor;
import application.domain.validator.UserCreateFormValidator;
import application.repository.RoleRepository;
import application.service.user.ClientOrganisationService;
import application.service.user.UserService;

@Controller
public class UserController {

   // private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    //private final UserService userService;
    private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final RoleRepository roleRepository;
    
    private JSONParser parser = new JSONParser();
    //private final UserCreateFormValidator userCreateFormValidator;

    @Autowired
    public UserController(ClientOrganisationService clientOrganisationService,UserService userService, RoleRepository roleRepository) {
        //this.userService = userService;
        this.clientOrganisationService = clientOrganisationService;
        this.userService = userService;
        this.roleRepository =  roleRepository;
      //  this.userCreateFormValidator = userCreateFormValidator;
    }
    
    
    // ----------------------- START SUPER ADMIN METHODS WITH ROLE CHECKING ------------------------
    // TODO: CHECK ROLE FOR SUPERADMIN
    // TODO: ADD CLIENT ORGANISATION SUBSCRIPTIONS.
    // This method adds a client organisation.
    @RequestMapping(value = "user/addClientOrganisation", method = RequestMethod.POST)
	@ResponseBody //RESPONSE ENTITY TO RETURN A USER BUT NO JACKSON
	public ResponseEntity<Void> addNewClientOrganisation(@RequestBody String clientOrgJSON) {

    	//Assume client org has ClientOrg name, One admin user which is to be created.
		try{
			Object obj = parser.parse(clientOrgJSON);
			JSONObject jsonObject = (JSONObject) obj;

			String name = (String)jsonObject.get("name");
			String email = (String)jsonObject.get("email");
			
			System.out.println("adding new client organisation" + name + " with it admin user: " + email);
			if ( !clientOrganisationService.createNewClientOrganisation(name, email) ) return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			System.out.println( "CREATED CLIENT ORGANISATION : " + userService.getUserByEmail(email).get().getClientOrganisation().getOrganisationName() );
			
			Set<Role> userRolesToAddIn2 = new HashSet<Role>();
			userRolesToAddIn2.add(roleRepository.getRoleByName("USER"));
			userService.createNewUser(userService.getUserByEmail(email).get().getClientOrganisation(), "chuajinfa@gmail.com", userRolesToAddIn2);
			System.out.println("CREATED USER FOR CHUAJINFA with client org" + userService.getUserByEmail(email).get().getClientOrganisation().getOrganisationName());
			
			
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
    
    //This method returns details of all client organizations except user and vendor information
    @RequestMapping(value = "user/viewAllClientOrganisations", method = RequestMethod.GET)
	@ResponseBody
	public String viewAllClientOrganisations(HttpServletRequest rq) {
    	
    	Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName()); //Extra checkings for role admin could be done
		if ( !usr.isPresent() ){
			return "ERROR"; //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}

    	//Assume client org has ClientOrg name, One admin user which is to be created.
		try{
			Collection<ClientOrganisation> orgsToReturn = clientOrganisationService.getAllClientOrganisations();
			
			Gson gson = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						//No Vendors or users inside the organisation should be returned
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class  || clazz == Vendor.class);
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
			return "ERROR";
		}
	}
    
    
    
 // ---------------------------------- END SUPER ADMIN METHODS ---------------------------------------
    
    //Takes in userJSON with information in the object {email:"email", roles:[arrayofroles]}
    @RequestMapping(value = "user/addNewUser", method = RequestMethod.POST)
   	@ResponseBody
   	public ResponseEntity<Void> addNewUser(@RequestBody String userJSON, HttpServletRequest rq) {

       	//Assume client org has ClientOrg name, One admin user which is to be created.
   		try{
   			Object obj = parser.parse(userJSON);
   			JSONObject jsonObject = (JSONObject) obj;
   			String email = (String)jsonObject.get("email");
   			String[] roles = (String[])jsonObject.get("roles");
   			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
		    Set<Role> userRolesToAddIn = new HashSet<Role>();
			for (String r : roles ){
				Role tempR = roleRepository.getRoleByName(r);
				userRolesToAddIn.add(tempR);				
			}

			if ( !userService.createNewUser(currUser.getClientOrganisation(), email, userRolesToAddIn) ) return new ResponseEntity<Void>(HttpStatus.CONFLICT);
   			//if ( !u.createNewClientOrganisation(name, email) ) return new ResponseEntity<Void>(HttpStatus.CONFLICT);
   			//System.out.println( "CREATED CLIENT ORGANISATION : " + userService.getUserByEmail(email).get().getClientOrganisation().getOrganisationName() );
   			
   			
   		}
   		catch (Exception e){
   			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
   			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
   		}
   		return new ResponseEntity<Void>(HttpStatus.OK);
   	}
	
    
    
    
    
    
//Standard
 /*   @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
        LOGGER.debug("Getting user page for user={}", id);
        return new ModelAndView("user", "user", userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException(String.format("User=%s not found", id))));
    }
    //Additional security measure
    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        LOGGER.debug("Getting user create form");
        return new ModelAndView("user_create", "form", new UserCreateForm());
    }
    */
    
    
    	
    	//Standard
    /*@PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
        LOGGER.debug("Processing user create form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "user_create";
        }
        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            // probably email already exists - very rare case when multiple admins are adding same user
            // at the same time and form validation has passed for more than one of them.
            LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        // ok, redirect
        return "redirect:/users";
    }	*/

}
