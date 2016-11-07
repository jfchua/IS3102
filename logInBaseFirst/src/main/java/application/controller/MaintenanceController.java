package application.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import application.entity.Area;
import application.entity.BookingAppl;
import application.entity.Category;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Level;
import application.entity.Maintenance;
import application.entity.MaintenanceSchedule;
import application.entity.PaymentPlan;
import application.entity.Ticket;
import application.entity.Unit;
import application.entity.UnitAttributeValue;
import application.entity.User;
import application.entity.Vendor;
import application.exception.UserNotFoundException;
import application.service.MaintenanceService;
import application.service.UnitService;
import application.service.UserService;
import application.service.VendorService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {
	@Autowired
	private ApplicationContext context;
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	private final UnitService unitService;
	private final MaintenanceService maintenanceService;
	private final VendorService vendorService;
	private final UserService userService;
	private JSONParser parser = new JSONParser();

	@Autowired
	public MaintenanceController(UnitService unitService, MaintenanceService maintenanceService, 
			VendorService vendorService, UserService userService) {
		super();
		this.unitService = unitService;
		this.maintenanceService = maintenanceService;
		this.vendorService = vendorService;
		this.userService = userService;
	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewAllSchedules/{id}",  method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> viewAllSchedules(@PathVariable("id") String bId, HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start view");
		Principal principal = rq.getUserPrincipal();
		Optional<User> user1 = userService.getUserByEmail(principal.getName());
		if ( !user1.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		try{
			//EventOrganizer eventOrg = eventOrg1.get();	
			User user = user1.get();
			ClientOrganisation client = user.getClientOrganisation();
			System.out.println(user.getId());
			long id = Long.parseLong(bId);
			Set<MaintenanceSchedule> schedules = maintenanceService.getMaintenanceSchedule(client,id);	
			System.out.println("There are " + schedules.size() + " events under this maintenance request");

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz == Unit.class)||(clazz == Maintenance.class);
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
			JSONArray jArray = new JSONArray();

			for(MaintenanceSchedule ms : schedules){
				JSONObject obj1 = new JSONObject();
				obj1.put("id", ms.getId());
				obj1.put("start", String.valueOf(ms.getStart_time()));
				obj1.put("end", String.valueOf(ms.getEnd_time()));
				obj1.put("maint", ms.getMaintenance().getId());
				obj1.put("unit", ms.getRoom());
				String vendors = "";
				Maintenance mt = ms.getMaintenance();
				Set<Vendor> vds = mt.getVendors();
				for(Vendor v : vds){
					vendors += v.getName() + " ";
				}
				obj1.put("vendors", vendors);
				jArray.add(obj1);
			}
			return new ResponseEntity<String>(jArray.toString(), HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/deleteSchedule/{id}", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> deleteSchedule(@PathVariable("id") String bId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT); 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			long id = Long.parseLong(bId);
			boolean bl = maintenanceService.deleteSchedule(client, id);
			//BookingAppl booking= bookingService.getBookingById(id).get();
			System.err.println("delete schedule " + bl);
			if(!bl){
				System.err.println("cannot delete schedule");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}				


	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	// Call this method using $http.get and you will get a JSON format containing an array of event objects.
	// Each object (building) will contain... long id, collection of levels.
	@RequestMapping(value = "/getMaintenance/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String getMaintenance(@PathVariable("id") String maintId, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return null; 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			long id = Long.parseLong(maintId);
			boolean bl = maintenanceService.checkMaintenance(client, id);
			Maintenance maint = maintenanceService.getMaintenanceById(id).get();
			if(bl){
				Gson gson2 = new GsonBuilder()
						.setExclusionStrategies(new ExclusionStrategy() {
							public boolean shouldSkipClass(Class<?> clazz) {
								return (clazz == MaintenanceSchedule.class);
							}
							@Override
							public boolean shouldSkipField(FieldAttributes f) {
								//TODO Auto-generated method stub
								return false;
							}
						})
						.serializeNulls()
						.create();
				JSONObject obj = new JSONObject();
				obj.put("id", maint.getId());
				System.out.println(maint.getId());
				String unit = maintenanceService.getUnitsId(id);
				obj.put("units", unit);
				String vendor = maintenanceService.getVendorsId(id);
				obj.put("vendors", vendor);
				System.out.println(unit);
				obj.put("start", String.valueOf(maint.getStart()));
				System.out.println(maint.getStart());
				obj.put("end", String.valueOf(maint.getEnd()));
				System.out.println(maint.getEnd());
				obj.put("description", maint.getDescription());
				//obj.put("event_period", event.getEvent_period());
				System.out.println(obj.toString());
				return obj.toString();
			}
			else
				return "cannot fetch";	
		}
		catch (Exception e){
			return "cannot fetch";
		}
	}

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/checkAvailability", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> checkAvailability(@RequestBody String eventJSON,
			HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start check availability for events");
		DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
		Principal principal = rq.getUserPrincipal();
		System.out.println(principal.getName());
		Optional<User> usr1 = userService.getUserByEmail(principal.getName());
		if ( !usr1.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			User usr = usr1.get();
			ClientOrganisation client = usr.getClientOrganisation();
			System.out.println(usr.getName());
			Object obj = parser.parse(eventJSON);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray units = (JSONArray)jsonObject.get("units");
			String unitsId = "";
			for(int i = 0; i < units.size(); i++){
				JSONObject unitObj = (JSONObject)units.get(i);		
				System.out.println(unitObj.toString());
				long unitId = (Long)unitObj.get("id");
				System.out.println(unitId);
				unitsId = unitsId+unitId + " ";
				System.out.println(unitsId);
			}
			Date start = sdf.parse((String)jsonObject.get("start"));
			Date end = sdf.parse((String)jsonObject.get("end"));				
			boolean bl = maintenanceService.checkAvailability(client, usr, unitsId, start, end);
			if(!bl){
				System.out.println("NOT AVAILABLE");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}			
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);	
	}	

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	@RequestMapping(value = "/checkAvailabilityForUpdate", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> checkAvailabilityForUpdate(@RequestBody String eventJSON,
			HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start check availability for events");
		DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
		Principal principal = rq.getUserPrincipal();
		System.out.println(principal.getName());
		Optional<User> user1 = userService.getUserByEmail(principal.getName());
		System.err.println("111");
		if ( !user1.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);//NEED ERROR HANDLING BY RETURNING HTTP ERROR
		}
		try{
			System.err.println("112");
			System.err.println(eventJSON);
			User user = user1.get();
			ClientOrganisation client = user.getClientOrganisation();
			System.out.println(user.getName());
			Object obj = parser.parse(eventJSON);
			System.err.println("113");
			JSONObject jsonObject = (JSONObject) obj;
			System.err.println("114");
			long maintId = (Long)jsonObject.get("id");
			System.err.println(maintId);
			JSONArray units = (JSONArray)jsonObject.get("units");
			String unitsId = "";
			for(int i = 0; i < units.size(); i++){
				JSONObject unitObj = (JSONObject)units.get(i);		
				System.err.println("HAHAHA");
				long unitId = (Long)unitObj.get("id");
				//System.out.println(unitId);
				unitsId = unitsId+unitId + " ";
				System.out.println(unitsId);
			}
			Date start = sdf.parse((String)jsonObject.get("start"));
			Date end = sdf.parse((String)jsonObject.get("end"));				
			boolean bl = maintenanceService.checkAvailabilityForUpdate(client, user, maintId, unitsId, start, end);
			if(!bl){
				System.out.println("NOT AVAILABLE");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}			
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);	
	}	


	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	/*	@RequestMapping(value = "/viewAllUnits",  method = RequestMethod.GET)
		@ResponseBody
		public String viewAllUnits(HttpServletRequest rq) {
			Principal principal = rq.getUserPrincipal();
			Optional<User> usr = userService.getUserByEmail(principal.getName());
			if ( !usr.isPresent() ){
				return "ERROR"; 
			}
			try{
			ClientOrganisation client = usr.get().getClientOrganisation();
		    System.out.println("start view");
		    Set<Unit> allUnits = maintenanceService.getAllUnits(client);

			System.out.println("There are " + allUnits.size() + " vendors");   
			Gson gson2 = new GsonBuilder()
				    .setExclusionStrategies(new ExclusionStrategy() {
				        public boolean shouldSkipClass(Class<?> clazz) {
				            return (clazz == Level.class)||(clazz==Event.class);
				        }

						@Override
						public boolean shouldSkipField(FieldAttributes f) {

							return false;

						}

				     })
			    .serializeNulls()
				    .create();			    
		    String json = gson2.toJson(allUnits);
		    System.out.println(json);
		    return json;
			}
			catch (Exception e){
				return "cannot fetch";
			}

		}*/
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//This method takes in a String which is the ID of the event to be deleted
	// Call $http.post(URL,(String)id);
	@RequestMapping(value = "/deleteMaintenance", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> deleteMaintenance(@RequestBody String maintenanceJSON, HttpServletRequest rq) throws UserNotFoundException {
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT); 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			System.out.println("Start deleting");
			Object obj = parser.parse(maintenanceJSON);
			JSONObject jsonObject = (JSONObject) obj;
			long id = (Long)jsonObject.get("id");
			System.out.println(id);	
			boolean bl = maintenanceService.deleteMaintenance(client, id);
			if(!bl){
				System.out.println("cannot delete maintenance");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);	
			}	
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}				
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//This method takes in a JSON format which contains an object with 5 attributes
	//Long/String id, int levelNum, int length, int width, String filePath
	//Call $httpPost(Url,JSONData);
	@RequestMapping(value = "/updateMaintenance", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> updateMaintenance(@RequestBody String maintenanceJSON, HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start updating not yet");

		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT); 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			DateFormat format = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
			System.out.println("start updating");
			Object obj = parser.parse(maintenanceJSON);
			JSONObject jsonObject = (JSONObject) obj;
			long id = (Long)jsonObject.get("id");
			System.out.println(id);


			JSONArray vendors = (JSONArray)jsonObject.get("vendors");
			String vendorsId = "";
			for(int i = 0; i < vendors.size(); i++){
				JSONObject vendorObj = (JSONObject)vendors.get(i);		
				long vendorId = (Long)vendorObj.get("id");
				vendorsId = vendorsId+vendorId + " ";
				System.out.println(vendorsId);
			}
			JSONArray units = (JSONArray)jsonObject.get("units");
			String unitsId = "";
			for(int i = 0; i < units.size(); i++){
				JSONObject unitObj = (JSONObject)units.get(i);		
				long unitId = (Long)unitObj.get("id");
				unitsId = unitsId+unitId + " ";
				System.out.println(unitsId);
			}
			Date start = format.parse((String)jsonObject.get("start"));
			Date end = format.parse((String)jsonObject.get("end"));			
			String description = (String)jsonObject.get("description");
			System.out.println("end of controller");
			boolean bl = maintenanceService.editMaintenance(client, id, unitsId, vendorsId, start, end, description);
			//levelService.editLevelInfo(levelId,levelNum, length, width, filePath);
			System.err.println("success????"+bl);
			if(!bl){
				System.out.println("cannot update maintenance");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);	
			}			
			//else
			//eventExternalService.updateEventOrganizerWithOnlyEventId(eventId);
		}
		catch (Exception e){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	//Security filters for inputs needs to be added
	//This method takes in a string which contains the attributes of the event to be added.
	//Call $http.post(URL,stringToAdd);
	@RequestMapping(value = "/addMaintenance", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Void> addMaintenance(@RequestBody String maintenanceJSON, HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start adding");
		DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("start adding");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return new ResponseEntity<Void>(HttpStatus.CONFLICT); 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Object obj = parser.parse(maintenanceJSON);
			JSONObject jsonObject = (JSONObject) obj;

			JSONArray units = (JSONArray)jsonObject.get("units");
			String unitsId = "";
			for(int i = 0; i < units.size(); i++){
				JSONObject unitObj = (JSONObject)units.get(i);		
				long unitId = (Long)unitObj.get("id");
				unitsId = unitsId+unitId + " ";
			}
			JSONArray vendors = (JSONArray)jsonObject.get("vendors");
			String vendorsId = "";
			for(int i = 0; i < vendors.size(); i++){
				JSONObject vendorObj = (JSONObject)vendors.get(i);		
				long vendorId = (Long)vendorObj.get("id");
				vendorsId = vendorsId+vendorId + " ";

			}

			Date start = sdf.parse((String)jsonObject.get("start"));
			Date end = sdf.parse((String)jsonObject.get("end"));
			String description = (String)jsonObject.get("description");

			boolean bl = maintenanceService.createMaintenance(client, unitsId, vendorsId, start, end, description);
			System.out.println("adding maint " + vendorsId);
			if(!bl){
				System.out.println("cannot add maintenance");
				return new ResponseEntity<Void>(HttpStatus.CONFLICT);
			}			
		}
		catch (Exception e){
			System.out.println("EEPTOIN" + e.toString() + "   " + e.getMessage());
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);	
	}		

	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY','ROLE_EVENT','ROLE_EXTEVE')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewMaintenance",  method = RequestMethod.GET)
	@ResponseBody
	public String viewMaintenance(HttpServletRequest rq) throws UserNotFoundException {	
		System.out.println("start viewing all");
		Principal principal = rq.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		if ( !usr.isPresent() ){
			return null; 
		}
		try{
			ClientOrganisation client = usr.get().getClientOrganisation();
			Set<Maintenance> maints = maintenanceService.getAllMaintenance(client);
			System.out.println("There are " + maints.size() + " events under this organizer");

			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;

			Gson gson2 = new GsonBuilder()
					.setExclusionStrategies(new ExclusionStrategy() {
						public boolean shouldSkipClass(Class<?> clazz) {
							return (clazz==MaintenanceSchedule.class);
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
			String json = gson2.toJson(maints);
			System.out.println(json);
			return json;
		}
		catch (Exception e){
			return "cannot fetch";
		}
		//return new ResponseEntity<Void>(HttpStatus.OK);
	}


	@PreAuthorize("hasAnyAuthority('ROLE_PROPERTY')")
	// Call this method using $http.get and you will get a JSON format containing an array of eventobjects.
	// Each object (building) will contain... long id, .
	@RequestMapping(value = "/viewAllSelectedUnits/{id}",  method = RequestMethod.GET)
	@ResponseBody
	public  ResponseEntity<String> viewAllSelectedUnits(@PathVariable("id") String bId, HttpServletRequest rq) throws UserNotFoundException {
		System.out.println("start view");
		Principal principal = rq.getUserPrincipal();
		Optional<User> eventOrg1 = userService.getUserByEmail(principal.getName());
		if ( !eventOrg1.isPresent() ){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
		try{
			//EventOrganizer eventOrg = eventOrg1.get();	
			User eventOrg = eventOrg1.get();
			ClientOrganisation client = eventOrg.getClientOrganisation();
			System.out.println(eventOrg.getId());
			long id = Long.parseLong(bId);
			Set<MaintenanceSchedule> schedules=maintenanceService.getMaintenanceSchedule(client,id);
			System.out.println("There are " + schedules.size() + " schedules under this organizer");
			//Set<BookingAppl> bookingsWithUnits=new HashSet<BookingAppl>();
			//Gson gson = new Gson();
			//String json = gson.toJson(levels);
			//System.out.println("Returning levels with json of : " + json);
			//return json;
			Set<Unit> units = new HashSet<Unit>();
			for(MaintenanceSchedule schedule:schedules){									
				schedule.getUnit().setBookings(null);
				schedule.getUnit().setUnitAttributeValues(null);
				schedule.getUnit().setMaintenanceSchedule(null);
				schedule.getUnit().setSquare(null);
				schedule.getUnit().setLevel(null);
				units.add(schedule.getUnit());
				System.err.println(units.size());
				System.err.println("*******");
			}
			Gson gson = new Gson();
			String json = gson.toJson(units);
			return new ResponseEntity<String>(json, HttpStatus.OK);
		}
		catch (Exception e){
			return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}
	}


	@RequestMapping(value = "/downloadReport", method = RequestMethod.POST, produces = "application/pdf")
	@ResponseBody
	public void downloadReport(HttpServletRequest request,HttpServletResponse response) throws JRException, IOException, UserNotFoundException {
		System.out.println("Enter");
		InputStream jasperStream = request.getSession().getServletContext().getResourceAsStream("/jasper/maintenanceReport.jasper");
		response.setContentType("application/pdf");
		Principal principal = request.getUserPrincipal();
		Optional<User> usr = userService.getUserByEmail(principal.getName());
		ClientOrganisation client = usr.get().getClientOrganisation();
		response.setHeader("Content-disposition", "attachment; filename=maintenanceReport.pdf");
		ServletOutputStream outputStream = response.getOutputStream();
		HashMap<String,Object> parameters = new HashMap<String,Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		Object obj;
		Calendar cal = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
		//DateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date d1 = cal.getTime();
		String[] arr1 = String.valueOf(sdf.format(d1)).split(" ");
		//System.out.println("FIRST DAY IS "+ str1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date d2 = cal.getTime();
		String[] arr2 = String.valueOf(sdf.format(d2)).split(" ");
		//System.out.println("LAST DAY IS "+ str2);

		sb.append("WHERE (CLIENT = ");
		
		sb.append(client.getId() + "  AND COMMENCE >= '");
		sb.append(arr1[0] + " 00:00:00 ' AND COMMENCE <= '");

		
		sb.append(arr2[0] +  " 23:59:59 ')");
		
		System.err.println("Query parameter is : " + sb.toString());
		parameters.put("criteria", sb.toString());
		Connection conn = null;
		try {
			DataSource ds = (DataSource)context.getBean("dataSource");
			conn = ds.getConnection();
			System.out.println("ERROR: ");
			System.out.println(conn.toString());
		} catch (SQLException e) {
			System.out.println("************* ERROR: " + e.getMessage());
			e.printStackTrace();
		}
		JasperRunManager.runReportToPdfStream(jasperStream, outputStream, parameters,conn);
		outputStream.flush();
		outputStream.close();
		System.out.println("FLUSHED OUT THE LOG");

	}
}
