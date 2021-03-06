package application.service;

import java.util.Optional;
import java.util.Set;

import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Payment;
import application.entity.PaymentPlan;
import application.entity.User;
import application.exception.UserNotFoundException;

public interface PaymentPlanService {
	boolean createPaymentPlan(ClientOrganisation client, User user, long eventId, Double total, Double deposit, 
			int subsequentNumber);
	
	boolean updatePaymentPlan(ClientOrganisation client, User user, long paymentId, Double depositRate, 
			int subsequentNumber);

	
	Optional<PaymentPlan> getPaymentPlanById(long id);
	
	Set<PaymentPlan> viewAllPaymentPlan(ClientOrganisation client, User user);
	
	boolean updateAmountPaidByOrg(ClientOrganisation client, User user, long paymentPlanId, String chequeNum,
			Double paid, String nextInvoice);
	
	boolean checkEvent(ClientOrganisation client, long eventId);
	
	boolean checkPaymentPlan(ClientOrganisation client, long paymentPlanId);
	
	Double checkRent(ClientOrganisation client, long eventId);
	//boolean updateAmountPayable(ClientOrganisation client, long paymentId, Double paid);
	String getOutstandingById(long userId);
	
	Set<Event> getEventsByOrgId(ClientOrganisation client, long id);

	PaymentPlan getPaymentPlanByEvent(ClientOrganisation client, long id);
	
	boolean updateTicketRevenue(ClientOrganisation client, User user, long paymentPlanId, Double paid);
	
	boolean updateOutgoingPayment(ClientOrganisation client, User user, long paymentPlanId, Double paid, String cheque);
	
	Set<Payment> getPaymentsByOrgId(ClientOrganisation client, long id);
	
	void alertForOverduePayment() throws UserNotFoundException;

	void deleteOldPdfs();
	
	boolean generatePayment(ClientOrganisation client, long id, String invoice);
	
	boolean updatePayment(ClientOrganisation client, long id, String invoice);
}
