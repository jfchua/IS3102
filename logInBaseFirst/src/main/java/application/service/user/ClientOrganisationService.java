package application.service.user;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OrganisationNameAlreadyExistsException;
import application.exception.UserNotFoundException;

public interface ClientOrganisationService {

	 public boolean createNewClientOrganisation(String orgName, String adminEmail, List<String> subs,String nameAdmin) throws EmailAlreadyExistsException, OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException;
	 
	 public Collection<ClientOrganisation>  getAllClientOrganisations();
	 
	 public ClientOrganisation getClientOrganisationByName(String name) throws ClientOrganisationNotFoundException;
	 
	 public boolean deleteClientOrg(Long id) throws ClientOrganisationNotFoundException;
	
}