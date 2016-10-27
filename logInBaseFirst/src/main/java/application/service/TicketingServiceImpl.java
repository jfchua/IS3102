package application.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SimpleTimeZone;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import application.entity.BookingAppl;
import application.entity.Building;
import application.entity.Category;
import application.entity.Event;
import application.entity.Level;
import application.entity.Unit;
import application.exception.EventNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.CategoryRepository;
import application.repository.EventRepository;
import application.repository.UserRepository;

@Service
public class TicketingServiceImpl implements TicketingService {

	private final UserRepository userRepository;
	private final EventService eventService;
	private final AuditLogRepository auditLogRepository;
	private final EventRepository eventRepository;
	private final CategoryRepository categoryRepository;

	@Autowired
	public TicketingServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository, EventService eventService,AuditLogRepository auditLogRepository, UserRepository userRepository) {
		super();
		this.auditLogRepository = auditLogRepository;
		this.userRepository = userRepository;
		this.eventService = eventService;
		this.eventRepository = eventRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public boolean addCategory(Long eventId, String catName, double price, int numTix) throws EventNotFoundException {
		Optional<Event> e = eventService.getEventById(eventId);
		try{

			Set<Category> temp = e.get().getCategories();
			Category cat = new Category();
			cat.setCategoryName(catName);
			cat.setEvent(e.get());
			cat.setNumOfTickets(numTix);
			cat.setPrice(price);
			temp.add(cat);
			eventRepository.save(e.get());
			return true;
		}
		catch ( Exception ex){
			System.err.println("add category error" + ex.getMessage());
		}

		return false;
	}

	@Override
	public Set<Category> getCategories(Long eventId) throws EventNotFoundException{
		Optional<Event> e = eventService.getEventById(eventId);
		return e.get().getCategories();
	}

	public boolean deleteCat(Long id){
		try{
			Category c = categoryRepository.findOne(id);
			Event e = c.getEvent();
			Set<Category> cats = e.getCategories();
			cats.remove(c);			
			e.setCategories(cats);
			categoryRepository.delete(c);
			eventRepository.save(e);

			categoryRepository.flush();
			return true;
		}
		catch (Exception e){
			return false;
		}

	}

	public String getEventDataAsJson(Long eventId) throws EventNotFoundException{
		Event e = eventService.getEventById(eventId).get();
		Gson json = new Gson();

		SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.setTimeZone(new SimpleTimeZone(0, "GMT+8"));
		sdf.applyPattern("dd MMM yyyy HH:mm:ss z");
		JSONObject bd = new JSONObject(); 
		bd.put("title", e.getEvent_title()); 
		bd.put("type", e.getEvent_type().toString()); 
		System.out.println(e.getEvent_start_date().toString());
		bd.put("startDate", sdf.format(e.getEvent_start_date())); 
		bd.put("endDate", sdf.format(e.getEvent_end_date())); 
		bd.put("description", e.getEvent_description());
		ArrayList<String> t = new ArrayList<String>();
		Set<BookingAppl> bookings = e.getBookings();
		for ( BookingAppl book : bookings){
			Unit u = book.getUnit();
			Level l = u.getLevel();
			Building b = l.getBuilding();
			String address = b.getName() + ", " + b.getAddress() + ", Level " + l.getLevelNum() + ", " + u.getUnitNumber();
			t.add(address);					
		}
		bd.put("address", t);

		/*
		Set<tempAddressObject> addresses = new HashSet<tempAddressObject>();
		Set<BookingAppl> bookings = e.getBookings();
		Set<Building> buildings = new HashSet<>();
		Set<Level> levels = new HashSet<>();
		Set<Unit> units = new HashSet<>();
		
		for ( BookingAppl book : bookings){
			buildings.add(book.getUnit().getLevel().getBuilding());
			levels.add(book.getUnit().getLevel());
			units.add(book.getUnit());
		}
		
				
		for ( BookingAppl book : bookings){
			Boolean addBuilding = true;
			Unit u = book.getUnit();
			Level l = u.getLevel();
			Building b = l.getBuilding();
			tempAddressObject tao = new tempAddressObject();
			tao.setBuilding(b);
			for ( tempAddressObject tempAdd : addresses){
				if ( tempAdd.getBuilding().equals(b)){
					addBuilding = false;
				}
			}
			if ( addBuilding ){
				addresses.add(t);
			}
			
			if ( for  )

		}*/
		

		//bd.put("roles", roles); 
		System.out.println("Returning event info : " + bd.toString());
		return bd.toString();
	}
	
/*	private class tempAddressObject{

		private Building building;
		private String levelNum;
		private ArrayList<String> units;

		public tempAddressObject(){

		}

		public Building getBuilding() {
			return building;
		}


		public void setBuilding(Building building) {
			this.building = building;
		}



		public String getLevelNum() {
			return levelNum;
		}

		public void setLevelNum(String levelNum) {
			this.levelNum = levelNum;
		}

		public ArrayList<String> getUnits() {
			return units;
		}

		public void setUnits(ArrayList<String> units) {
			this.units = units;
		}

		@Override
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(buildingName + " Level " + levelNum+ ", " );
			for ( String unit : units){
				sb.append(unit + ", ");
			}
			String toReturn = sb.toString();
			toReturn = toReturn.trim();
			toReturn = toReturn.substring(0, toReturn.length()-1);
			return toReturn;
		}

	}*/
}
