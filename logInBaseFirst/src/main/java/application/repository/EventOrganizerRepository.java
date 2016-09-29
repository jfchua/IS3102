package application.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;


import application.domain.EventOrganizer;
import application.domain.Event;

import java.util.Optional;
import java.util.Set;

public interface EventOrganizerRepository extends JpaRepository<EventOrganizer, Long> {
	 Optional<EventOrganizer> findOneByEmail(String email);
	 
	 @Query(
		        value = "SELECT * FROM organizer", 
		        nativeQuery=true
		   )
	 public Set<EventOrganizer> getAllEventOrganizers();
}
