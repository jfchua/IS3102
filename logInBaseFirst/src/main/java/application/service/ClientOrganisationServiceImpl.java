package application.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import application.entity.ClientOrganisation;
import application.entity.Role;
import application.entity.User;
import application.enumeration.Subscription;
import application.exception.ClientOrganisationNotFoundException;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OrganisationNameAlreadyExistsException;
import application.exception.UserNotFoundException;
import application.repository.ClientOrganisationRepository;
import application.repository.RoleRepository;
import application.repository.UserRepository;


@Service
public class ClientOrganisationServiceImpl implements ClientOrganisationService {


	private final UserRepository userRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final RoleRepository roleRepository;
	private final EmailService emailService;
	private final UserService userService;
	// private final MessageRepository messageRepository = null;

	@Autowired
	public ClientOrganisationServiceImpl(UserService userService, EmailService emailService, UserRepository userRepository,  ClientOrganisationRepository clientOrganisationRepository, RoleRepository roleRepository ) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.roleRepository = roleRepository;
		this.userService = userService;
	}


	public boolean createNewClientOrganisation(String orgName, String adminEmail, List<Subscription> subs,String nameAdmin, Double fee, Date start, Date end, String address, String postal, String phone)
			throws EmailAlreadyExistsException,OrganisationNameAlreadyExistsException, ClientOrganisationNotFoundException, UserNotFoundException, InvalidEmailException{
		Pattern pat = Pattern.compile("^.+@.+\\..+$");
		Matcher get = pat.matcher(adminEmail);		
		if(!get.matches()){
			throw new InvalidEmailException("The email " + adminEmail + " is invalid");
			//return false;
		}
		try{
			if ( userService.getUserByEmail(adminEmail).isPresent()) {
				System.err.println("EMAIL ALREADY EXISTS");
				throw new EmailAlreadyExistsException("Email: " + adminEmail + " already exists");				
			}
		}
		catch ( UserNotFoundException e){

		}
		try{
			ClientOrganisation cl = this.getClientOrganisationByName(orgName);
			if( cl != null ){
				throw new OrganisationNameAlreadyExistsException("Org name " + orgName + " already exists");
			}			
		}
		catch (ClientOrganisationNotFoundException e ){

		}

		try{
			//CREATE USER START

			User user = new User();
			Set<Role> temp = new HashSet<Role>();
			Role r = roleRepository.getRoleByName("ROLE_ADMIN");
			temp.add(r);
			user.setRoles(temp);
			user.setEmail(adminEmail);
			user.setName(nameAdmin);
			//user.setSecurity("5");
			//user.setsecurityQuestion("favourite number");
			SecureRandom random = new SecureRandom();
			String t = new BigInteger(130,random).toString(32);
			String password =  t.substring(0,10);		
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String hashedPassword = encoder.encode(password);
			user.setPasswordHash(hashedPassword); //add salt?

			//CREATE USER END

			//CREATE CLIENT ORG START
			ClientOrganisation clientOrg = new ClientOrganisation();
			clientOrg.setOrganisationName(orgName);
			clientOrg.setSystemSubscriptions(subs);
			clientOrg.setFee(fee);
			clientOrg.setStart_date(start);
			clientOrg.setEnd_date(end);
			clientOrg.setAddress(address);
			clientOrg.setPostal(postal);
			clientOrg.setPhone(phone);
			Set<User> tempSetOfUsers = new HashSet<User>();
			tempSetOfUsers.add(user);
			clientOrg.setUsers(tempSetOfUsers);
			//CREATE CLIENT ORG END

			user.setClientOrganisation(clientOrg);

			//REPOSITORY SAVING


			clientOrganisationRepository.save(clientOrg);
			userRepository.save(user);
			//Send created password to the new user's email
			emailService.sendEmail(adminEmail, "New IFMS account created", "Please log in using your email and this generated password: " + password);

		}
		catch ( Exception e){
			System.err.println("Exception at createNewClientOrganisation "  + e.toString());
			return false;
		}
		return true;


	}


	public Collection<ClientOrganisation> getAllClientOrganisations(){

		return clientOrganisationRepository.findAll();


	}

	public boolean deleteClientOrg(Long id) throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrganisationRepository.findOne(id);
		if ( clientOrg == null){
			throw new ClientOrganisationNotFoundException("Client organisation of id" + id + " was not found");
		}
		try{
			clientOrganisationRepository.delete(id);
		}
		catch ( Exception e){
			return false;
		}
		return true;

	}

	public ClientOrganisation getClientOrganisationByName(String name) throws ClientOrganisationNotFoundException{
		ClientOrganisation clientOrg = clientOrganisationRepository.getClientOrgByName(name);	
		if ( clientOrg == null){
			throw new ClientOrganisationNotFoundException("Client organisation " + name + " was not found");
		}
		return	clientOrg;

	}



}