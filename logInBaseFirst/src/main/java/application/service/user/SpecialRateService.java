package application.service.user;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.domain.SpecialRate;

public interface SpecialRateService {
	
	boolean createSpecialRate(ClientOrganisation client, Double rate, String description, String period);

	boolean deleteSpecialRate(ClientOrganisation client, long id);
	
	boolean updateSpecialRate(ClientOrganisation client, long id, Double rate, String description, String period);
	
	Set<SpecialRate> viewSpecialRates(ClientOrganisation client);
	
	Optional<SpecialRate> getSpecialRateById(long id);
}
