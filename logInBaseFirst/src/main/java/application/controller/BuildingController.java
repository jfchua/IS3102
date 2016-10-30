package application.controller;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
//import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.AuditLog;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Level;
import application.entity.User;
import application.exception.BuildingNotFoundException;
import application.exception.InvalidFileUploadException;
import application.exception.InvalidIconException;
import application.exception.InvalidPostalCodeException;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.BuildingRepository;
import application.service.BuildingService;
import application.service.ClientOrganisationService;
import application.service.FileUploadCheckingService;
import application.service.UserService;

@Controller
@RequestMapping("/building")
public class BuildingController {
	@Autowired
	private final BuildingService buildingService;
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final AuditLogRepository auditLogRepository;
	private final BuildingRepository buildingRepository;
	private final FileUploadCheckingService fileService;
	private Gson geeson= new Gson();

	private JSONParser parser = new JSONParser();

	@Autowired
	public BuildingController(FileUploadCheckingService fileService,BuildingRepository buildingRepository,AuditLogRepository auditLogRepository, BuildingService buildingService, ClientOrganisationService clientOrganisationService,
			UserService userService) {
		super();
		this.buildingService = buildingService;
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.auditLogRepository = auditLogRepository;
		this.buildingRepository = buildingRepository;
		this.fileService = fileService;
	}


	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY', 'ROLE_EXTEVE')")
	@RequestMapping(value = "/viewBuildings", method = RequestMethod.GET)
	@ResponseBody
	public String viewBuildings(HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return "ERROR";//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Building> buildings = client.getBuildings();
			//Gson gson = new Gson();
			//String json = gson.toJson(buildings);
			//System.out.println("Returning buildings with json of : " + json);
			//return json;	
			System.out.println(buildings);
			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Level.class);
						}

						/**
						 * Custom field exclusion goes here
						 */

						@Override
						public boolean shouldSkipField(FieldAttributes f) {
							//TODO Auto-generated method stub
							return false;
						}

					})
					/**
					 * Use serializeNulls method if you want To serialize null values 
					 * By default, Gson does not serialize null values
					 */
					.serializeNulls()
					.create();



			String json = gson2.toJson(buildings);
			System.out.println("BUILDING IS " + json);

			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}	

	// Call this method using $http.get and you will get a JSON format containing an array of building objects.
	// Each object (building) will contain... long id, collection of levels.
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/getBuilding/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getBuilding(@PathVariable("id") String buildingId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return null; 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();	
			long id = Long.parseLong(buildingId);
			boolean bl =buildingService.checkBuilding(client, id);
			Building build = buildingService.getBuildingById(id).get();
			if(bl){
				Gson gson2 = new GsonBuilder()
						.setExclusionStrategies(new ExclusionStrategy() {
							public boolean shouldSkipClass(Class<?> clazz) {
								return (clazz == Level.class);
							}
							/**
							 * Custom field exclusion goes here
							 */
							@Override
							public boolean shouldSkipField(FieldAttributes f) {
								//TODO Auto-generated method stub
								return false;
							}
						})
						/**
						 * Use serializeNulls method if you want To serialize null values 
						 * By default, Gson does not serialize null values
						 */
						.serializeNulls()
						.create();
				String json = gson2.toJson(build);
				System.out.println("BUILDING IS " + json);

				return json;
			}
			else
				return geeson.toJson("Error in retreiving building of id " + buildingId);
		}
		catch ( BuildingNotFoundException e){
			return geeson.toJson("Error in retreiving building of id " + buildingId);
		}
		catch (Exception e){
			return geeson.toJson("Error in retreiving building of id " + buildingId);
		}


	}
	//Security filters for inputs needs to be added
	//This method takes in a string which contains the attributes of the building to be added.
	//Call $http.post(URL,stringToAdd);
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/addBuilding", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> addBuilding(@RequestBody String buildingJSON,HttpServletRequest rq) throws UserNotFoundException,InvalidPostalCodeException {
		System.out.println("startADD");
		String buildingInputName = "";
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			//throw new UserNotFoundException("User was not found");
			return new ResponseEntity<String>(geeson.toJson("Current User was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(buildingJSON);
			JSONObject jsonObject = (JSONObject) obj1;
			String name = (String)jsonObject.get("name");

			String address = (String)jsonObject.get("address");
			String postalCode = (String)jsonObject.get("postalCode");
			System.out.println(postalCode);
			String city = (String)jsonObject.get("city");
			int numFloor = ((Long)jsonObject.get("numFloor")).intValue();
			String filePath = (String)jsonObject.get("filePath");

			boolean bl = buildingService.create(client, name, address, postalCode, city, numFloor, filePath);
			System.out.println("adding building " + name);
			if(bl){
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Add Building: " + name);
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
			}
			else{
				return new ResponseEntity<String>(geeson.toJson("Server error in adding new building"),HttpStatus.INTERNAL_SERVER_ERROR);
			}
			buildingInputName = name;
		}
		catch ( InvalidPostalCodeException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in adding new building"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Building build = buildingRepository.getBuildingByName(buildingInputName);
		String buildingIdToReturn = Long.toString(build.getId());
		System.out.println("Gotten building of id" + build.getId() + " from name of " + buildingInputName);
		return new ResponseEntity<String>(buildingIdToReturn,HttpStatus.OK);
	}	

	//This method takes in a String which is the ID of the building to be deleted
	// Call $http.post(URL,(String)id);

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/deleteBuilding", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteBuilding(@RequestBody String buildingId,HttpServletRequest rq) throws UserNotFoundException {

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("User was not found"),HttpStatus.INTERNAL_SERVER_ERROR);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}

		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println(buildingId + " delete!!");
			Object obj = parser.parse(buildingId);
			JSONObject jsonObject = (JSONObject) obj;
			long id = (Long)jsonObject.get("id");
			boolean bl = buildingService.deleteBuilding(client,id);
			System.out.println("delete building " + id);
			System.out.println(bl);
			if ( bl ){
				AuditLog al = new AuditLog();
				al.setTimeToNow();
				al.setSystem("Property");
				al.setAction("Delete Building: id" + id);
				al.setUser(usr.get());
				al.setUserEmail(usr.get().getEmail());
				auditLogRepository.save(al);
			}
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		catch ( BuildingNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error deleting building"),HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	//This method takes in a JSON format which contains an object with 7 attributes
	//Long/String id, String name, String address, int postalCode, String city, 
	//int numFloor, String filePath;
	//Call $httpPost(Url,JSONData);
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/updateBuilding", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> updateBuilding(@RequestBody String buildingId,HttpServletRequest rq) throws UserNotFoundException {
		String buildingInputName = "";
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<String>(geeson.toJson("User was not found"),HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();		
			Object obj = parser.parse(buildingId);
			JSONObject jsonObject = (JSONObject) obj;
			System.out.println("start");
			long id = (Long)jsonObject.get("id");
			String name = (String)jsonObject.get("name");
			System.out.println("after name");
			String address = (String)jsonObject.get("address");
			System.out.println("after address");
			String postalCode = (String)jsonObject.get("postalCode");
			System.out.println("after post");
			String city = (String)jsonObject.get("city");
			System.out.println("after city");
			int numFloor = ((Long)jsonObject.get("numFloor")).intValue();
			String filePath = (String)jsonObject.get("filePath");
			//Principal principal = rq.getUserPrincipal();
			//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
			boolean bl = buildingService.editBuildingInfo(client,id, name, address, postalCode, city, numFloor);
			if(!bl){
				return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			buildingInputName = name;

			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Property");
			al.setAction("Update Building: " + buildingService.getBuildingById(id).get().getName());
			al.setUser(usr.get());
			al.setUserEmail(usr.get().getEmail());
			auditLogRepository.save(al);
		}
		catch ( BuildingNotFoundException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( InvalidPostalCodeException e){
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch (Exception e){
			return new ResponseEntity<String>(geeson.toJson("Server error in updating building"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Building build = buildingRepository.getBuildingByName(buildingInputName);
		String buildingIdToReturn = Long.toString(build.getId());
		return new ResponseEntity<String>(buildingIdToReturn,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveBuildingImage")
	public ResponseEntity<String> saveBuildingImage(@RequestParam("file") MultipartFile file,String buildingId, HttpServletRequest request ) throws IOException, UserNotFoundException, InvalidIconException {

		Principal p = request.getUserPrincipal();
		User curUser = userService.getUserByEmail(p.getName()).get();
		//CHECKING FOR FILE VALIDITY
		if ( buildingId == null || buildingId.equals("")){
			return new ResponseEntity<String>(geeson.toJson("Server error in saving building information"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			fileService.checkFile(file);
			//GET RELATIVE PATH SINCE EVERYONE COMPUTER DIFFERENT
			String iconPath = request.getSession().getServletContext().getRealPath("/");
			System.out.println(iconPath);
			File toTrans = new File(iconPath,file.getOriginalFilename());
			toTrans.setExecutable(true);
			toTrans.setWritable(true);
			file.transferTo(toTrans);
			System.err.println("saveBuildingImage in controller saving to " + Long.valueOf(buildingId));
			boolean OK = buildingService.saveImageToBuilding(Long.valueOf(buildingId), file.getOriginalFilename());
			if(OK){
				System.out.println("Building image is created");
			}else{
				System.out.println("Building image is not created");
			}
			//clientOrganisationRepository.saveAndFlush(client);
			System.out.println("Saved Logo");
		}
		catch ( InvalidFileUploadException e ){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		catch ( Exception e){
			System.err.println(e.getMessage());
			return new ResponseEntity<String>(geeson.toJson(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.err.println(String.format("received %s", file.getOriginalFilename()));
		return new ResponseEntity<String>(HttpStatus.OK);
	}


}
