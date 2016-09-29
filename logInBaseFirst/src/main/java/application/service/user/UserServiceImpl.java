package application.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import application.domain.User;
import application.domain.UserCreateForm;
import application.domain.ClientOrganisation;
import application.domain.Message;
import application.domain.PasswordResetToken;
import application.domain.Role;
import application.domain.SendMessageForm;
import application.repository.ClientOrganisationRepository;
import application.repository.MessageRepository;
import application.repository.PasswordResetTokenRepository;
import application.repository.UserRepository;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final EmailService emailService;
	PasswordResetTokenRepository passwordResetTokenRepository;
	// private final MessageRepository messageRepository = null;

	@Autowired
	public UserServiceImpl(ClientOrganisationRepository clientOrganisationRepository, EmailService emailService, UserRepository userRepository,PasswordResetTokenRepository passwordResetTokenRepository ) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.emailService = emailService;
		this.clientOrganisationRepository = clientOrganisationRepository;
	}

	/* public UserServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }*/

	@Override
	public Optional<User> getUserById(long id) {
		LOGGER.debug("Getting user={}", id);
		return Optional.ofNullable(userRepository.findOne(id));
	}

	@Override
	public Optional<User> getUserByEmail(String email) {
		LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));
		return userRepository.findOneByEmail(email);
	}

	@Override
	public Collection<User> getAllUsers() {
		LOGGER.debug("Getting all users");
		return userRepository.findAll(new Sort("email"));
	}

	/*@Override
	public User create(UserCreateForm form) {
		User user = new User();
		user.setEmail(form.getEmail());
		user.setPasswordHash(new BCryptPasswordEncoder().encode(form.getPassword()));
		user.setRole(form.getRole());
		return userRepository.save(user);
	}*/

	public PasswordResetToken getPasswordResetToken(String token){

		return passwordResetTokenRepository.getTokenByString(token); 	

	}

	public void createPasswordResetTokenForUser(User user, String token){
		PasswordResetToken prt = new PasswordResetToken();
		prt.setUser(user);
		prt.setToken(token);
		prt.setExpiry();
	    passwordResetTokenRepository.save(prt);			
	}
	
	public void changePassword(long id, String password){
		
		Optional<User> user = getUserById(id);
		System.out.println("LINE 84 AT USER SERVICE IMPL");
		if (user.isPresent()){
			System.out.println("Setting user with ID" + user.get().getId());
			BCryptPasswordEncoder t = new BCryptPasswordEncoder();
			String newPassword = t.encode(password);
			user.get().setPasswordHash(newPassword);	
			userRepository.save(user.get());
		
		}
		System.out.println("LINE 90 AT USER SERVICE IMPL");
	
	}
	
	public boolean createNewUser(ClientOrganisation clientOrg, String name, String userEmail, Set<Role> roles){

		try{
			//CREATE USER START
			User user = new User();
			user.setName(name);
			user.setRoles(roles);
			user.setEmail(userEmail);
			//Create and set random password
			SecureRandom random = new SecureRandom();
			String t = new BigInteger(130,random).toString(32);
			String password =  t.substring(0,10);		
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String hashedPassword = encoder.encode(password);
			user.setPasswordHash(hashedPassword); //add salt?
			user.setClientOrganisation(clientOrg);
			
			//Send created password to the new user's email
			
			//CREATE USER END
			clientOrg.addUser(user);
		
			
			
			//REPOSITORY SAVING

			userRepository.save(user);
			clientOrganisationRepository.save(clientOrg);
			emailService.sendEmail(userEmail, "New IFMS account created", "Please log in using your email and this generated password: " + password);
			
			
		}
		catch ( Exception e){
			System.err.println("Exception at create new user "  + e.toString());
			return false;
		}
		return true;
		
		
	}
	
	

	@Override
	public void editUsers(ClientOrganisation clientOrg, String name, String userEmail, Set<Role> roles) {
		// TODO Auto-generated method stub
		
		try{
			Set<User> allUsers = (Set<User>) userRepository.findAll();
			//User user = findOneByE}
		}
		catch(Exception e){
			
		}
		
	}
	

	@Override
	public Collection<User> viewAllUsers(ClientOrganisation clientOrg) {
		// TODO Auto-generated method stub
		return userRepository.getUsersByClientOrgId(clientOrg.getId());
	}
	
	@Override
	public void deleteUser(User us){
		try {
			userRepository.delete(us.getId());
		
		}
		catch ( Exception e){
			System.err.println("Error at anything"  + e.toString());
		}
		
	}
	
	@Override
	 public Set<User> getExternalUsers(ClientOrganisation clientOrg) {
	  // TODO Auto-generated method stub
	  Set<User> allUsers = userRepository.getAllUsers(clientOrg);
	  Set<User> externalUsers = new HashSet<User>();
	  for(User u: allUsers){
	   Set<Role> roles = u.getRoles();
	   for(Role r: roles){
	    if(r.getName().equals("ROLE_EXTEVE"))
	     externalUsers.add(u);
	   }
	  }
	   
	  return externalUsers;
	 }

	 @Override
	 public Set<User> getFinanceManagers(ClientOrganisation clientOrg) {
	  // TODO Auto-generated method stub
	  Set<User> allUsers = userRepository.getAllUsers(clientOrg);
	  Set<User> financeManagers = new HashSet<User>();
	  for(User u: allUsers){
	   Set<Role> roles = u.getRoles();
	   for(Role r: roles){
	    if(r.getName().equals("ROLE_FINANCE"))
	     financeManagers.add(u);
	   }
	  }
	   
	  return financeManagers;
	 }

	 @Override
	 public Set<User> getTicketManagers(ClientOrganisation clientOrg) {
	  // TODO Auto-generated method stub
	  Set<User> allUsers = userRepository.getAllUsers(clientOrg);
	  Set<User> ticketManagers = new HashSet<User>();
	  for(User u: allUsers){
	   Set<Role> roles = u.getRoles();
	   for(Role r: roles){
	    if(r.getName().equals("ROLE_TICKETING"))
	     ticketManagers.add(u);
	   }
	  }  
	  return ticketManagers;
	 }
	 
}