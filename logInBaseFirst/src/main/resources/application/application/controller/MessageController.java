package application.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import application.domain.ClientOrganisation;
import application.domain.GraphAdapterBuilder;
import application.domain.Message;
import application.domain.PasswordResetToken;
import application.domain.SendMessageForm;
import application.domain.User;
import application.domain.validator.SendMessageFormValidator;
import application.repository.MessageRepository;
import application.service.user.MessageService;
import application.service.user.UserService;

@Controller
public class MessageController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);
	private final MessageService messageService;
	private final UserService userService;
	private final SendMessageFormValidator sendMessageFormValidator;
	private MessageRepository messageRepository;
	private  JSONParser parser = new JSONParser();

	@Autowired
	public MessageController(MessageRepository messagRepeository,MessageService messageService, UserService userService, SendMessageFormValidator sendMessageFormValidator) {
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

	@PreAuthorize("hasAnyAuthority('USER','ADMIN')")
	@RequestMapping(value = "/user/send", method = RequestMethod.GET)
	public ModelAndView sendMessage() {
		LOGGER.debug("Getting sendMessage page");
		//return "sendMessage";
		return new ModelAndView("sendMessage", "users", userService.getAllUsers());
	}

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
	public String getAllMessages(HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		System.out.println("THIS PLACE");
		System.out.println(principal.toString());
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR"; //NEED ERROR HANDLING BY RETURNING HTTP ERROR
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
					/**
					 * Use serializeNulls method if you want To serialize null values 
					 * By default, Gson does not serialize null values
					 */
					.serializeNulls()
					.create();

			//	Gson gson = new Gson();
			//    GsonBuilder gsonBuilder = new GsonBuilder();
			//    new GraphAdapterBuilder()
			//        .addType(Message.class)
			//        .registerOn(gsonBuilder);
			//    Gson gson3 = gsonBuilder.create();
			//    System.out.println("NEWGRAPSH ADAPTER: " + gson3.toJson(temp));

			String json = gson2.toJson(temp);
			System.out.println(json);
			return json;


		}
		catch ( Exception e){
			System.out.println("Exception caught at MessagControllerFindAllnotifications" + e.toString());
		}
		return "NOT OK";
	}


	@RequestMapping(value = "/user/notifications/sendNotification", method = RequestMethod.POST, headers = {"Content-type=application/json"})
	//@ResponseBody
	public ResponseEntity<Void> sendNotification(@RequestBody String info, HttpServletRequest rq) throws URISyntaxException, IOException, ParseException {
		//	JSONParser parser = new JSONParser();
		try{
			System.out.println("Sending notification...");
			Object obj = parser.parse(info);
			JSONObject jsonObject = (JSONObject) obj;
			String senderName = rq.getUserPrincipal().getName();
			System.out.println("Sender name is " + senderName);
			Optional<User> usr = userService.getUserByEmail(senderName);
			Optional<User> usrRecipient = userService.getUserByEmail((String)jsonObject.get("recipient"));	
			System.out.println("Sending message from" + usr.get().getEmail());
			//usr.get().sendMessage(usrRecipient.get(), (String)jsonObject.get("subject"), (String)jsonObject.get("message"), messageService);
			//System.out.println(sender + " is sending message with subject of " + (String)jsonObject.get("subject"));
			messageService.sendMessage(usr.get(), usrRecipient.get(),(String)jsonObject.get("subject"), (String)jsonObject.get("message"));
			System.out.println("Message Sucessfully sent to " + usrRecipient.get().getEmail());
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		catch (Exception e){
			System.err.println("Exception at send Notification" + e.toString());
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}

	}

	//This method takes in a String which is the ID of the toDoTask to delete
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteToDoTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> deleteToDoTask(@RequestBody String taskId,HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			currUser.deleteToDoTask(Long.parseLong(taskId));
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	//This method takes in a String which is the ID of the notification to delete
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteNotification", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<Void> deleteNotification(@RequestBody String notificationId,HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Message m = messageRepository.findOne(Long.parseLong(notificationId));
			if ( !m.getRecipient().equals(currUser)){ //THE MESSAGE TO BE DELETED IS NOT RECEIVED BY THE CURRENT USER
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
			messageService.deleteMessage(m);

		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@RequestMapping(value = "/getUsersToSendTo", method = RequestMethod.GET)
	@ResponseBody
	public String getUsersToSendNotificationsTo(HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			ClientOrganisation c = currUser.getClientOrganisation();
			Set<User> setOfUsers = c.getUsers();
			Set<String> emailsToReturn = new HashSet<String>();
			for ( User us : setOfUsers){
				if ( !us.getEmail().equals(currUser.getEmail()))
					emailsToReturn.add(us.getEmail());
			}
			Gson gson = new Gson();
			String json = gson.toJson(emailsToReturn);
			System.out.println(json);
			return json; 

		}
		catch (Exception e){
			return "ERROR";
		}
	}

}
