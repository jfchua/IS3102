package application.service;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Payment;
import application.entity.PaymentPlan;
import application.entity.PaymentPolicy;
import application.entity.Role;
import application.entity.Unit;
import application.entity.User;
import application.enumeration.PaymentStatus;
import application.exception.UserNotFoundException;
import application.repository.ClientOrganisationRepository;
import application.repository.EventRepository;
import application.repository.PaymentPlanRepository;
import application.repository.PaymentRepository;
import application.repository.SpecialRateRepository;
import application.repository.UserRepository;
@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {
	@Autowired
	ServletContext servletContext;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final PaymentPlanRepository paymentPlanRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;
	private final MessageService messageService;
	private final EmailService emailService;
	private final EventExternalService eventExternalService;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);

	@Autowired
	public PaymentPlanServiceImpl(PaymentPlanRepository paymentPlanRepository, ClientOrganisationRepository clientOrganisationRepository,
			EventRepository eventRepository, UserRepository userRepository, PaymentRepository paymentRepository,
			MessageService messageService, EmailService emailService, EventExternalService eventExternalService) {
		//super();
		this.paymentPlanRepository = paymentPlanRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.eventRepository= eventRepository;
		this.userRepository = userRepository;
		this.paymentRepository = paymentRepository;
		this.messageService =messageService;
		this.emailService = emailService;
		this.eventExternalService =eventExternalService;
	}

	@Override
	public boolean createPaymentPlan(ClientOrganisation client, User user,long eventId, Double total, Double deposit,
			int subsequentNumber) {

		// does the user belong to client organization and does the user have role of "external event organizer"
		Set<User> users = userRepository.getAllUsers(client);
		PaymentPolicy payPol = client.getPaymentPolicy();
		int dueDays = (payPol.getNumOfDueDays())/2;
		boolean doesHave = false;
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r: roles){
				if(r.getName().equals("ROLE_FINANCE") && u.equals(user))
					doesHave = true;
			}
		}
		if((!doesHave)||(!checkEvent(client, eventId))||(subsequentNumber<0)||(total<0)||(deposit<0)||(deposit>total))
			return false;
		PaymentPlan plan = new PaymentPlan();
		plan.setTotal(total);
		NumberFormat formatter = new DecimalFormat("#0.00"); 
		plan.setGst(Double.valueOf(formatter.format(total/1.07*0.07)));
		plan.setTotalBeforeGst(Double.valueOf(formatter.format(total/1.07)));
		plan.setDepositRate(payPol.getDepositRate());
		plan.setOverdue(false);
		plan.setTicketRevenue(0.00);
		plan.setDeposit(deposit);
		plan.setNextPayment(deposit);
		plan.setSubsequentNumber(subsequentNumber);
		Double subsequent = 0.00;
		if((total > deposit)&&subsequentNumber>0)
			subsequent = (total-deposit)/subsequentNumber;
		else
			subsequent = 0.00;
		plan.setSubsequent(subsequent);
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//Date date = new Date();
		//plan.setCreated(date);
		Calendar cal = Calendar.getInstance();
		plan.setCreated(cal.getTime());
		cal.add(Calendar.DATE, +dueDays);
		plan.setNotificationDue(cal.getTime());
		cal.add(Calendar.DATE, +dueDays);
		plan.setDue(cal.getTime());
		//add due date and notification due date
		plan.setPaid(0.00);
		plan.setPayable(total);
		Event event;
		try{
			Optional<Event> event1 = Optional.ofNullable(eventRepository.findOne(eventId));
			if(event1.isPresent()){
				event = event1.get();
				plan.setEvent(event);
				User eventOrg = event.getEventOrg();
				plan.setOwner(eventOrg.getEmail());
				event.setPaymentPlan(plan);
			}
		}catch(Exception e){
			return false;
		}
		paymentPlanRepository.save(plan);
		eventRepository.flush();
		return true;
	}

	@Override
	public boolean updatePaymentPlan(ClientOrganisation client, User user, long paymentId, Double depositRate, 
			int subsequentNumber) {
		Set<User> users = userRepository.getAllUsers(client);
		System.out.println("clientUser");

		boolean doesHave = false;
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r: roles){
				if(r.getName().equals("ROLE_FINANCE") && u.equals(user))
					doesHave = true;
			}
		}
		if((!doesHave)||(!checkEvent(client, paymentId))||(subsequentNumber<0)||(depositRate<0)||(depositRate>1))
			return false;
		System.out.println("before try");
		try{
			Optional<PaymentPlan> pay1 = getPaymentPlanById(paymentId); 
			System.out.println(pay1.isPresent());
			if(pay1.isPresent()){
				PaymentPlan pay = pay1.get();
				Double total = pay.getTotal();
				//System.out.println(total);
				NumberFormat formatter = new DecimalFormat("#0.00"); 
				// plan.setGst(Double.valueOf(formatter.format(total/1.07*0.07)));
				pay.setDeposit(Double.valueOf(formatter.format(total*depositRate)));
				//System.out.println(formatter.format(total*depositRate));
				pay.setDepositRate(depositRate);
				pay.setSubsequentNumber(subsequentNumber);
				//System.out.println("after subsequent");
				pay.setSubsequent(Double.valueOf(formatter.format((total-total*depositRate)/subsequentNumber)));
				//System.out.println("finally!!!");
				paymentPlanRepository.flush();
				messageService.sendMessage(user, pay.getEvent().getEventOrg(), "Change of Payment Plan", "Your payment plan is changed, "
						+ "the latest deposit rate is " + depositRate +", the latest number of subsequent number of payments are "+subsequentNumber);
				return true;
			}
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Optional<PaymentPlan> getPaymentPlanById(long id) {
		LOGGER.debug("Getting payment plan={}", id);
		return Optional.ofNullable(paymentPlanRepository.findOne(id));
	}

	@Override
	public Set<PaymentPlan> viewAllPaymentPlan(ClientOrganisation client, User user) {
		Set<User> users = userRepository.getAllUsers(client);
		Set<PaymentPlan> payments = new HashSet<PaymentPlan>();
		boolean doesHave = false;
		try{
			for(User u:users){
				Set<Role> roles = u.getRoles();
				for(Role r: roles){
					if(r.getName().equals("ROLE_EXTEVE")){
						Set<Event> events = u.getEvents();
						for(Event e: events){
							payments.add(e.getPaymentPlan());
						}
					}
				}		   
				for(User user2: users){
					Set<Role> roles2 = user2.getRoles();
					for(Role r: roles){
						if(r.getName().equals("ROLE_FINANCE") && u.equals(user))
							doesHave = true;
					}
				}
			}
			if(!doesHave)
				return null;
		}catch(Exception e){
			return null;
		}
		return payments;
	}

	@Override
	public boolean updateAmountPaidByOrg(ClientOrganisation client, User user,long paymentPlanId,
			String chequeNum, Double paid) {
		Set<User> users = userRepository.getAllUsers(client);
		System.out.println("clientUser");
		NumberFormat formatter = new DecimalFormat("#0.00"); 
		PaymentPolicy payPol = client.getPaymentPolicy();
		int period = payPol.getInterimPeriod();
		int due = (payPol.getNumOfDueDays())/2;
		boolean doesHave = false;
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r: roles){
				if(r.getName().equals("ROLE_FINANCE") && u.equals(user))
					doesHave = true;
			}
		}
		if((!doesHave)||(!checkEvent(client, paymentPlanId)))
			return false;
		try{
			Optional<PaymentPlan> pay1 = getPaymentPlanById(paymentPlanId); 
			System.out.println(pay1.isPresent());
			if(pay1.isPresent()){
				PaymentPlan pay = pay1.get();
				Set<Payment> payments = pay.getPayments();
				Double payable = pay.getPayable();
				System.out.println("payable " + payable);
				Event event = pay.getEvent();
				Double subsequent = pay.getSubsequent();
				System.out.println("subsequent " + subsequent);
				Date previousDue = pay.getDue();
				System.out.println("before if");	    	
				if(payable < paid)
					return false;
				Payment payment = new Payment();
				Calendar cal = Calendar.getInstance();
				payment.setPaid(cal.getTime());
				//System.out.println(cal.getTime());
				//DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.");
				//String[] arr1 = String.valueOf(sdf.format(cal.getTime())).split(" ");
				//DateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				//System.out.println(sdf.parse(arr1[0]));
				//payment.setPaid(sdf.parse(arr1[0]));
				System.out.println("paid time " + cal.getTime());
				payment.setAmount(paid);
				payment.setCheque(chequeNum);
				payment.setPlan(paymentPlanId);
				paymentRepository.save(payment);
				payments.add(payment);
				Double alrPaid = pay.getPaid();
				pay.setPaid(Double.valueOf(formatter.format(paid+alrPaid)));
				Double total = pay.getTotal();
				System.out.println("total " + total);
				pay.setPayable(Double.valueOf(formatter.format(total-pay.getPaid())));
				if((total-pay.getPaid()) >= subsequent)
					pay.setNextPayment(subsequent);
				else if (((total-pay.getPaid())<subsequent)&&((total-pay.getPaid())>0))
					pay.setNextPayment(total-pay.getPaid());
				else if(pay.getTotal()==pay.getPaid())
					pay.setNextPayment(0.00);
				Calendar cal1 = Calendar.getInstance();
				cal1.setTime(previousDue);
				cal1.add(Calendar.DATE, period);
				System.out.println("period " + period);
				pay.setDue(cal1.getTime());
				System.out.println(cal1.getTime());
				cal1.add(Calendar.DATE, -due);
				System.out.println("due " + due);
				pay.setNotificationDue(cal1.getTime());
				System.out.println(cal1.getTime());
				if(payment.getPaid().before(pay.getDue()))
					event.setPaymentStatus(PaymentStatus.valueOf("PAID"));

				paymentPlanRepository.save(pay);
				return true;
			}
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean checkEvent(ClientOrganisation client, long eventId) {
		Set<User> eventOrgs = userRepository.getAllUsers(client);
		boolean doesHave = false;
		try{
			Optional<Event> event1 = Optional.ofNullable(eventRepository.findOne(eventId));
			if(event1.isPresent()){
				Event event = event1.get();
				for(User u: eventOrgs){
					Set<Role> roles = u.getRoles();
					for(Role r: roles){
						if(r.getName().equals("ROLE_EXTEVE") && u.getEvents().contains(event)){
							doesHave = true;
							break;
						}
					}
				}
			}
		}catch(Exception e){
			return false;
		}
		return doesHave;
	}

	@Override
	public Double checkRent(ClientOrganisation client, long eventId) {
		if(checkEvent(client, eventId)){
			Double rent = 0.00;
			try{
				Optional<Event> event1 = Optional.ofNullable(eventRepository.findOne(eventId));
				NumberFormat formatter = new DecimalFormat("#0.00"); 
				if(event1.isPresent()){
					Event event = event1.get();
					Date start = event.getEvent_start_date();
					Date end = event.getEvent_end_date();
					User user = event.getEventOrg();
					Set<BookingAppl> bookings = event.getBookings();
					String unitsId = "";
					for(BookingAppl b : bookings)
						unitsId+=b.getUnit().getId();
					rent = eventExternalService.checkRent(client, user, unitsId, start, end);
				}
				return rent*1.07;
			}catch(Exception e){
				return 0.00;
			}
		}
		else
			return 0.00;
	}

	@Override
	public boolean checkPaymentPlan(ClientOrganisation client, long paymentPlanId) {
		try{
			Optional<PaymentPlan> pay1 = getPaymentPlanById(paymentPlanId); 
			if(pay1.isPresent()){
				PaymentPlan pay = pay1.get();
				Event event = pay.getEvent();
				return checkEvent(client, event.getId());
			}	
			else 
				return false;
		}catch (Exception e){
			return false;
		}
	}

	@Override
	public Double getOutstandingById(long userId) {
		try{
			Optional<User> user1 = Optional.ofNullable(userRepository.findOne(userId)); 
			Double outstanding = 0.00;
			if(user1.isPresent()){
				User user = user1.get();
				System.out.println("user id is "+user.getId());
				Set<Event> events = user.getEvents();
				for(Event e : events){
					PaymentPlan pay = e.getPaymentPlan();
					System.out.println("payment plan total "+ pay.getTotal());
					outstanding += pay.getPayable();
				}
				System.out.println("outstanding");
				return outstanding;
			}	
			else 
				return 0.00;
		}catch (Exception e){
			return 0.00;
		}
	}

	@Override
	public Set<Event> getEventsByOrgId(ClientOrganisation client, long id) {
		try{
			Optional<User> user1 = Optional.ofNullable(userRepository.findOne(id)); 			
			if(user1.isPresent()&&(client.getUsers().contains(user1.get()))){
				User user = user1.get();
				System.out.println("user id is "+user.getId());
				System.out.println("outstanding");
				return user.getEvents();
			}   
			else
				return null;
		}catch (Exception e){
			return null;
		}
	}

	@Override
	public PaymentPlan getPaymentPlanByEvent(ClientOrganisation client, long id) {
		if(checkEvent(client, id)){
			try{
				Optional<Event> event1 = Optional.ofNullable(eventRepository.findOne(id)); 			
				return event1.get().getPaymentPlan();
			}catch (Exception e){
				return null;
			}	
		}
		else
			return null;
	}

	@Override
	public boolean updateTicketRevenue(ClientOrganisation client, User user, long paymentPlanId, Double paid) {
		Set<User> users = userRepository.getAllUsers(client);
		System.out.println("clientUser");
		PaymentPolicy payPol = client.getPaymentPolicy();
		int period = payPol.getInterimPeriod();
		int due = payPol.getNumOfDueDays();
		boolean doesHave = false;
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r: roles){
				if(r.getName().equals("ROLE_FINANCE") && u.equals(user))
					doesHave = true;
			}
		}
		if((!doesHave)||(!checkEvent(client, paymentPlanId)))
			return false;
		try{
			Optional<PaymentPlan> pay1 = getPaymentPlanById(paymentPlanId); 
			System.out.println(pay1.isPresent());
			if(pay1.isPresent()){
				PaymentPlan pay = pay1.get();
				pay.setTicketRevenue(paid);
				Double payable = pay.getPayable();
				pay.setPayable(payable-paid);
				paymentPlanRepository.flush();
				return true;
			}
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public boolean updateOutgoingPayment(ClientOrganisation client, User user, long paymentPlanId, Double paid) {
		Set<User> users = userRepository.getAllUsers(client);
		System.out.println("clientUser");
		PaymentPolicy payPol = client.getPaymentPolicy();
		int period = payPol.getInterimPeriod();
		int due = payPol.getNumOfDueDays();
		boolean doesHave = false;
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r: roles){
				if(r.getName().equals("ROLE_FINANCE") && u.equals(user))
					doesHave = true;
			}
		}
		if((!doesHave)||(!checkEvent(client, paymentPlanId)))
			return false;
		try{
			Optional<PaymentPlan> pay1 = getPaymentPlanById(paymentPlanId); 
			System.out.println(pay1.isPresent());
			if(pay1.isPresent()){
				PaymentPlan pay = pay1.get();
				Double payable = pay.getPayable();
				if((payable + paid)!= 0)
					return false;
				pay.setPayable(0.00);
				pay.setNextPayment(0.00);
				paymentPlanRepository.flush();
				return true;
			}
			else
				return false;
		}catch(Exception e){
			return false;
		}
	}

	@Override
	public Set<Payment> getPaymentsByOrgId(ClientOrganisation client, long id) {
		try{
			Optional<User> user1 = Optional.ofNullable(userRepository.findOne(id)); 			
			if(user1.isPresent()&&(client.getUsers().contains(user1.get()))){
				Set<Payment> payments = new HashSet<Payment>();
				User user = user1.get();
				System.out.println("user id is "+user.getId());
				System.out.println("outstanding");
				Set<Event> events = user.getEvents();
				for(Event e : events){
					Set<Payment> pays = e.getPaymentPlan().getPayments();
					for(Payment p : pays)
						payments.add(p);
				}
				return payments;
			}   
			else
				return null;
		}catch (Exception e){
			return null;
		}
	}

	@Override
	//@Scheduled(fixedRate = 60000)
	@Scheduled(fixedRate = 86400000)
	public void alertForOverduePayment() throws UserNotFoundException {
		// TODO Auto-generated method stub
		//SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		//LOGGER.info("The time is now {}", dateFormat.format(new Date()));
		System.out.println("START*****");
		Set<PaymentPlan> pays = paymentPlanRepository.getAll();
		for(PaymentPlan p : pays){
			Date due = p.getDue();
			Calendar cal = Calendar.getInstance();
			Date notiDue = p.getNotificationDue();
			System.out.println("******NEXT PAYMENT *******" + p.getNextPayment());
			System.out.println("******DEPOSIT *******" + p.getDeposit());
			System.out.println("******SUBSEQUENT *******" + p.getSubsequent());
			//overdue deposit payment 
			if(DateUtils.isSameDay(cal.getTime(), due)){
				ClientOrganisation client = p.getEvent().getEventOrg().getClientOrganisation();
				p.setOverdue(true);
				paymentPlanRepository.save(p);
				System.out.println("SAVE OVERDUE PAYMENT PLAN");
				if((p.getNextPayment().equals(p.getDeposit()))){			    
					Event event = p.getEvent();
					event.setPaymentStatus(PaymentStatus.valueOf("LATE"));
					eventRepository.save(event);
					System.out.println("DEPOSIT OVERDUE?"+p.getOverdue());
					Set<User> users = client.getUsers();
					Set<User> finance = new HashSet<User>();
					Set<User> eventU = new HashSet<User>();
					User admin = null;
					System.out.println("HI");
					for(User u : users){
						Set<Role> roles = u.getRoles();
						for(Role r : roles){
							if(r.getName().equals("ROLE_FINANCE"))
								finance.add(u);

							else if(r.getName().equals("ROLE_EVENT"))
								eventU.add(u);

							else if(r.getName().equals("ROLE_ADMIN"))
								admin = u;
						}
					}
					System.out.println("******HI*******");
					for(User f: finance){
						messageService.sendMessage(admin, f, "Finance-Overdue Deposit Payment", "Overdue deposit payment plan ID is " +p.getId());
					}
					for(User e : eventU){
						messageService.sendMessage(admin, e, "Event-Overdue Deposit Payment", "Overdue deposit payment plan ID is " +p.getId());
					}
					emailService.sendEmail(p.getOwner(), "Overdue Deposit Payment", "You have an overdue deposit payment plan with ID " +p.getId()+" of "+p.getDeposit()+" SGD.");
				}
				//for overdue subsequent payment
				//else if(p.getNextPayment().equals(p.getSubsequent())){
				else{
					System.out.println("OVERDUE?"+p.getOverdue());
					Set<User> users = client.getUsers();
					Set<User> finance = new HashSet<User>();
					User admin = null;
					System.out.println("HI");
					for(User u : users){
						Set<Role> roles = u.getRoles();
						for(Role r : roles){
							if(r.getName().equals("ROLE_FINANCE"))
								finance.add(u);
							else if(r.getName().equals("ROLE_ADMIN"))
								admin = u;
						}
					}					
					for(User f : finance){
						messageService.sendMessage(admin, f, "Finance-Overdue Payment", "Overdue payment plan ID is " +p.getId());
					}
					messageService.sendMessage(admin, p.getEvent().getEventOrg(), "Event Organiser-Overdue Payment", "You have an overdue payment plan of ID " +p.getId());
					System.out.println("******HI*******FINISH****");
				}
			}	
			if(DateUtils.isSameDay(cal.getTime(), notiDue)){
				ClientOrganisation client = p.getEvent().getEventOrg().getClientOrganisation();
				System.out.println("NOTI DUE?"+p.getOverdue());
				Set<User> users = client.getUsers();
				User finance = null;
				System.out.println("HI");
				for(User u : users){
					Set<Role> roles = u.getRoles();
					for(Role r : roles){
						if(r.getName().equals("ROLE_FINANCE")){
							finance = u;
							break;
						}
					}
				}
				System.out.println("******SEND NOTI*******");
				messageService.sendMessage(finance, p.getEvent().getEventOrg(), "Reminder for due payment", "You have an upcoming payment within " 
						+client.getPaymentPolicy().getNumOfDueDays() +" of amount "+ p.getNextPayment());
			}
		}
	}

	@Override
	@Scheduled(fixedRate = 20000)
	public void deleteOldPdfs() {
		File dir = new File(servletContext.getRealPath("/"));
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				int i = child.getName().lastIndexOf('.');
				if (i > 0) {
					String extension = child.getName().substring(i+1);
					if ( extension.equals("pdf")){
						long diff = new Date().getTime() - child.lastModified();
						if (diff > 24 * 60 * 60 * 1000) {
							System.err.println("DELETING OLD FILES: " + child.getName());
							child.delete();
						}
					}
				}
				//if(file.delete()){
				//		System.out.println(file.getName() + " is deleted!");
				//}else{
				//	System.out.println("Delete operation is failed.");
				//}
			}
		} else {
			System.err.println("not a dir");
		}

	}

	@Override
	public boolean generatePayment(ClientOrganisation client, long id, String invoice) {
		System.out.println("BEFORE TRY");
		try{
			System.out.println("AFTER TRY");
			Optional<PaymentPlan> pay1 = Optional.ofNullable(paymentPlanRepository.findOne(id)); 
			System.out.println("BEFORE IF "+ pay1.isPresent());
			if(pay1.isPresent()){
				System.out.println("INSIDE IF");
				PaymentPlan pay = pay1.get();
				Set<Payment> payments = pay.getPayments();
				Payment p = new Payment();
				p.setInvoice(invoice);
				p.setPlan(id);
				paymentRepository.save(p);
				System.out.println("SAVE PAYMENT SUCCESS");
				payments.add(p);
				pay.setNextInvoice(invoice);
				paymentPlanRepository.flush();
				return true;
			}
			else{
				System.out.println("SAVE PAYMENT ERROR");
				return false;
			}
		}catch (Exception e){
			return false;
		}
	}
}
