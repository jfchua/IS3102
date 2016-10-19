package application.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import application.entity.CurrentUser;
import application.entity.User;
import application.exception.UserNotFoundException;

//Where is the method used?


@Service
public class CurrentUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);
    private final UserService userService;

    @Autowired
    public CurrentUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        LOGGER.debug("Authenticating user with email={}", email.replaceFirst("@.*", "@***"));
        System.out.println("Authenticating user with email="+email);
        Optional<User> user = null;
		try {
			user = userService.getUserByEmail(email);
			        //.orElseThrow(() -> new UserNotFoundException(String.format("User with email=%s was not found", email)));
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			throw new UsernameNotFoundException("User of email " + email + " was not found");
		}
        System.out.println("Found user with email of : " + email);
        return new CurrentUser(user.get());
    }

}
