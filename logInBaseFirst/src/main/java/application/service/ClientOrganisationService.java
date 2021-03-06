package application.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import application.entity.ClientOrganisation;
import application.enumeration.Subscription;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OrganisationNameAlreadyExistsException;
import application.exception.UserNotFoundException;

public interface ClientOrganisationService {

	 public boolean createNewClientOrganisation(String orgName, String adminEmail, Set<Subscription> subs,String nameAdmin, Double fee, Date start, Date end, String address, String postal, String phone) 
			 throws EmailAlreadyExistsException, OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException;
	 
	 public Collection<ClientOrganisation>  getAllClientOrganisations();
	 
	 public ClientOrganisation getClientOrganisationByName(String name) throws ClientOrganisationNotFoundException;
	 
	 public boolean deleteClientOrg(Long id) throws ClientOrganisationNotFoundException;
	
}