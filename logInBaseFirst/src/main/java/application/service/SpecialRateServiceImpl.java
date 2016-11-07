package application.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.Event;
import application.entity.Role;
import application.entity.SpecialRate;
import application.entity.User;
import application.repository.BuildingRepository;
import application.repository.ClientOrganisationRepository;
import application.repository.EventRepository;
import application.repository.SpecialRateRepository;
@Service
public class SpecialRateServiceImpl implements SpecialRateService {

	private final ClientOrganisationRepository clientOrganisationRepository;
	private final SpecialRateRepository specialRateRepository;
	private final EventRepository eventRepository;
	 private final MessageService messageService;
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildingServiceImpl.class);
	
	@Autowired
	public SpecialRateServiceImpl(SpecialRateRepository specialRateRepository, ClientOrganisationRepository clientOrganisationRepository,
			EventRepository eventRepository, MessageService messageService) {
		//super();
		this.specialRateRepository = specialRateRepository;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.eventRepository = eventRepository;
		this.messageService = messageService;
	}
	@Override
	public boolean createSpecialRate(ClientOrganisation client, Double rate, String description, 
			String period){
		Set<SpecialRate> rates = client.getSpecialRates();
		for(SpecialRate r : rates){
			if(period.equals(r.getPeriod()))
				return false;
		}
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
			for(SpecialRate r : rates){
				if(period.equals(r.getPeriod()))
					return false;
			}
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
	@Override
	public void checkRate(ClientOrganisation client, long id) {
		// TODO Auto-generated method stub
		SpecialRate sp = Optional.ofNullable(specialRateRepository.findOne(id)).get();
		Set<User> users = client.getUsers();
		for(User u: users){
			Set<Role> roles = u.getRoles();
			for(Role r: roles){
				if(r.getName().equals("ROLE_EXTEVE")){
					Set<Event> events = u.getEvents();
					for(Event e : events){
						Date start = e.getEvent_start_date();
						Date end = e.getEvent_end_date();
						if(DateUtils.isSameDay(start, end)){
							if(checkPeriod(sp.getPeriod(), String.valueOf(start)) && e.getPaymentPlan() == null)
								messageService.sendMessage(sender, e.getEventOrg(), "Changes in your event payment", msg)
						}else{
						while(!DateUtils.isSameDay(start, end)){
							
						}	
						}
					}
				}				
			}
		}
	}
	@Override
	public boolean checkPeriod(String period, String date) {
		// TODO Auto-generated method stub
		return false;
	}

}
