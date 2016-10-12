package application.service.user;

import java.util.Optional;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.domain.PaymentPlan;
import application.domain.User;

public interface PaymentPlanService {
	boolean createPaymentPlan(ClientOrganisation client, User user, long eventId, Double total, Double deposit, 
			int subsequentNumber, Double subsequent);
	
	boolean updatePaymentPlan(ClientOrganisation client, User user, long paymentId, Double total, Double deposit, 
			int subsequentNumber, Double subsequent);
	
	boolean deletePaymentPlan(ClientOrganisation client, User user, long paymentId);
	
	Optional<PaymentPlan> getPaymentPlanById(long id);
	
	Set<PaymentPlan> viewAllPaymentPlan(ClientOrganisation client, User user);
	
	boolean updateAmountPaid(ClientOrganisation client, User user, long paymentId, Double paid);
	
	boolean checkEvent(ClientOrganisation client, long eventId);
	
	//boolean updateAmountPayable(ClientOrganisation client, long paymentId, Double paid);

}
