package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.ClientOrganisation;
import application.entity.Message;
import application.entity.User;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);
    
    @Query(
	        value = "SELECT * FROM User u where u.client_organisation_id = :client_organisation", 
	        nativeQuery=true
	   )
    public Set<User> getUsersByClientOrgId(@Param("client_organisation") Long id);
    
    @Query(
            value = "SELECT * FROM User user WHERE user.client_organisation_id = :client",
            nativeQuery=true
       )
    public Set<User> getAllUsers(@Param("client") ClientOrganisation client);
    
    
}


