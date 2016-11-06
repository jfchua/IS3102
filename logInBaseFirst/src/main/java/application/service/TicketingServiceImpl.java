package application.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import application.entity.BookingAppl;
import application.entity.Building;
import application.entity.Category;
import application.entity.Event;
import application.entity.Level;
import application.entity.Role;
import application.entity.Ticket;
import application.entity.Unit;
import application.entity.User;
import application.exception.EmailAlreadyExistsException;
import application.exception.EventNotFoundException;
import application.exception.InvalidEmailException;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.CategoryRepository;
import application.repository.EventRepository;
import application.repository.RoleRepository;
import application.repository.TicketRepository;
import application.repository.UserRepository;

@Service
public class TicketingServiceImpl implements TicketingService {

	private final UserRepository userRepository;
	private final EventService eventService;
	private final AuditLogRepository auditLogRepository;
	private final UserService userService;
	private final EventRepository eventRepository;
	private final CategoryRepository categoryRepository;
	private final TicketRepository ticketRepository;
	private final RoleRepository roleRepository;
	private final EmailService emailService;

	@Autowired
	public TicketingServiceImpl(EmailService emailService, RoleRepository roleRepository, UserService userService, TicketRepository ticketRepository, CategoryRepository categoryRepository, EventRepository eventRepository, EventService eventService,AuditLogRepository auditLogRepository, UserRepository userRepository) {
		super();
		this.auditLogRepository = auditLogRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.emailService = emailService;
		this.eventService = eventService;
		this.eventRepository = eventRepository;
		this.userService = userService;
		this.categoryRepository = categoryRepository;
		this.ticketRepository = ticketRepository;
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
	public boolean updateCategory(Long catId, String catName, double price, int numTix) throws EventNotFoundException {
		try{
			Category c = categoryRepository.findOne(catId);
			c.setCategoryName(catName);
			c.setNumOfTickets(numTix);
			c.setPrice(price);
			categoryRepository.save(c);
			return true;
		}
		catch ( Exception ex){
			System.err.println("update category error" + ex.getMessage());
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
		bd.put("filePath", e.getFilePath());
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
	
	public int checkTickets(int numTickets, Long categoryId){
		
		Category c = categoryRepository.findOne(categoryId);
		int maxNumTix = c.getNumOfTickets();
		int currentTixNum = c.getTickets().size();
		int availableTix = maxNumTix-currentTixNum;
		if ( numTickets> availableTix ){
			return availableTix; 
		}
		else{
			return -1;
		}
	}

	public boolean generateTicket(User user, String paymentId, int numTickets, Long categoryId){
		try{
			Category c = categoryRepository.findOne(categoryId);
			/*if ( c.getTickets() != null ){
				System.err.println("!=null");
				Set<Ticket> tickets = c.getTickets();
				if ( tickets.isEmpty() && ( (tickets.size() + numTickets) ) > c.getNumOfTickets()   ){
					return false;
				}
			}
			else{
				System.err.println("new hash set");
				Set<Ticket> tickets = new HashSet<Ticket>();
			}
			Set<Ticket> tickets = c.getTickets();*/

			for ( int i = 0; i < numTickets;i++){
				Ticket t = new Ticket();
				t.setCategory(c);
				t.setPaymentId(paymentId);
				t.setPurchase_date(new Date());
				t.setEnd_date(c.getEvent().getEvent_end_date());
				t.setStart_date(c.getEvent().getEvent_start_date());
				t.setTicketDetails(c.getEvent().getEvent_title() + " : " + c.getCategoryName());

				SecureRandom random = new SecureRandom();
				String toUuid = new BigInteger(130,random).toString(32);
				String uuid =  toUuid.substring(0,10);		
				t.setTicketUUID(uuid);
				System.err.println(t.getTicketUUID());

				//tickets.add(t);
				//c.setTickets(tickets);
				c.addTicket(t);
	
				Set<Ticket> ticketsUser = user.getTickets();
	
				ticketsUser.add(t);
		
				user.setTickets(ticketsUser);
				ticketRepository.save(t);
		

				//categoryRepository.save(c);
				//userRepository.save(user);
			}
		}
		catch ( Exception e){
			System.err.println("Error at tix generation");
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public boolean registerNewUser(String name, String userEmail, String password) throws EmailAlreadyExistsException, UserNotFoundException, InvalidEmailException{
		Pattern pat = Pattern.compile("^.+@.+\\..+$");
		Matcher get = pat.matcher(userEmail);		
		if(!get.matches()){
			System.err.println("Invalid email exception");
			throw new InvalidEmailException("The email " + userEmail + " is invalid");
			//return false;
		}
		try{
			if ( userService.getUserByEmail(userEmail).isPresent() ){
				System.err.println("User already exists!");
				throw new EmailAlreadyExistsException("User with email " + userEmail + " already exists");
			}
		}
		catch ( UserNotFoundException e ){			
		}
		try{
			//CREATE USER START
			User user = new User();
			user.setName(name);
			Role r = roleRepository.getRoleByName("ROLE_EVEGOER");
			Set<Role> roles = new HashSet<Role>();
			roles.add(r);
			user.setRoles(roles);
			user.setEmail(userEmail);
			user.setClientOrganisation(null);
			user.setSecurity("");

			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String hashedPassword = encoder.encode(password);
			user.setPasswordHash(hashedPassword); //add salt?
			//Send created password to the new user's email

			//REPOSITORY SAVING

			userRepository.save(user);
			System.out.println("Saved user");
			emailService.sendEmail(userEmail, "Algattas account signup", "Hi " + name + "! Thank you for signing up for a new account. You may now log in with your new account.");

		}
		catch ( Exception e){
			System.err.println("Exception at register new user "  + e.toString());
			return false;
		}
		return true;


	}

	@Override
	public boolean redeemTicket(String qrCode) {
		Ticket t = ticketRepository.getTicketByCode(qrCode);
		if ( t == null){
			return false;
		}
		else{
			try{
				if ( t.isRedeemed() ){
					return false;
				}
				t.setRedeemed(true);
				ticketRepository.save(t);
				return true;
			}
			catch(Exception e){
				System.err.println("Redeem ticket error " + e.getMessage());
			}
		}
		return false;

	}
}
