package application.service;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

import antlr.debug.Event;
import application.entity.ClientOrganisation;
import application.entity.SpecialRate;

public interface SpecialRateService {
	
	boolean createSpecialRate(ClientOrganisation client, Double rate, String description, String period);

	boolean deleteSpecialRate(ClientOrganisation client, long id);
	
	boolean updateSpecialRate(ClientOrganisation client, long id, Double rate, String description, String period);
	
	Set<SpecialRate> viewSpecialRates(ClientOrganisation client);
	
	Optional<SpecialRate> getSpecialRateById(long id);
	
	boolean checkRate(ClientOrganisation client, long id) throws ParseException;
	
	boolean checkPeriod(String period, Date date) throws ParseException;
}
