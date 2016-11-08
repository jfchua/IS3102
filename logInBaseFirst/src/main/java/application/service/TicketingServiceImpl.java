package application.service;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

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
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperRunManager;

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
	private ServletContext servletContext;
	@Autowired
	private ApplicationContext context;

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
			for ( Category c : temp){
				if ( c.getCategoryName().equalsIgnoreCase(catName)){
					return false;
				}
			}
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
			System.err.println("Start updating category");
			Category c = categoryRepository.findOne(catId);
			Event e = c.getEvent();
			Set<Category> ttt = c.getEvent().getCategories();
			for ( Category cx : ttt){
				System.err.println("Start updating category111");
				if ( cx.getCategoryName().equalsIgnoreCase(catName) && cx.getEvent().getId() == e.getId() && cx.getId() != catId){
					return false;
				}
			}
			System.err.println("Start updating category2222");
			c.setCategoryName(catName);
			c.setNumOfTickets(numTix);
			c.setPrice(price);
			categoryRepository.flush();
			
		}
		catch ( Exception ex){
			System.err.println("update category error" + ex.getMessage());
			return false;
		}

		return true;
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

	private class tempStorage{

		private String paymentId;
		private ArrayList<String> categories = new ArrayList<>();
		private ArrayList<Integer> numTix = new ArrayList<>();
		private Date purDate;


		public Date getPurDate() {
			return purDate;
		}
		public void setPurDate(Date purDate) {
			this.purDate = purDate;
		}
		public String getPaymentId() {
			return paymentId;
		}
		public void setPaymentId(String paymentId) {
			this.paymentId = paymentId;
		}
		public ArrayList<String> getCategories() {
			return categories;
		}
		public void setCategories(ArrayList<String> categories) {
			this.categories = categories;
		}
		public ArrayList<Integer> getNumTix() {
			return numTix;
		}
		public void setNumTix(ArrayList<Integer> numTix) {
			this.numTix = numTix;
		}
		public void addCat(String cat){
			this.categories.add(cat);
		}
		public void addTix(int idx){
			if ( idx == this.numTix.size()){
				this.numTix.add(1);
				return;
			}
			int current = this.numTix.get(idx);
			current++;
			this.numTix.set(idx, current);
		}

		@Override
		public String toString(){
			String x =  "Payment ID: " + this.paymentId + "\n" + "Purchased: " +  new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(this.purDate) + "\n";
			for ( int i = 0; i < this.categories.size() ; i++){
				x += this.getNumTix().get(i) + " " + this.getCategories().get(i) + "\n";
			}
			return x;
		}

	}

	public ArrayList<String> viewTransactionHistory(Long userId) throws UserNotFoundException{
		Optional<User> user = userService.getUserById(userId);
		try{

			Set<Ticket> tx = user.get().getTickets();
			ArrayList<String> newList = new ArrayList<String>();
			//Map<String,String> mapz =  new HashMap<>();
			ArrayList<tempStorage> temp  = new ArrayList<>();
			for ( Ticket tix: tx){
				System.err.println(tix.getTicketDetails());
				boolean found = false;
				String paymentId = tix.getPaymentId();
				if ( temp.size() == 0){
					tempStorage p = new tempStorage();
					p.addCat(tix.getPaymentId());
					p.addTix(0);
					p.setPaymentId(paymentId);
					p.setPurDate(tix.getPurchase_date());
					temp.add(p);	
					continue;
				}
				Iterator<tempStorage> iterator = temp.iterator();
				while(iterator.hasNext()){
					tempStorage t = iterator.next();
					System.err.println("inside line 330" + t.getPaymentId() + "  " + paymentId);
					//Already 
					if ( t.getPaymentId().equals(paymentId) ){
						System.err.println("insie line 333");
						found = true;
						boolean found2 = false;
						String catName = "";
						int counter = 0;
						for ( String cat : t.getCategories() ){
							System.err.println("insideline 339");
							catName = tix.getCategory().getCategoryName();
							if ( cat.equalsIgnoreCase(catName)){
								System.err.println("insideline 342");
								found2 = true;
								t.addTix(counter);													
							}
							System.err.println("insideline 346");
							counter++;

						}
						System.err.println("insideline 350");
						if ( !found2 ){
							System.err.println("insideline 352");
							t.addCat(catName);
							t.addTix(t.getCategories().size()-1);
						}
						//Not exists yet

					}

				}
				if ( !found ){
					System.err.println("inside not found!");
					tempStorage p = new tempStorage();
					p.addCat(tix.getPaymentId());
					p.addTix(0);
					p.setPaymentId(paymentId);
					p.setPurDate(tix.getPurchase_date());
					temp.add(p);			
				}

			}
			Collections.sort(temp, new Comparator<tempStorage>() {
				public int compare(tempStorage o1, tempStorage o2) {
					return o1.getPurDate().compareTo(o2.getPurDate());
				}});
			for ( tempStorage tempS : temp){
				newList.add(tempS.toString());
			}


			return newList;
		}
		catch(Exception exp){
			exp.printStackTrace();
		}
		return new ArrayList<String>();

	}

	public String generateTicket(User user, String paymentId, int numTickets, Long categoryId){
		String uuid = "";
		Category c = categoryRepository.findOne(categoryId);
		try{

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
				uuid =  toUuid.substring(0,10);		
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
			return null;
		}
		//return true;

		//Gen qr code image
		String myCodeText = uuid;
		String filePath = servletContext.getRealPath("/") + "QRCODETOOVERWRITE.png";
		System.err.println("FILE PATH IS " + filePath);
		int size = 250;
		String fileType = "png";
		File myFile = new File(filePath);
		try {

			Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

			// Now with zxing version 3.2.1 you could change border size (white border size to just 1)
			hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
					size, hintMap);
			int CrunchifyWidth = byteMatrix.getWidth();
			BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
					BufferedImage.TYPE_INT_RGB);
			image.createGraphics();

			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
			graphics.setColor(Color.BLACK);

			for (int i = 0; i < CrunchifyWidth; i++) {
				for (int j = 0; j < CrunchifyWidth; j++) {
					if (byteMatrix.get(i, j)) {
						graphics.fillRect(i, j, 1, 1);
					}
				}
			}
			ImageIO.write(image, fileType, myFile);
			//


			System.err.println("Enter");
			InputStream jasperStream = servletContext.getResourceAsStream("/jasper/TicketQR.jasper");



			HashMap<String,Object> parameters = new HashMap<String,Object>();
			String toPut = "Event name: " + c.getEvent().getEvent_title() +"\n"+ " 1 "+  c.getCategoryName() +" ticket" +  "\n" + "Payment ID: " + paymentId;
			System.out.println(toPut);
			parameters.put("ticketInformation",toPut );
			parameters.put("qrcode",filePath );

			Connection conn = null;
			try {
				DataSource ds = (DataSource)context.getBean("dataSource");

				conn = ds.getConnection();
				System.out.println(conn.toString());
			} catch (SQLException e) {
				System.out.println("************* ERROR: " + e.getMessage());
				e.printStackTrace();
			}
			try{
				String path = servletContext.getRealPath("/");
				path += (c.getCategoryName() + "_" + uuid + ".pdf");	
				System.err.println("path is " + path);

				FileOutputStream fileOutputStream = new FileOutputStream(path);
				JasperRunManager.runReportToPdfStream(jasperStream, fileOutputStream, parameters,new JREmptyDataSource());
				fileOutputStream.flush();
				fileOutputStream.close();
				System.out.println("FLUSHED OUT THE LOG");
				return path;
				//emailService.sendEmailWithAttachment("kenneth1399@hotmail.com", "Invoice for payment plan id ", "bodymessage" , path);

			}
			catch(Exception exp){
				exp.printStackTrace();
			}
		} catch (Exception e1) {
			System.out.println("at /download invoice there was an error parsing the json string received");
			e1.printStackTrace();
		}
		return null;
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
	
	public Ticket getTicketByCode(String code){
		return ticketRepository.getTicketByCode(code);
	}
	
}
