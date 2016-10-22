package application.service;

import java.text.DateFormat;
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
import application.entity.PaymentPlan;
import application.entity.PaymentPolicy;
import application.entity.Role;
import application.entity.Unit;
import application.entity.User;
import application.repository.ClientOrganisationRepository;
import application.repository.EventRepository;
import application.repository.PaymentPlanRepository;
import application.repository.SpecialRateRepository;
import application.repository.UserRepository;
@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {

	private final ClientOrganisationRepository clientOrganisationRepository;
	private final PaymentPlanRepository paymentPlanRepository;
	private final EventRepository eventRepository;
	private final UserRepository userRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	
	@Autowired
	public PaymentPlanServiceImpl(PaymentPlanRepository paymentPlanRepository, ClientOrganisationRepository clientOrganisationRepository,
			EventRepository eventRepository, UserRepository userRepository) {
		//super();
		this.paymentPlanRepository = paymentPlanRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.eventRepository= eventRepository;
		this.userRepository = userRepository;
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
	    cal.add(Calendar.DATE, +3);
	    plan.setNotificationDue(cal.getTime());
	    cal.add(Calendar.DATE, +3);
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
	public boolean updatePaymentPlan(ClientOrganisation client, User user,long paymentId, Double total, Double deposit,
			int subsequentNumber, Double subsequent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePaymentPlan(ClientOrganisation client, User user,long paymentId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<PaymentPlan> getPaymentPlanById(long id) {
		// TODO Auto-generated method stub
		return null;
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
	public boolean updateAmountPaid(ClientOrganisation client, User user,long paymentId, Double paid) {
		// TODO Auto-generated method stub
		return false;
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

}
