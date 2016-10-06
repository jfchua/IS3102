package application.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.domain.AuditLog;
import application.domain.ClientOrganisation;
import application.domain.Icon;
import application.domain.User;
import application.repository.AuditLogRepository;
import application.service.user.IconService;
import application.service.user.ClientOrganisationService;
import application.service.user.UserService;

@Controller
@RequestMapping("/property")
public class IconController {
	
	@Autowired
	private final IconService iconService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final AuditLogRepository auditLogRepository;
	private JSONParser parser = new JSONParser();
	

	@Autowired
	public IconController(AuditLogRepository auditLogRepository, IconService iconService, ClientOrganisationService clientOrganisationService,
			UserService userService) {
		super();
		this.iconService = iconService;
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.auditLogRepository = auditLogRepository;
	}
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	/*
	@RequestMapping(value = "/viewIcons", method = RequestMethod.GET)
	@ResponseBody
	public String viewIcons(HttpServletRequest rq) {//need to change String to ResponseEntity<>
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			JSONObject bd = new JSONObject(); 
			bd.put("error", "User is not verified"); 				
		    System.out.println("Error: Unauthorised user is trying to retrieve icons");
			return bd.toString();
			//return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Icon> icons = iconService.getAllIconFromClientOrganisation(client);		
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return false;
						}
						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							//TODO Auto-generated method stub
							return false;
						}

					})
					.serializeNulls()
					.create();



			String json = gson2.toJson(icons);
			return json;
		}
		catch (Exception e){
			JSONObject bd = new JSONObject(); 
			bd.put("error", "Cannot fetch Icons"); 				
		    System.out.println("Error: Fail to retrieve icons");
			return bd.toString();
		}
	}
	*/
	
	
	@RequestMapping(value = "/viewIcons", method = RequestMethod.GET)
	@ResponseBody
	public  ResponseEntity<Set<Icon>> viewIcons(HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			
			return new ResponseEntity<Set<Icon>>(HttpStatus.CONFLICT);
			//return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Icon> icons = iconService.getAllIconFromClientOrganisation(client);	
			System.out.println(icons);
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return false;
						}
						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							//TODO Auto-generated method stub
							return false;
						}

					})
					.serializeNulls()
					.create();



			String json = gson2.toJson(icons);
			return new ResponseEntity<Set<Icon>>(icons,HttpStatus.OK);
		}
		catch (Exception e){
			
			return new ResponseEntity<Set<Icon>>(HttpStatus.CONFLICT);
		}
	}
	
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/uploadIcon")
	public void uploadIcon(@RequestParam("file") MultipartFile file, HttpServletRequest request ) throws IOException {

		Principal p = request.getUserPrincipal();
		User curUser = userService.getUserByEmail(p.getName()).get();

		if (!file.isEmpty()) {
			//GET RELATIVE PATH SINCE EVERYONE COMPUTER DIFFERENT
			String filePath = request.getSession().getServletContext().getRealPath("/");
			
			System.err.println(filePath);
			File toTrans = new File(filePath,file.getOriginalFilename());
			toTrans.setExecutable(true);
			toTrans.setWritable(true);
			file.transferTo(toTrans);
			ClientOrganisation client = curUser.getClientOrganisation();
			client.setLogoFilePath(file.getOriginalFilename());
			//clientOrganisationRepository.saveAndFlush(client);
			System.out.println("Saved Logo");

		}


		System.err.println(String.format("received %s", file.getOriginalFilename()));
	}

	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/saveIcon")
	public void saveIcon(@RequestParam("file") MultipartFile file, String iconType, HttpServletRequest request ) throws IOException {

		Principal p = request.getUserPrincipal();
		User curUser = userService.getUserByEmail(p.getName()).get();

		if (!file.isEmpty()) {
			//GET RELATIVE PATH SINCE EVERYONE COMPUTER DIFFERENT
			String iconPath = request.getSession().getServletContext().getRealPath("/");
			System.out.println(iconPath);
			File toTrans = new File(iconPath,file.getOriginalFilename());
			toTrans.setExecutable(true);
			toTrans.setWritable(true);
			file.transferTo(toTrans);
			ClientOrganisation client = curUser.getClientOrganisation();
			boolean bl = iconService.createIconOnClientOrganisation(client, iconType, file.getOriginalFilename());
			if(bl){
				System.out.println("Icon is created");
			}else{
				System.out.println("Icon is not created");
			}
			//clientOrganisationRepository.saveAndFlush(client);
			System.out.println("Saved Logo");

		}


		System.err.println(String.format("received %s", file.getOriginalFilename()));
	}
	
	
	@RequestMapping(value = "/addIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addIcon(@RequestBody String iconJSON,HttpServletRequest rq) {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(iconJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			MultipartFile file=(MultipartFile)jsonObject.get("file");
			String iconType = (String)jsonObject.get("inconType");
			//String iconLabel = (String)jsonObject.get("inconLabel");
			//check if asset file exists for client
			//create a asset file for client
			//save assetFilePath/iconLabel.svg as iconPath
			String iconPath = "";

			boolean bl = iconService.createIconOnClientOrganisation(client, iconType, iconPath);
		
			if(bl){
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Add Icon: " + "");//add icon label here
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
			}
			else
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}	
	
	
	@RequestMapping(value = "/deleteIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> deleteIcon(@RequestBody String iconId,HttpServletRequest rq) {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			
			Object obj = parser.parse(iconId);
			JSONObject jsonObject = (JSONObject) obj;
			long id = (Long)jsonObject.get("id");
			boolean bl = iconService.deleteIconFromClientOrganisation(client, id);
			System.out.println("delete icon " + id);
			if ( bl ){
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Delete Icon: " + id);
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
			}
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/updateIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateIcon(@RequestBody String iconId,HttpServletRequest rq) {
		
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();		
			Object obj = parser.parse(iconId);
			JSONObject jsonObject = (JSONObject) obj;
			
			long id = (Long)jsonObject.get("id");
			String iconType = (String)jsonObject.get("iconType");		
			String iconLabel = (String)jsonObject.get("iconLabel");
			String iconPath="";//get iconPath from iconLabel
			//read the file and save to local directory
			boolean bl = iconService.editIcon(id, iconType, iconPath);
			if(!bl)
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Property");
			al.setAction("Update Icon: " + id);
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}


}
