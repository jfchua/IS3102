package application.service.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.domain.PasswordResetToken;
import application.domain.Role;
import application.domain.User;
import application.exception.EmailAlreadyExistsException;
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

	boolean createNewUser(ClientOrganisation clientOrg,String name, String userEmail, Set<Role> roles) throws UserNotFoundException, EmailAlreadyExistsException;

	boolean editUser(String name, User user, Set<Role> roles) throws UserNotFoundException;

	Collection<User> viewAllUsers(ClientOrganisation clientOrg);

	public boolean deleteUser(User us) throws UserNotFoundException;

	Set<User> getExternalUsers(ClientOrganisation clientOrg);

	Set<User> getFinanceManagers(ClientOrganisation clientOrg);

	Set<User> getTicketManagers(ClientOrganisation clientOrg);

	void checkOldPassword(Long id, String oldpass) throws OldPasswordInvalidException, UserNotFoundException;

}
