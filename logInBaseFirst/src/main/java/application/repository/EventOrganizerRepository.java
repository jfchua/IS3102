package application.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import application.entity.Event;
import application.entity.EventOrganizer;

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
