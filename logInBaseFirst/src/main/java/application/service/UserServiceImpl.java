package application.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import application.entity.ClientOrganisation;
import application.entity.PasswordResetToken;
import application.entity.Role;
import application.entity.User;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OldPasswordInvalidException;
import application.exception.OldSecurityInvalidException;
import application.exception.PasswordResetTokenNotFoundException;
import application.exception.UserNotFoundException;
import application.repository.ClientOrganisationRepository;
import application.repository.PasswordResetTokenRepository;
import application.repository.RoleRepository;
import application.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
	private final UserRepository userRepository;
	private final ClientOrganisationRepository clientOrganisationRepository;
	private final EmailService emailService;
	private final PasswordResetTokenRepository passwordResetTokenRepository;
	private final RoleRepository roleRepository; 


	// private final MessageRepository messageRepository = null;

	@Autowired
	public UserServiceImpl(RoleRepository roleRepository, ClientOrganisationRepository clientOrganisationRepository, EmailService emailService, UserRepository userRepository,PasswordResetTokenRepository passwordResetTokenRepository ) {
		this.userRepository = userRepository;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.emailService = emailService;
		this.clientOrganisationRepository = clientOrganisationRepository;
		this.roleRepository = roleRepository;
	}

	/* public UserServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }*/

	@Override
	public Optional<User> getUserById(long id) throws UserNotFoundException {
		LOGGER.debug("Getting user={}", id);
		Optional<User> us =  Optional.ofNullable(userRepository.findOne(id));
		if ( !us.isPresent() ){
			throw new UserNotFoundException("User with email " + id + " was not found" );
		}
		return us;
	}

	@Override
	public Optional<User> getUserByEmail(String email)  throws UserNotFoundException {
		LOGGER.debug("Getting user by email={}", email.replaceFirst("@.*", "@***"));
		Optional<User> us = userRepository.findOneByEmail(email);
		if ( !us.isPresent() ){
			throw new UserNotFoundException("User with email " + email + " was not found" );
		}
		return us;
	}

	@Override
	public boolean getUserBySecurity(User user, String security) {
		BCryptPasswordEncoder t = new BCryptPasswordEncoder();

		if (t.matches(security, user.getSecurity())) {
			return true;
		}
		return false;
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

	public PasswordResetToken getPasswordResetToken(String token) throws PasswordResetTokenNotFoundException{
		try{
			PasswordResetToken p = passwordResetTokenRepository.getTokenByString(token); 	
			if ( p == null ){
				throw new PasswordResetTokenNotFoundException("Invalid token!");
			}
			return p;
		}
		catch ( Exception e ){
			throw e;
		}


	}

	public boolean createPasswordResetTokenForUser(User user, String token) throws UserNotFoundException{
		if ( user == null ){
			throw new UserNotFoundException("User to create the reset token for was not found");
		}
		try{
			PasswordResetToken prt = new PasswordResetToken();
			prt.setUser(user);
			prt.setToken(token);
			prt.setExpiry();
			passwordResetTokenRepository.save(prt);		
			return true;
		}
		catch ( Exception e){
			throw e;
		}
	}

	public boolean changePassword(long id, String password) throws UserNotFoundException{


		try{
			Optional<User> user = getUserById(id);
			System.out.println("Setting user with ID" + user.get().getId() + "to password " + password);
			BCryptPasswordEncoder t = new BCryptPasswordEncoder();
			String newPassword = t.encode(password);
			user.get().setPasswordHash(newPassword);	
			System.out.println("Set user with new password of " + newPassword);
			userRepository.saveAndFlush(user.get());
			System.out.println("Set new password to " + user.get().getPasswordHash());

		}
		catch ( UserNotFoundException e){
			throw e;
		}
		catch(Exception e){
			System.err.println("EXCEPTION AT CHANGE PASSWORD" + e.getMessage());
		}


		System.out.println("LINE 90 AT USER SERVICE IMPL");
		return true;

	}

	public boolean changeSecurity(long id, String security, String question) throws UserNotFoundException{


		try{
			Optional<User> user = getUserById(id);
			System.out.println("Setting user with ID" + user.get().getId() + "to security " + security);
			//Changing security answer
			BCryptPasswordEncoder t = new BCryptPasswordEncoder();
			String newSecurity = t.encode(security);
			user.get().setSecurity(newSecurity);	
			System.out.println("Set user with new security answer of " + newSecurity);
			userRepository.saveAndFlush(user.get());
			System.out.println("Set new password to " + user.get().getPasswordHash());
			String securityQuestion = question;
			user.get().setSecurityQuestion(securityQuestion);
		}
		catch ( UserNotFoundException e){
			throw e;
		}
		catch(Exception e){
			System.err.println("EXCEPTION AT CHANGE PASSWORD" + e.getMessage());
		}


		System.out.println("LINE 90 AT USER SERVICE IMPL");
		return true;

	}
	
	public boolean createNewUser(ClientOrganisation clientOrg, String name, String userEmail, Set<Role> roles) throws EmailAlreadyExistsException, UserNotFoundException, InvalidEmailException{
		Pattern pat = Pattern.compile("^.+@.+\\..+$");
		Matcher get = pat.matcher(userEmail);		
		if(!get.matches()){
			throw new InvalidEmailException("The email " + userEmail + " is invalid");
			//return false;
		}
		try{
			if ( this.getUserByEmail(userEmail).isPresent() ){
				//User already exists
				throw new EmailAlreadyExistsException("User with email " + userEmail + " already exists");
			}
		}
		catch ( UserNotFoundException e ){			
		}
		for ( Role r : roles){
			System.err.println(r.getName());
		}
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
			emailService.sendEmail(userEmail, "New IFMS account created", "Please log in using your email and this generated password: " + password + " . Please remember to set your security question to have access to password retrieval services.");


		}
		catch ( Exception e){
			System.err.println("Exception at create new user "  + e.toString());
			return false;
		}
		return true;


	}



	@Override
	public boolean editUser(String name, User user, Set<Role> roles) throws UserNotFoundException {
		if ( user == null ){
			throw new UserNotFoundException("No user was found");
		}
		try{
			user.setName(name);
			userRepository.saveAndFlush(user);
			//System.out.println("GG");
			user.setRoles(roles);
			userRepository.saveAndFlush(user);

		}
		catch(Exception e){
			throw e;
		}
		return true;

	}


	@Override
	public Collection<User> viewAllUsers(ClientOrganisation clientOrg) {
		// TODO Auto-generated method stub
		return userRepository.getUsersByClientOrgId(clientOrg.getId());
	}

	@Override
	public boolean deleteUser(User us) throws UserNotFoundException{
		if ( us == null ){
			throw new UserNotFoundException("User does not exist");
		}
		try {
			userRepository.delete(us.getId());
		}
		catch ( Exception e){
			System.err.println("Error at deleting user"  + e.toString());
		}
		return true;

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

	@Override
	public boolean checkOldPassword(Long id, String oldpass) throws OldPasswordInvalidException, UserNotFoundException {

		Optional<User> user = this.getUserById(id);
		String oldPassUser = user.get().getPasswordHash();

		BCryptPasswordEncoder t = new BCryptPasswordEncoder();
		if ( !t.matches(oldpass, oldPassUser)){
			System.err.println("invalid old pass");
			throw new OldPasswordInvalidException("The old password entered was invalid");
		}
		return true;


	}

	@Override
	public boolean checkOldSecurity(Long id, String oldSec) throws OldSecurityInvalidException, UserNotFoundException {

		Optional<User> user = this.getUserById(id);
		String oldSecurity = user.get().getSecurity();

		BCryptPasswordEncoder t = new BCryptPasswordEncoder();
		if ( !t.matches(oldSec, oldSecurity)){
			System.err.println("invalid old security answer");
			throw new OldSecurityInvalidException("The old security answer entered was invalid");
		}
		return true;


	}


}