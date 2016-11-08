package application.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONArray;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import application.entity.AuditLog;
import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Level;
import application.entity.Unit;
import application.entity.UnitAttributeType;
import application.entity.UnitAttributeValue;
import application.entity.User;
import application.exception.InvalidPostalCodeException;
import application.exception.UserNotFoundException;
import application.service.BuildingService;
import application.service.ClientOrganisationService;
import application.service.LevelService;
import application.service.UnitAttributeService;
import application.service.UnitService;
import application.service.UserService;

@Controller
@RequestMapping("/property")
public class UnitAttributeController {

	@Autowired
	private final ClientOrganisationService clientOrganisationService;
	private final UserService userService;
	private final UnitAttributeService unitAttributeService;
	private final BuildingService buildingService;
	private final LevelService levelService;
	private final UnitService unitService;
	private JSONParser parser = new JSONParser();
	private Gson geeson = new Gson();


	@Autowired
	public UnitAttributeController( ClientOrganisationService clientOrganisationService,UserService userService, UnitAttributeService unitAttributeService
			,BuildingService buildingService,LevelService levelService,UnitService unitService) {
		super();
		
		this.clientOrganisationService = clientOrganisationService;
		this.userService = userService;
		this.unitAttributeService = unitAttributeService;
		this.buildingService=buildingService;
		this.levelService=levelService;
		this.unitService=unitService;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	//ADD ATTRIBUTE TYPE AND VALUE
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/saveDatas", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> saveDatas(@RequestBody String data,HttpServletRequest rq) throws UserNotFoundException,InvalidPostalCodeException {
		System.out.println("start saving CSV");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			//throw new UserNotFoundException("User was not found");
			return new ResponseEntity<String>(geeson.toJson("Current User was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj1 = parser.parse(data);
			JSONObject jsonObject = (JSONObject) obj1;
			JSONObject arrayData = (JSONObject)jsonObject.get("datas");				
			JSONArray datas = (JSONArray)arrayData.get("data");	
			JSONObject arrayAttributeType = (JSONObject)jsonObject.get("attributeTypes");				
			JSONArray attributeTypes = (JSONArray)arrayAttributeType.get("attributeType");	
			Set<UnitAttributeType> custmisedTypes= new HashSet<UnitAttributeType>();
			System.out.println("test1");
			//LOOP THROUGH EACH ATTRIBUTE TYPE
			for(int i = 0; i <attributeTypes.size(); i++){	
					JSONObject attributeTypeObj = (JSONObject)attributeTypes.get(i);									
					String attributeType =(String)attributeTypeObj.get("header");
					System.out.println("test2");
					if(!unitAttributeService.isDefaultAttribute(attributeType)){//OPT: IS NOT DEFAULT ATTRIBUTE TYPE OF THE UNIT OR SQUARE
						if(!unitAttributeService.attributeTypeExists(client,attributeType)){//OPT: CUSTOMISED ATTRIBUTE TYPE HAS NOT BEEN CREATED
							System.out.println("test3");
							if(!unitAttributeService.createAttributeTypeOnClient(client, attributeType)){//ERROR DURING CREATE CUSTOMISED ATTRIBUTE TYPE ON CLIENT
							System.out.println("Error on creating new attribute type "+attributeType);
							return new ResponseEntity<String>(geeson.toJson("Error on creating new attribute type "+attributeType),HttpStatus.INTERNAL_SERVER_ERROR);
							
						}
					}System.out.println("test4");
						UnitAttributeType custmisedType=unitAttributeService.getAttributeTypeByString(client,attributeType);
						custmisedTypes.add(custmisedType);//ADD NON-DEFAULT ATTRIBUTE TYPE TO attributeTypeStrings SET
				}	
			}//END FOR LOOP FOR ATTRIBUTE TYPES JSONARRAY
			
			System.out.println("test5");
			//LOOP THROUGH EACH DATA
			for(int i = 0; i <datas.size(); i++){	
					JSONObject dataObj = (JSONObject)datas.get(i);									
					//LOOP THROUGH EACH TYPE, GET VALUE OF EACH TYPE FOR THIS DATA
					String unitNumber =(String)dataObj.get("unitNumber");
					int dimensionWidth = Integer.parseInt((String) dataObj.get("width"));
					int dimensionLength = Integer.parseInt( (String) dataObj.get("length"));
					String description =(String)dataObj.get("description");
					Double rent =Double.parseDouble((String)dataObj.get("rent"));
					int left = Integer.parseInt((String) dataObj.get("left"));
					int top = Integer.parseInt((String) dataObj.get("top"));
					int levelNum =Integer.parseInt((String)dataObj.get("levelNum"));				
					String buildingName =(String)dataObj.get("buildingName");
					System.out.println("test6");
					//TRY TO GET BUILDING
					Set<Building> buildings=buildingService.getAllBuildings(client);
					Set<Level> levels;
					long buildingId=0;
					for(Building building:buildings){
						System.out.println("test7");
						if((buildingName).equals(building.getName())){
							buildingId=building.getId();
						}
					}
					if(buildingId==0){
						System.out.println("Building Name was not found");
						return new ResponseEntity<String>(geeson.toJson("Building Name "+buildingName+" was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
						
					}else{
						levels=levelService.getAllLevels(client, buildingId);
					}
					//TRY TO GET LEVEL
					long levelId=0;
					int levelLength=0;
					int levelWidth=0;
					for(Level level:levels){
						System.out.println("test8");
						int onelevelNum=level.getLevelNum();
						if(levelNum==onelevelNum){
							System.out.println("Found level "+onelevelNum+"id "+level.getId());
							levelId=level.getId();
							levelLength=level.getLength();
							levelWidth=level.getWidth();
						}
					}
					Unit unit;
					if(levelId==0||levelLength==0||levelWidth==0){
						System.out.println("level number was not found");
						return new ResponseEntity<String>(geeson.toJson("Level Number "+levelNum+" was not found"),HttpStatus.INTERNAL_SERVER_ERROR);
						
					}else if((left+dimensionLength)>levelLength||(top+dimensionWidth)>levelWidth){
						System.out.println("level number was not found");
						return new ResponseEntity<String>(geeson.toJson("Unit at position: top:"+top+"m, left:"+left+"m, with dimension: length:"+dimensionLength+"m, width:"+dimensionWidth+"m is out of boundary of level "+levelNum+": length:"+levelLength+"m, width:"+levelWidth+"m"),HttpStatus.INTERNAL_SERVER_ERROR);
						
					}else{//width of square== (800/levelLength)*dimensionLength of unit; height of square ==(800/levelLength)*dimensionWidth of unit
						unit=unitService.uploadUnitOnLevel(levelId, left, top, (int)(800*dimensionWidth/levelLength), (int)(800*dimensionLength/levelLength), "coral", "./svg/rect.svg", unitNumber,  left,  top,  dimensionWidth, dimensionLength, true, description);
						if(unit==null){
							return new ResponseEntity<String>(geeson.toJson("Unit at position: top: "+top+"m, left: "+left+"m is clashing with existing units on level "+levelNum),HttpStatus.INTERNAL_SERVER_ERROR);
							
						}
						//ADD OVERLAPPING CHECK IN UNIT SERVICE
					}
					//READ VALUE OF CUSTOMISED TYPES
					for(UnitAttributeType custmisedType: custmisedTypes){
						System.out.println("test9");
						String attributeType=custmisedType.getAttributeType();
						System.out.println(attributeType);
						String attributeValue =(String)dataObj.get(attributeType);
						System.out.println(attributeValue);
						if(!unitAttributeService.attributeValueExistsOnClient(client,attributeValue)){//UNITATTRIBUTEVALUE DOES NOT EXIST FOR THE CLIENT ORGANISATION
							System.out.println("test10");
							unitAttributeService.createAttributeValueOnTypeNUnit(attributeValue, custmisedType,unit);//CREATE THE VALUE ENTITY AND SET TO TYPE AND UNIT
							System.out.println("test11");
						}else{//UNITATTRIBUTEVALUE EXISTs FOR THE CLIENT ORGANISATION
							//UNITATTRIBUTEVALUE OBJECT
							UnitAttributeValue unitAttributeValue=unitAttributeService.getAttributeValueByStringOnClient(client,attributeValue);
							if(!unitAttributeService.attributeValueExistsOnType(custmisedType,unitAttributeValue)){//UNITATTRIBUTEVALUE EXIST FOR THE CLIENT ORGANISATION BUT NOT SET TO THE TYPE
								System.out.println("test12");
								if(!unitAttributeService.setAttributeValueonTypeNUnit(unitAttributeValue,custmisedType,unit)){//SET VALUE TO TYPE AND UNIT
									System.out.println("Error while setting attribute value "+unitAttributeValue.getAttributeValue());
									return new ResponseEntity<String>(geeson.toJson("Error setting attribute value "+unitAttributeValue.getAttributeValue() ),HttpStatus.INTERNAL_SERVER_ERROR);								
								}
							}else{//UNITATTRIBUTEVALUE EXIST FOR THE CLIENT ORGANISATION AND TYPE
								if(!unitAttributeService.setAttributeValueonUnit(unitAttributeValue,unit)){//SET VALUE TO UNIT 
									System.out.println("error while setting attribute value "+unitAttributeValue.getAttributeValue());
									return new ResponseEntity<String>(geeson.toJson("Error setting attribute value "+unitAttributeValue.getAttributeValue() ),HttpStatus.INTERNAL_SERVER_ERROR);								
								}
							}
							
						}

					}//END FOR LOOP FOR UNITATTRIBUTETYPE OBJECTS
					
			}//END FOR LOOP FOR DATA JSON OBJECTS
			
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<String>(geeson.toJson("Server error in adding attributes"),HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println("CSV WAS SAVED");
		return new ResponseEntity<String>(HttpStatus.OK);
	}	

}
