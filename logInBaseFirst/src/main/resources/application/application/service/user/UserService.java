package application.service.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import application.domain.ClientOrganisation;
import application.domain.PasswordResetToken;
import application.domain.Role;
import application.domain.User;

public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

   // User create(UserCreateForm form);
    
    PasswordResetToken getPasswordResetToken(String token);
    
    void createPasswordResetTokenForUser(User user, String token);
    
    void changePassword(long id, String password);
    
    boolean createNewUser(ClientOrganisation clientOrg, String userEmail, Set<Role> roles);

}
	 	