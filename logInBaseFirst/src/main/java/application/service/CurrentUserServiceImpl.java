package application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import application.entity.CurrentUser;
import application.entity.Role;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentUserDetailsService.class);

   /* @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        LOGGER.debug("Checking if user={} has access to user={}", currentUser, userId);
        System.out.println("canaccess");
        System.out.println("checking usr" + currentUser.toString());
        System.out.println(currentUser.getRoles().toString());
        return currentUser != null
                && (currentUser.getRoles().contains("ADMIN") || currentUser.getId().equals(userId));
        //== Role.ADMIN
    }*/

}
