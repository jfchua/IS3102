package application.service;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import application.repository.ClientOrganisationRepository;
import application.repository.EventRepository;
import application.repository.PaymentPlanRepository;
import application.repository.PaymentRepository;
import application.repository.SpecialRateRepository;
import application.repository.UserRepository;
@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {

	private final ClientOrganisationRepository clientOrganisationRepository;
	private final PaymentPlanRepository paymentPlanRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private final PaymentRepository paymentRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	
	@Autowired
	public PaymentPlanServiceImpl(PaymentPlanRepository paymentPlanRepository, ClientOrganisationRepository clientOrganisationRepository,
			EventRepository eventRepository, UserRepository userRepository, PaymentRepository paymentRepository) {
		//super();
		this.paymentPlanRepository = paymentPlanRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.eventRepository= eventRepository;
		this.userRepository = userRepository;
		this.paymentRepository = paymentRepository;
	}
	
	@Override
	public boolean createPaymentPlan(ClientOrganisation client, User user,long eventId, Double total, Double deposit,
			int subsequentNumber) {
        		
		// does the user belong to client organization and does the user have role of "external event organizer"
		Set<User> users = userRepository.getAllUsers(client);
		PaymentPolicy payPol = client.getPaymentPolicy();
		int dueDays = payPol.getNumOfDueDays();
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
	    plan.setTotalBeforeGst(total/1.07);
	    plan.setDepositRate(payPol.getDepositRate());
	    plan.setOverdue(false);
	    plan.setTicketRevenue(0.00);
	    plan.setDeposit(deposit);
	    plan.setSubsequentNumber(subsequentNumber);
	    Double subsequent = 0.00;
	    if((total > deposit)&&subsequentNumber>0)
	    	subsequent = (total-deposit)/subsequentNumber;
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
		    	Set<Payment> payments = pay.getPayments();
		    	Double payable = pay.getPayable();
		    	Event event = pay.getEvent();
		    	Double subsequent = pay.getSubsequent();
		    	Double deposit = pay.getDeposit();
		    	Date previousDue = pay.getNotificationDue();
		    		    	
		    	if((payable < paid)||(!((deposit == paid)||(subsequent == paid))))
		    		return false;
		    	Payment payment = new Payment();
		    	Calendar cal = Calendar.getInstance();
			    payment.setPaid(cal.getTime());
		    	payment.setAmount(paid);
		    	payment.setCheque(chequeNum);
		    	payment.setPlan(paymentPlanId);
		    	paymentRepository.save(payment);
		    	payments.add(payment);
		    	pay.setPaid(paid);
		    	Double total = pay.getTotal();
		    	pay.setPayable(total-paid);
		    	
		    	Calendar cal1 = Calendar.getInstance();
		        cal1.setTime(previousDue);
		        cal1.add(Calendar.DATE, period);		        
		    	pay.setNotificationDue(cal1.getTime());
		    	cal1.add(Calendar.DATE, due);
		    	pay.setDue(cal1.getTime());
		        if(payment.getPaid().before(pay.getDue()))
		        	event.setPaymentStatus(PaymentStatus.valueOf("PAID"));
		       /* else
		        	pay.setOverdue(true);*/
		        
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
				if(event1.isPresent()){
				Event event = event1.get();
				Date start = event.getEvent_start_date();
				Date end = event.getEvent_end_date();
				long diff = end.getTime() - start.getTime();
				long duration = TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
				Double duration1 = Double.valueOf(duration);
			    Set<BookingAppl> bookings = event.getBookings();
			    for(BookingAppl b : bookings){
			    	Unit u = b.getUnit();
			    	Double rentU = u.getRent();
			    	rent += rentU*duration1;
			    }
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

}
