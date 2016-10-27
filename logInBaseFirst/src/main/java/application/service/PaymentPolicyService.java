package application.service;

import java.util.Optional;

import application.entity.ClientOrganisation;
import application.entity.PaymentPolicy;

public interface PaymentPolicyService {
	boolean createPaymentPolicy(ClientOrganisation client, Double rate, int num, int due, int period);

	boolean deletePaymentPolicy(ClientOrganisation client, long id);
	
	boolean updatePaymentPolicy(ClientOrganisation client, long id, Double rate, int num, int due, int period);
	
	Optional<PaymentPolicy> getPaymentPolicy(long id);
	
	Double calculateTurnover(ClientOrganisation client);
}
