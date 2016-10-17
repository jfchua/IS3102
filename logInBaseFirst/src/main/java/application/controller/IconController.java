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
import org.springframework.security.access.prepost.PreAuthorize;
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

import application.entity.AuditLog;
import application.entity.ClientOrganisation;
import application.entity.Icon;
import application.entity.User;
import application.exception.IconNotFoundException;
import application.exception.InvalidFileUploadException;
import application.exception.InvalidIconException;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.service.ClientOrganisationService;
import application.service.FileUploadCheckingService;
import application.service.IconService;
import application.service.UserService;

@Controller
@RequestMapping("/property")
public class IconController {

	@Autowired
	private final IconService iconService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final FileUploadCheckingService fileService;
	private final AuditLogRepository auditLogRepository;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();


	@Autowired
	public IconController(FileUploadCheckingService fileService, AuditLogRepository auditLogRepository, IconService iconService, ClientOrganisationService clientOrganisationService,
			UserService userService) {
		super();
		this.fileService = fileService;
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

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/viewIcons", method = RequestMethod.GET)
	@ResponseBody
	public  ResponseEntity<Set<Icon>> viewIcons(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){

			return new ResponseEntity<Set<Icon>>(HttpStatus.CONFLICT);
			//return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			//Set<Icon> icons = iconService.getAllIconFromClientOrganisation(client);	
			Set<Icon> icons = client.getIcons();	
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

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/saveIcon")
	public ResponseEntity<String> saveIcon(@RequestParam("file") MultipartFile file, String iconType, HttpServletRequest request ) throws IOException, UserNotFoundException, InvalidIconException {

		Principal p = request.getUserPrincipal();
		User curUser = userService.getUserByEmail(p.getName()).get();
		//CHECKING FOR FILE VALIDITY
		try{
			fileService.checkFile(file);
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
		catch ( InvalidFileUploadException e ){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( InvalidIconException e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.err.println(String.format("received %s", file.getOriginalFilename()));
		return new ResponseEntity<String>(HttpStatus.OK);
	}





	/*
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

	 */
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/deleteIcon", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteIcon(@RequestBody String iconId,HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("User was not found"),HttpStatus.INTERNAL_SERVER_ERROR);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
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
		catch ( IconNotFoundException e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@ResponseStatus(HttpStatus.OK)
	@RequestMapping(value = "/updateIcon")
	public ResponseEntity<String> updateIcon(@RequestParam("file") MultipartFile file, Long id, HttpServletRequest request ) throws IOException, UserNotFoundException, IconNotFoundException {
		System.out.println("test");

		Principal p = request.getUserPrincipal();
		User curUser = userService.getUserByEmail(p.getName()).get();
		ClientOrganisation client = curUser.getClientOrganisation();
		System.out.print("client id "+client.getId());
		try{
			fileService.checkFile(file);
			if (!file.isEmpty()) {
				//GET RELATIVE PATH SINCE EVERYONE COMPUTER DIFFERENT
				//Long id=Long.valueOf(iconId);
				String iconPath = request.getSession().getServletContext().getRealPath("/");
				System.out.println(iconPath);
				File toTrans = new File(iconPath,file.getOriginalFilename());
				toTrans.setExecutable(true);
				toTrans.setWritable(true);
				file.transferTo(toTrans);

				boolean bl = iconService.editIcon(client,id, file.getOriginalFilename());
				if(bl){
					System.out.println("Icon is updated");
				}else{
					System.out.println("Icon is not updated");
				}
				//clientOrganisationRepository.saveAndFlush(client);
				System.out.println("Saved Icon");

			}
		}
		catch ( IconNotFoundException e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( InvalidFileUploadException e ){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( InvalidIconException e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in updating icon"),HttpStatus.INTERNAL_SERVER_ERROR);
		}


		System.err.println(String.format("received %s", file.getOriginalFilename()));
		return new ResponseEntity<String>(HttpStatus.OK);

	}



}
