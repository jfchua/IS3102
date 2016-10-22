package application.service;

import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.ClientOrganisation;
import application.entity.PaymentPolicy;
import application.entity.SpecialRate;
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
	public boolean createPaymentPolicy(ClientOrganisation client, Double rate, int num) {
		if((rate <= 0)||num<0)
			return false;
		else{
			PaymentPolicy newPay = new PaymentPolicy();
			newPay.setDepositRate(rate);
			newPay.setSubsequentNumber(num);
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
	public boolean updatePaymentPolicy(ClientOrganisation client, long id, Double rate, int num) {
		try{
			Optional<PaymentPolicy> rate1 = getPaymentPolicy(id);
			PaymentPolicy payment = client.getPaymentPolicy();
			if(rate1.isPresent()&&((rate1.get()).equals(payment))){
				PaymentPolicy rate2 = rate1.get();
			   //rate2.setDate(date);
			   rate2.setDepositRate(rate);
			   rate2.setSubsequentNumber(num);
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
}
