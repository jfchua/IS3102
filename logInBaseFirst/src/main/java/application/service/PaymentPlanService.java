package application.service;

import java.util.Optional;
import java.util.Set;

import application.entity.ClientOrganisation;
import application.entity.PaymentPlan;
import application.entity.User;

public interface PaymentPlanService {
	boolean createPaymentPlan(ClientOrganisation client, User user, long eventId, Double total, Double deposit, 
			int subsequentNumber);
	
	boolean updatePaymentPlan(ClientOrganisation client, User user, long paymentId, Double depositRate, 
			int subsequentNumber);

	
	Optional<PaymentPlan> getPaymentPlanById(long id);
	
	Set<PaymentPlan> viewAllPaymentPlan(ClientOrganisation client, User user);
	
	boolean updateAmountPaid(ClientOrganisation client, User user, long paymentId, Double paid);
	
	boolean checkEvent(ClientOrganisation client, long eventId);
	
	boolean checkPaymentPlan(ClientOrganisation client, long paymentPlanId);
	
	Double checkRent(ClientOrganisation client, long eventId);
	//boolean updateAmountPayable(ClientOrganisation client, long paymentId, Double paid);

}
