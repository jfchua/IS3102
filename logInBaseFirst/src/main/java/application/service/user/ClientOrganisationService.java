package application.service.user;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import application.domain.ClientOrganisation;

public interface ClientOrganisationService {

	 public boolean createNewClientOrganisation(String orgName, String adminEmail, List<String> subs,String nameAdmin);
	 
	 public Collection<ClientOrganisation>  getAllClientOrganisations();
	 
	 public ClientOrganisation getClientOrganisationByName(String name);
	 
	 public void deleteClientOrg(Long id);
	
}