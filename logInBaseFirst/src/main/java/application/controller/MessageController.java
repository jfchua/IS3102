package application.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import application.entity.ClientOrganisation;
import application.entity.GraphAdapterBuilder;
import application.entity.Message;
import application.entity.PasswordResetToken;
import application.entity.Role;
import application.entity.SendMessageForm;
import application.entity.SendMessageFormValidator;
import application.entity.User;
import application.exception.MessageNotFoundException;
import application.exception.ToDoTaskNotFoundException;
import application.exception.UserNotFoundException;
import application.repository.MessageRepository;
import application.service.MessageService;
import application.service.UserService;

@Controller
public class MessageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	private final MessageService messageService;
	private final UserService userService;
	private final SendMessageFormValidator sendMessageFormValidator;
	private MessageRepository messageRepository;
	private  JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();

	@Autowired
	public MessageController(MessageRepository messageRepository,MessageService messageService, UserService userService, SendMessageFormValidator sendMessageFormValidator) {
		super();
		this.messageService = messageService;
		this.messageRepository =  messageRepository;
		this.userService = userService;
		this.sendMessageFormValidator = sendMessageFormValidator;
	}

	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(sendMessageFormValidator);
	}

	/*@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	@RequestMapping(value = "/user/send", method = RequestMethod.GET)
	public ModelAndView sendMessage() {
		LOGGER.debug("Getting sendMessage page");
		//return "sendMessage";
		return new ModelAndView("sendMessage", "users", userService.getAllUsers());
	}*/

	/*@RequestMapping("/user/messages")
	public ModelAndView getViewMessagesPage() {
		LOGGER.debug("Getting view Messages");
		return new ModelAndView("viewMessage", "messages", messageService.getMessages());
	}*/

	/*@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	@RequestMapping(value = "/user/send", method = RequestMethod.POST)
	public String handleSendMessageForm(@Valid @ModelAttribute("form") SendMessageForm form, BindingResult bindingResult) {
		messageService.sendMessage(form);
		return "redirect:/users";


		if (bindingResult.hasErrors()) {
			// failed validation
			return "home";
		}
		try {

			messageService.sendMessage(form);
		} catch (DataIntegrityViolationException e) {
			// probably email already exists - very rare case when multiple admins are adding same user
			// at the same time and form validation has passed for more than one of them.
			LOGGER.warn("Exception occurred when trying to save the user, assuming duplicate email", e);
			return "home";
		}
		// ok, redirect
		return "home";
	}*/

	/*@RequestMapping(value = "/user/notifications/findAllNotifications", method = RequestMethod.GET)
	@ResponseBody
	public String getAllMessages(HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR"; //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
		System.out.println("Getting messages sent to " + usr.get().getEmail());
		Collection<Message> temp = usr.get().getMessagesReceived();
		System.out.println("Number of messages rceived is : " + temp.size());
		Gson gson = new Gson();
	    String json = gson.toJson(temp);
	    System.out.println(json);
	    return json;	
		}
		catch ( Exception e){
			System.out.println("Exception caught at MessagControllerFindAllnotifications" + e.toString());
		}
		return "NOT OK";
	}*/

	@RequestMapping(value = "/user/notifications/findAllNotifications", method = RequestMethod.GET)
	@ResponseBody
	public String getAllMessages(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		System.out.println("THIS PLACE");
		System.out.println(principal.toString());
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return geeson.toJson("User was not found"); //NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			System.out.println("Getting messages sent to " + usr.get().getEmail());
			Collection<Message> temp = messageService.getMessages(usr.get());
			System.out.println("FINALLY NUM MESSAGES RETURNED IS " + temp.size());
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {

						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == User.class);
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

			String json = gson2.toJson(temp);
			System.out.println(json);
			return json;


		}
		catch ( Exception e){
			System.out.println("Exception caught at MessagControllerFindAllnotifications" + e.toString());
			return geeson.toJson("Server error in getting all notifications");
		}
	}


	@RequestMapping(value = "/user/notifications/sendNotification", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	//@ResponseBody
	public ResponseEntity<String> sendNotification(@RequestBody String info, HttpServletRequest rq) throws URISyntaxException, IOException, ParseException {
		//	JSONParser parser = new JSONParser();
		try{
			System.out.println("Sending notification...");
			Object obj = parser.parse(info);
			JSONObject jsonObject = (JSONObject) obj;
			String senderName = rq.getUserPrincipal().getName();;
			Optional<User> usr = userService.getUserByEmail(senderName);
			JSONArray recipients = (JSONArray)jsonObject.get("recipient");
			//List<String> list = new ArrayList<String>();
			for(int i = 0; i < recipients.size(); i++){
				Optional<User> usrRecipient = userService.getUserByEmail((String)recipients.get(i));	
				System.out.println("***Sending message from" + usr.get().getEmail());
				//usr.get().sendMessage(usrRecipient.get(), (String)jsonObject.get("subject"), (String)jsonObject.get("message"), messageService);
				//System.out.println(sender + " is sending message with subject of " + (String)jsonObject.get("subject"));
				messageService.sendMessage(usr.get(), usrRecipient.get(),(String)jsonObject.get("subject"), (String)jsonObject.get("message"));
				System.out.println("***Message Sucessfully sent to " + usrRecipient.get().getEmail());
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.err.println("Exception at send Notification" + e.toString());
			return new ResponseEntity<String>(geeson.toJson("Server error in sending notification"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	//This method takes in a String which is the ID of the toDoTask to delete
	// Call $http.post(URL,(String)id);
/*	@RequestMapping(value = "/deleteToDoTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteToDoTask(@RequestBody String taskId,HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			currUser.deleteToDoTask(Long.parseLong(taskId));
		}
		catch ( ToDoTaskNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in deleting to do task"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}*/

	//This method takes in a String which is the ID of the notification to delete
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteNotification", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteNotification(@RequestBody String notificationId,HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Message m = messageRepository.findOne(Long.parseLong(notificationId));
			System.out.println("MESSAGE TO BE DELETED FOUND!!!");
			if ( !m.getRecipient().equals(currUser)){ //THE MESSAGE TO BE DELETED IS NOT RECEIVED BY THE CURRENT USER
				System.err.println("Other things dont have?");
				return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			messageService.deleteMessage(m);

		}
		catch ( MessageNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.out.println(e.toString());
			System.out.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in deleting message"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/getUsersToSendTo", method = RequestMethod.GET)
	@ResponseBody
	public String getUsersToSendNotificationsTo(HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			ClientOrganisation c = currUser.getClientOrganisation();
			Set<User> setOfUsers = c.getUsers();
			ArrayList<String> emailsToReturn = new ArrayList<String>();
			for ( User us : setOfUsers){
				if ( !us.getEmail().equals(currUser.getEmail())){
					boolean isEgoer=false;
				
					Set<Role> rolesOfUser=us.getRoles();
						for (Role role:rolesOfUser){
							if(role.getName().equals("ROLE_EVEGOER")){
								isEgoer=true;
							}
						}
					if(!isEgoer)
					emailsToReturn.add(us.getEmail());
			}
			}
			Collections.sort(emailsToReturn);			
			Gson gson = new Gson();
			String json = gson.toJson(emailsToReturn);
			System.out.println(json);
			return json; 

		}
		catch (Exception e){
			return geeson.toJson("Server error in getting users to send to");
		}
	}

}
