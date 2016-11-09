package application.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import application.entity.ToDoTask;
import application.entity.User;
import application.exception.ToDoTaskNotFoundException;
import application.exception.UserNotFoundException;
import application.repository.ToDoTaskRepository;
import application.service.ToDoTaskService;
import application.service.UserService;


@Controller
@RequestMapping("/todo")
public class TodoController {

	@Autowired
	private final UserService userService;
	private final ToDoTaskService toDoTaskService;
	private Gson geeson = new Gson();
	private final ToDoTaskRepository toDoTaskRepository;
	
	private JSONParser parser = new JSONParser();


	@Autowired
	public TodoController(UserService userService,ToDoTaskService toDoTaskService,ToDoTaskRepository toDoTaskRepository) {
		super();
		this.userService = userService;
		this.toDoTaskService =  toDoTaskService;
		this.toDoTaskRepository = toDoTaskRepository;

	}

// Call this method using $http.get and you will get a JSON format containing an array of toDoTask objects.
// Each object (toDoTask) will contain... long id, String task.
	@RequestMapping(value = "/getToDoList", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getToDoList(HttpServletRequest rq) throws UserNotFoundException {
		try{
		Principal principal = rq.getUserPrincipal();
		User currUser = (User)userService.getUserByEmail(principal.getName()).get();
		Collection<ToDoTask> t = toDoTaskService.getToDoList(currUser);
		Gson gson = new Gson();
		String json = gson.toJson(t);
		System.out.println("Returning getTodoList with json of : " + json);
		return new ResponseEntity<String>(json,HttpStatus.OK);	
		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in getting to do list"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//Security filters for inputs needs to be added
	//This method takes in a String which is the new toDoTask to be added.
	//Call $http.post(URL,stringToAdd);
	@RequestMapping(value = "/addToDoTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addToDoTask(@RequestBody String toDoTaskJSON,HttpServletRequest rq) {
		System.out.println("TYPE STH HERE FOR TODOTASK");
		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			Object obj = parser.parse(toDoTaskJSON);
			JSONObject jsonObject = (JSONObject) obj;

			String task = (String)jsonObject.get("task");
			String date = (String)jsonObject.get("date");
			
			Date dateParsed = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			
			toDoTaskService.addToDoTask(currUser, task, dateParsed);
			System.out.println("adding todotask to usrname: " + currUser.getEmail());
		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in adding to do task"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	//This method takes in a String which is the ID of the toDoTask to delete
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteToDoTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteToDoTask(@RequestBody String taskId,HttpServletRequest rq) {

		try{
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			toDoTaskService.deleteToDoTask(currUser, Long.parseLong(taskId));
		}
		catch ( UserNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( ToDoTaskNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in deleting to do task"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	//This method takes in a JSON format which contains an object with 2 attributes
	//Long/String id, String toDoTask;
	//Call $httpPost(Url,JSONData);
	/*@RequestMapping(value = "/updateToDoTask", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateToDoTask(@RequestBody String taskId,HttpServletRequest rq) {

		try{
			Object obj = parser.parse(taskId);
			JSONObject jsonObject = (JSONObject) obj;
			long id = (Long)jsonObject.get("id");
			String toDoTask = (String)jsonObject.get("toDoTask");
			Principal principal = rq.getUserPrincipal();
			User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			toDoTaskService.updateToDoTask(currUser, id, toDoTask);
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}*/


	
//what is this?!? 
	@RequestMapping("/layout")
	public String getTodoPartialPage() {
		return "todo/layout";
	}
	
	
	
	
	//Security filters for inputs needs to be added
		//This method takes in a String which is the new toDoTask to be added.
		//Call $http.post(URL,stringToAdd);
		@RequestMapping(value = "/updateToDoTask", method = RequestMethod.POST)
		@ResponseBody
		public ResponseEntity<String> updateToDoTask(@RequestBody String toDoTaskJSON,HttpServletRequest rq) {
			System.out.println("TYPE STH HERE FOR TODOTASK");
			try{
				Principal principal = rq.getUserPrincipal();
				User currUser = (User)userService.getUserByEmail(principal.getName()).get();
				Object obj = parser.parse(toDoTaskJSON);
				JSONObject jsonObject = (JSONObject) obj;

			
				long id = (Long)jsonObject.get("id");	
				//System.out.println(id);
				String date = (String)jsonObject.get("date");
				//System.out.println(date);
				Date dateParsed = new SimpleDateFormat("yyyy-MM-dd").parse(date);
				ToDoTask todo=toDoTaskRepository.findOne(id);
				Calendar cal = Calendar.getInstance();
				cal.setTime(dateParsed);
				cal.add(Calendar.DATE, 1); 
				todo.setDate(cal.getTime());
				toDoTaskRepository.saveAndFlush(todo);
				System.out.println("updating todotask to usrname: " + currUser.getEmail());
			}
			catch ( UserNotFoundException e){
				return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			catch (Exception e){
				System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
				return new ResponseEntity<String>(geeson.toJson("Server error in updating to do task"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
}