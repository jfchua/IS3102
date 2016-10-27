package application.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import application.entity.ClientOrganisation;
import application.entity.PasswordResetToken;
import application.entity.Role;
import application.entity.User;
import application.exception.EmailAlreadyExistsException;
import application.exception.InvalidEmailException;
import application.exception.OldPasswordInvalidException;
import application.exception.PasswordResetTokenNotFoundException;
import application.exception.UserNotFoundException;

public interface UserService {

	Optional<User> getUserById(long id) throws UserNotFoundException;

	Optional<User> getUserByEmail(String email) throws UserNotFoundException;

	Collection<User> getAllUsers();

	// User create(UserCreateForm form);

	PasswordResetToken getPasswordResetToken(String token) throws PasswordResetTokenNotFoundException;

	boolean createPasswordResetTokenForUser(User user, String token) throws UserNotFoundException;

	boolean changePassword(long id, String password) throws UserNotFoundException;

	boolean createNewUser(ClientOrganisation clientOrg,String name, String userEmail, Set<Role> roles) throws UserNotFoundException, EmailAlreadyExistsException, InvalidEmailException;

	boolean editUser(String name, User user, Set<Role> roles) throws UserNotFoundException;

	Collection<User> viewAllUsers(ClientOrganisation clientOrg);

	public boolean deleteUser(User us) throws UserNotFoundException;

	Set<User> getExternalUsers(ClientOrganisation clientOrg);

	Set<User> getFinanceManagers(ClientOrganisation clientOrg);

	Set<User> getTicketManagers(ClientOrganisation clientOrg);

	boolean checkOldPassword(Long id, String oldpass) throws OldPasswordInvalidException, UserNotFoundException;
	
	boolean registerNewUser(String name, String email, String pass) throws EmailAlreadyExistsException, UserNotFoundException, InvalidEmailException;

}
