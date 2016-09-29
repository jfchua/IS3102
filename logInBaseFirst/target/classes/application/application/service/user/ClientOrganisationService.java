package application.service.user;

import java.util.Collection;

import application.domain.ClientOrganisation;

public interface ClientOrganisationService {

	 public boolean createNewClientOrganisation(String orgName, String adminEmail);
	 
	 public Collection<ClientOrganisation>  getAllClientOrganisations();
	
}
