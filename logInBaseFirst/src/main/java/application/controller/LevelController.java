package application.controller;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.reflect.TypeToken;

import application.entity.AuditLog;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Icon;
import application.entity.Level;
import application.entity.Unit;
import application.entity.User;
import application.exception.InvalidFileUploadException;
import application.exception.InvalidIconException;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.service.BuildingService;
import application.service.FileUploadCheckingService;
import application.service.LevelService;
import application.service.UserService;

@Controller
@RequestMapping("/level")
public class LevelController {
	@Autowired
	private final BuildingService buildingService;
	private final LevelService levelService;
	private final UserService userService;
	private JSONParser parser = new JSONParser();
	private final FileUploadCheckingService fileService;
	private AuditLogRepository auditLogRepository;
	private Gson geeson= new Gson();

	@Autowired
	public LevelController(AuditLogRepository auditLogRepository, LevelService levelService, UserService userService,BuildingService buildingService,FileUploadCheckingService fileService) {
		super();
		this.buildingService = buildingService;
		this.levelService = levelService;
		this.userService = userService;
		this.fileService=fileService;
		this.auditLogRepository = auditLogRepository;
	}
	
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY', 'ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of level objects.
		// Each object (building) will contain... long id, .
			@RequestMapping(value = "/viewLevels/{id}",  method = RequestMethod.GET)
			@ResponseBody
			public String viewLevels(@PathVariable("id") String buildingId, HttpServletRequest rq) throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return null; 
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();				
				long buildingIdLong = Long.parseLong(buildingId);
				Set<Level> levels = levelService.getAllLevels(client, buildingIdLong);
				System.err.println("There are " + levels.size() + " levels in the building");
				
				//Gson gson = new Gson();
				//String json = gson.toJson(levels);
			    //System.out.println("Returning levels with json of : " + json);
				//return json;
				
				Gson gson2 = new GsonBuilder()
					    .setExclusionStrategies(new ExclusionStrategy() {
					        public boolean shouldSkipClass(Class<?> clazz) {
					            return (clazz == Building.class)|| (clazz == Unit.class);
					        }

					        /**
					          * Custom field exclusion goes here
					          */

							@Override
							public boolean shouldSkipField(FieldAttributes f) {
								//TODO Auto-generated method stub
								return false;
										//(f.getDeclaringClass() == Level.class && f.getUnits().equals("units"));
							}

					     })
					    /**
					      * Use serializeNulls method if you want To serialize null values 
					      * By default, Gson does not serialize null values
					      */
					    .serializeNulls()
					    .create();
					
			
			    
			    String json = gson2.toJson(levels);
			    System.out.println(json);
			    return json;
				}
				catch (Exception e){
					return "cannot fetch";
				}
				//return new ResponseEntity<Void>(HttpStatus.OK);
			}
		
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			// Call this method using $http.get and you will get a JSON format containing an array of level objects.
			// Each object (building) will contain... long id, .
				@RequestMapping(value = "/getAllLevels/{id}",  method = RequestMethod.GET)
				@ResponseBody
				public String getAllLevels(@PathVariable("id") String buildingId, HttpServletRequest rq) throws UserNotFoundException {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return null; 
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();				
					    long buildingIdLong = Long.parseLong(buildingId);
						Set<Level> levels = levelService.getAllLevels(client, buildingIdLong);
					System.err.println("There are " + levels.size() + " levels in the building");
					
					//Gson gson = new Gson();
					//String json = gson.toJson(levels);
				    //System.out.println("Returning levels with json of : " + json);
					//return json;
					
					Gson gson2 = new GsonBuilder()
						    .setExclusionStrategies(new ExclusionStrategy() {
						        public boolean shouldSkipClass(Class<?> clazz) {
						            return (clazz == Building.class)|| (clazz == Unit.class);
						        }

						        /**
						          * Custom field exclusion goes here
						          */

								@Override
								public boolean shouldSkipField(FieldAttributes f) {
									//TODO Auto-generated method stub
									return false;
											//(f.getDeclaringClass() == Level.class && f.getUnits().equals("units"));
								}

						     })
						    /**
						      * Use serializeNulls method if you want To serialize null values 
						      * By default, Gson does not serialize null values
						      */
						    .serializeNulls()
						    .create();
						
				
				    
				    String json = gson2.toJson(levels);
				    System.out.println(json);
				    return json;
					}
					catch (Exception e){
						return "cannot fetch";
					}
					//return new ResponseEntity<Void>(HttpStatus.OK);
				}
			
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			// Call this method using $http.get and you will get a JSON format containing an array of building objects.
			// Each object (building) will contain... long id, collection of levels.
				@RequestMapping(value = "/getLevel/{id}", method = RequestMethod.GET)
				@ResponseBody
				public String getLevel(@PathVariable("id") String levelId, HttpServletRequest rq) throws UserNotFoundException {
					Principal principal = rq.getUserPrincipal();
					Optional<User> usr = userService.getUserByEmail(principal.getName());
					if ( !usr.isPresent() ){
						return null; 
					}
					try{
					    ClientOrganisation client = usr.get().getClientOrganisation();	
						long id = Long.parseLong(levelId);
						Level level = levelService.getLevelById(id).get();
						boolean bl = levelService.checkLevel(client, id);
						Gson gson2 = new GsonBuilder()
								.setExclusionStrategies(new ExclusionStrategy() {
									public boolean shouldSkipClass(Class<?> clazz) {
										return (clazz == Building.class)|| (clazz == Unit.class);
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
						String json = gson2.toJson(level);
						System.out.println("BUILDING IS " + json);

						return json;
					}
					catch (Exception e){
						return "cannot fetch";
					}
				}
			
			
			
			
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			//Security filters for inputs needs to be added
			//This method takes in a string which contains the attributes of the level to be added.
			//Call $http.post(URL,stringToAdd);
			@RequestMapping(value = "/addLevel", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<String> addLevel(@RequestBody String levelJSON,
					HttpServletRequest rq) throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return null; 
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();	
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					Object obj = parser.parse(levelJSON);
					JSONObject jsonObject = (JSONObject) obj;
					//Object obj1 = parser.parse(buildingJSON);
					//JSONObject jsonObject1 = (JSONObject) obj1;
					
					int levelNum = ((Long)jsonObject.get("levelNum")).intValue();
					int length = ((Long)jsonObject.get("length")).intValue();
					int width = ((Long)jsonObject.get("width")).intValue();
					String filePath = (String)jsonObject.get("filePath");
					
					long buildingId = (Long)jsonObject.get("id");
					
					Level level = levelService.create(client, buildingId, levelNum, length, width, filePath);
					//levelService.updateBuilding(buildingId, level.getId());
					System.out.println("adding level " + levelNum);
					if(level==null){
						System.out.println("out of range");
						return new ResponseEntity<String>(HttpStatus.CONFLICT);
					}else{
						String levelIdToReturn = Long.toString(level.getId());
						
						AuditLog al = new AuditLog();
						al.setTimeToNow();
						al.setSystem("Property");
						al.setAction("Add level: id" + levelIdToReturn);
						al.setUser(usr.get());
						al.setUserEmail(usr.get().getEmail());
						auditLogRepository.save(al);
						
						
						return new ResponseEntity<String>(levelIdToReturn ,HttpStatus.OK);
					}				
				}
				catch (Exception e){
					System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}
				//return new ResponseEntity<String>(HttpStatus.OK);
			
			}	
			
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			//This method takes in a String which is the ID of the level to be deleted
			// Call $http.post(URL,(String)id);
			@RequestMapping(value = "/deleteLevel", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<String> deleteLevel(@RequestBody String levelJSON, HttpServletRequest rq) throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return null; 
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();	
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					Object obj = parser.parse(levelJSON);
					JSONObject jsonObject = (JSONObject) obj;
					//long buildingId = (Long)jsonObject.get("buildingId");
					long levelId = (Long)jsonObject.get("levelId");
					boolean bl = levelService.deleteLevel(client, levelId);

					if(!bl){
						System.out.println("cannot delete");
						return new ResponseEntity<String>(geeson.toJson("Unable to delete level with existing events"),HttpStatus.INTERNAL_SERVER_ERROR);
					}	
					AuditLog al = new AuditLog();
					al.setTimeToNow();
					al.setSystem("Property");
					al.setAction("Delete level: id" + levelId);
					al.setUser(usr.get());
					al.setUserEmail(usr.get().getEmail());
					auditLogRepository.save(al);
				}
				catch (Exception e){
					return new ResponseEntity<String>(geeson.toJson("Server error deleting building"),HttpStatus.INTERNAL_SERVER_ERROR);
				}

				
				return new ResponseEntity<String>(HttpStatus.OK);
			}
			
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			//This method takes in a JSON format which contains an object with 5 attributes
			//Long/String id, int levelNum, int length, int width, String filePath
			//Call $httpPost(Url,JSONData);
			@RequestMapping(value = "/updateLevel", method = RequestMethod.POST)
			@ResponseBody
			public ResponseEntity<Void> updateLevel(@RequestBody String levelJSON, HttpServletRequest rq) throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return null; 
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();
					Object obj = parser.parse(levelJSON);
					JSONObject jsonObject = (JSONObject) obj;
					//long buildingId = (Long)jsonObject.get("buildingId");
					long levelId = (Long)jsonObject.get("levelId");
					int levelNum = ((Long)jsonObject.get("levelNum")).intValue();
					int length = ((Long)jsonObject.get("length")).intValue();
					int width = ((Long)jsonObject.get("width")).intValue();
					String filePath = (String)jsonObject.get("filePath");		
					//Principal principal = rq.getUserPrincipal();
					//User currUser = (User)userService.getUserByEmail(principal.getName()).get();
					boolean bl=levelService.editLevelInfo(client, levelId,levelNum, length, width, filePath);
					//levelService.updateBuildingWithOnlyLevelId(levelId);
					if(!bl){
						System.out.println("out of range");
						return new ResponseEntity<Void>(HttpStatus.CONFLICT);
					}
					AuditLog al = new AuditLog();
					al.setTimeToNow();
					al.setSystem("Property");
					al.setAction("Update level: id" + levelId);
					al.setUser(usr.get());
					al.setUserEmail(usr.get().getEmail());
					auditLogRepository.save(al);
				}
				catch (Exception e){
					return new ResponseEntity<Void>(HttpStatus.CONFLICT);
				}
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
			
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			//input level id x as {id:x}; output building id y as {buildingId:y}
			@RequestMapping(value = "/getBuildingId",  method = RequestMethod.POST)//hailing
			@ResponseBody
			public String getBuildingId(@RequestBody String level,HttpServletRequest rq) throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return null; 
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();
				System.out.println("test hailing");
				Object obj = parser.parse(level);
				JSONObject jsonObject = (JSONObject) obj;
				
				long levelId = (Long)jsonObject.get("id");
				System.out.println("Returning building id : "+levelService.getBuildingByLevelId(client, levelId));
				long buildingId = levelService.getBuildingByLevelId(client, levelId);
				if(buildingId==0){
					return "{\"error\":\"cannot fetch}\"";
				}else{					
					JSONObject bd = new JSONObject(); 
					bd.put("buildingId", buildingId); 				
				    System.out.println("Returning building id : " + bd.toString());
					return bd.toString();
				}
				
				
				}
				catch (Exception e){
					return "{\"error\":\"cannot fetch}\"";
				}
				//return new ResponseEntity<Void>(HttpStatus.OK);
			}
			
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
			//input level id x as {id:x}; output building y as 
			@RequestMapping(value = "/getBuilding",  method = RequestMethod.POST)//hailing
			@ResponseBody
			public ResponseEntity<Building> getBuilding(@RequestBody String level,HttpServletRequest rq) throws UserNotFoundException {
				Principal principal = rq.getUserPrincipal();
				Optional<User> usr = userService.getUserByEmail(principal.getName());
				if ( !usr.isPresent() ){
					return new ResponseEntity<Building>(HttpStatus.CONFLICT);
				}
				try{
				    ClientOrganisation client = usr.get().getClientOrganisation();
				System.out.println("test hailing");
				Object obj = parser.parse(level);
				JSONObject jsonObject = (JSONObject) obj;
				
				long levelId = (Long)jsonObject.get("id");
				System.out.println("Returning building id : "+levelService.getBuildingByLevelId(client, levelId));
				long buildingId = levelService.getBuildingByLevelId(client, levelId);
				if(buildingId==0){
					return new ResponseEntity<Building>(HttpStatus.CONFLICT);
				}else{					
					
					Building building=buildingService.getBuilding(buildingId);
					return new ResponseEntity<Building>(building,HttpStatus.OK);
				}
				
				
				}
				catch (Exception e){
					return new ResponseEntity<Building>(HttpStatus.CONFLICT);
				}
				//return new ResponseEntity<Void>(HttpStatus.OK);
			}
			
	         
	         @PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	     	@RequestMapping(value = "/saveLevelImage",  method = RequestMethod.POST)
	     	@ResponseBody
	     	public ResponseEntity<String> saveLevelImage(@RequestParam("file") MultipartFile file,String levelId, HttpServletRequest request ) throws IOException, UserNotFoundException, InvalidIconException {

	     		Principal p = request.getUserPrincipal();
	     		User curUser = userService.getUserByEmail(p.getName()).get();
	     		//CHECKING FOR FILE VALIDITY
	     		if ( levelId == null || levelId.equals("")){
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
	     			System.err.println("saveLevelImage in controller saving to " + Long.valueOf(levelId));
	     			boolean OK = levelService.saveImageToLevel(Long.valueOf(levelId), file.getOriginalFilename());
	     			if(OK){
	     				System.out.println("Level image is created");
	     				
	     			}else{
	     				System.out.println("Level image is not created");
	     			}
	     			//clientOrganisationRepository.saveAndFlush(client);
	     			System.out.println("Saved Level image");
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
