package application.service.user;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import application.domain.ClientOrganisation;
import application.domain.Role;
import application.domain.User;
import application.repository.ClientOrganisationRepository;
import application.repository.RoleRepository;
import application.repository.UserRepository;


@Service
public class ClientOrganisationServiceImpl implements ClientOrganisationService {


	private final UserRepository userRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final RoleRepository roleRepository;
	private final EmailService emailService;
	// private final MessageRepository messageRepository = null;

	@Autowired
	public ClientOrganisationServiceImpl(EmailService emailService, UserRepository userRepository,  ClientOrganisationRepository clientOrganisationRepository, RoleRepository roleRepository ) {
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.clientOrganisationRepository =   clientOrganisationRepository;
		this.roleRepository = roleRepository;
	}


	public boolean createNewClientOrganisation(String orgName, String adminEmail, List<String> subs,String nameAdmin){
		try{
			//CREATE USER START
			User user = new User();
			Set<Role> temp = new HashSet<Role>();
			Role r = roleRepository.getRoleByName("ROLE_ADMIN");
			temp.add(r);
			user.setRoles(temp);
			user.setEmail(adminEmail);
			user.setName(nameAdmin);
			//Create and set random password
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

	public void deleteClientOrg(Long id){

		clientOrganisationRepository.delete(id);

	}
	
	public ClientOrganisation getClientOrganisationByName(String name){
		return clientOrganisationRepository.getClientOrgByName(name);		
	}



}