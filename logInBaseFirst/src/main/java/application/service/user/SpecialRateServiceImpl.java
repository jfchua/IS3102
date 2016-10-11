package application.service.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.domain.Building;
import application.domain.ClientOrganisation;
import application.domain.SpecialRate;
import application.repository.BuildingRepository;
import application.repository.ClientOrganisationRepository;
import application.repository.SpecialRateRepository;
@Service
public class SpecialRateServiceImpl implements SpecialRateService {

	private final ClientOrganisationRepository clientOrganisationRepository;
	private final SpecialRateRepository specialRateRepository;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	
	@Autowired
	public SpecialRateServiceImpl(SpecialRateRepository specialRateRepository, ClientOrganisationRepository clientOrganisationRepository) {
		//super();
		this.specialRateRepository = specialRateRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
	}
	@Override
	public boolean createSpecialRate(ClientOrganisation client, Double rate, String description, 
			String period){
		if(rate <= 0)
			return false;
		else{
		Set<SpecialRate> rates = client.getSpecialRates();
		SpecialRate newRate = new SpecialRate();
		newRate.setRate(rate);
		newRate.setDescription(description);
		//newRate.setDate(date);
		newRate.setPeriod(period);
		/*
		if(period.length()!=3 ||period.length()!=7 ){
	        newRate.setPeriod("specific");
			DateFormat sdf = new SimpleDateFormat("EE MMM dd yyyy HH:mm:ss");
			Date applicable_date = sdf.parse(period);
			newRate.setDate(applicable_date);
		}*/
		specialRateRepository.save(newRate);
		rates.add(newRate);
		clientOrganisationRepository.save(client);
		return true;
		}
	}

	@Override
	public boolean deleteSpecialRate(ClientOrganisation client, long id) {
		try{
			Optional<SpecialRate> rate1 = getSpecialRateById(id);
			Set<SpecialRate> rates = client.getSpecialRates();
			if(rate1.isPresent()&&rates.contains(rate1.get())){
				SpecialRate rate = rate1.get();	
				rates.remove(rate);
				clientOrganisationRepository.save(client);	
				specialRateRepository.delete(rate);
			}
			else
				return false;
			}catch(Exception e){
				return false;
			}
			return true;
	}

	@Override
	public boolean updateSpecialRate(ClientOrganisation client, long id, Double rate, String description, 
			String period) {
		try{
			Optional<SpecialRate> rate1 = getSpecialRateById(id);
			Set<SpecialRate> rates = client.getSpecialRates();
			if(rate1.isPresent()&&rates.contains(rate1.get())){
				SpecialRate rate2 = rate1.get();
			   //rate2.setDate(date);
			   rate2.setDescription(description);
			   rate2.setPeriod(period);
			   rate2.setRate(rate);
		       specialRateRepository.flush();
			}
			else
				return false;
			}catch (Exception e){
				return false;
			}
			return true;
	}

	@Override
	public Set<SpecialRate> viewSpecialRates(ClientOrganisation client) {
		// TODO Auto-generated method stub
		return client.getSpecialRates();
	}

	@Override
	public Optional<SpecialRate> getSpecialRateById(long id) {
		LOGGER.debug("Getting special rate={}", id);
		return Optional.ofNullable(specialRateRepository.findOne(id));
	}

}
