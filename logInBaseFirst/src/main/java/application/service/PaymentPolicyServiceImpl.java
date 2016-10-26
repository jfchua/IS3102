package application.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.ClientOrganisation;
import application.entity.PaymentPolicy;
import application.entity.Role;
import application.entity.Event;
import application.entity.PaymentPlan;
import application.entity.SpecialRate;
import application.entity.User;
import application.repository.ClientOrganisationRepository;
import application.repository.PaymentPolicyRepository;
@Service
public class PaymentPolicyServiceImpl implements PaymentPolicyService {

	private final ClientOrganisationRepository clientOrganisationRepository;
	private final PaymentPolicyRepository paymentPolicyRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	
	@Autowired
	public PaymentPolicyServiceImpl(PaymentPolicyRepository paymentPolicyRepository, ClientOrganisationRepository clientOrganisationRepository) {
		//super();
		this.paymentPolicyRepository= paymentPolicyRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
	}
	
	@Override
	public boolean createPaymentPolicy(ClientOrganisation client, Double rate, int num, int due, int period) {
		if((rate <= 0)||num<0)
			return false;
		else{
			PaymentPolicy newPay = new PaymentPolicy();
			newPay.setDepositRate(rate);
			newPay.setSubsequentNumber(num);
			newPay.setNumOfDueDays(due);
			newPay.setInterimPeriod(period);
			paymentPolicyRepository.save(newPay);
			client.setPaymentPolicy(newPay);
			clientOrganisationRepository.save(client);
			return true;
		}
	}

	@Override
	public boolean deletePaymentPolicy(ClientOrganisation client, long id) {
		try{
			Optional<PaymentPolicy> rate1 = getPaymentPolicy(id);
			PaymentPolicy payment = client.getPaymentPolicy();
			if(rate1.isPresent()&&((rate1.get()).equals(payment))){
				client.setPaymentPolicy(null);
				clientOrganisationRepository.save(client);	
				paymentPolicyRepository.delete(rate1.get());
			}
			else
				return false;
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public boolean updatePaymentPolicy(ClientOrganisation client, long id, Double rate, int num, int due, int period) {
		try{
			Optional<PaymentPolicy> rate1 = getPaymentPolicy(id);
			PaymentPolicy payment = client.getPaymentPolicy();
			if(rate1.isPresent()&&((rate1.get()).equals(payment))){
				PaymentPolicy rate2 = rate1.get();
			   //rate2.setDate(date);
			   rate2.setDepositRate(rate);
			   rate2.setSubsequentNumber(num);
			   rate2.setNumOfDueDays(due);
			   rate2.setInterimPeriod(period);
		       paymentPolicyRepository.flush();
			}
			else
				return false;
			}catch (Exception e){
				return false;
			}
			return true;
	}

	@Override
	public Optional<PaymentPolicy> getPaymentPolicy(long id) {
		LOGGER.debug("Getting special rate={}", id);
		return Optional.ofNullable(paymentPolicyRepository.findOne(id));
	}

	@Override
	public Double calculateTurnover(ClientOrganisation client) {
		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		Double totalDe = 0.00;
		Double begin = 0.00;
		Double end = 0.00;
		
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date d1 = cal1.getTime();
		cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date d2 = cal1.getTime();
		
		Set<User> users = client.getUsers();
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r : roles){
				if(r.getName().equals("ROLE_EXTEVE")){
					Set<Event> events = u.getEvents();
					for(Event e : events){
						PaymentPlan pay = e.getPaymentPlan();
						cal.setTime(pay.getCreated());
						int month1 = cal.get(Calendar.MONTH);
						if(month== month1){
						totalDe += pay.getDeposit();	
						}
						if(DateUtils.isSameDay(d1, pay.getCreated()))
							begin += pay.getDeposit();
						else if(DateUtils.isSameDay(d2, pay.getCreated()))
							end += pay.getDeposit();
					}
				}
			}
		}
		return 2*totalDe/(begin+end);
	}
}
