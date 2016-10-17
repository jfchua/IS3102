package application.entity;

import java.util.Collection;

import org.springframework.security.core.authority.AuthorityUtils;


//LogIn
//1. Enter details - email and password
//2. LogInController - which start to receive error if there is any for validation
//3. CurrentUser - extend Spring security -> go to User table to check
// It will look at the csrf parameter and value and check for errors
//4. If there is error, then show error else it will go back to home.ftl
// LOG IN SUCCESSFUL COME HERE
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;
    public CurrentUser(User user) {   	
    	   super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList((user.getRolesAsStringArray())));
    	   System.out.println("Set user as current user");
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Collection<Role> getRoles() {
        return user.getRoles();
    }
    
    public void setRoles(){
    	
    }
    public String[] getRolesAsStringArray() {
        return user.getRolesAsStringArray();
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
