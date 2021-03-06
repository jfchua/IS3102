package application.repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Event;
import application.entity.Message;
import application.entity.User;

public interface EventRepository extends JpaRepository<Event, Long>{
	//find event by title
	//public Optional<Event> findEventByevent_title(String title);
	
/*	@Query(
	        value = "SELECT * FROM Event e where e.event_title = :event", 
	        nativeQuery=true
	   )
 public Collection<Event> getEventsByTitle(@Param("event") String event);
 */
	@Query(
	        value = "SELECT * FROM Event", 
	        nativeQuery=true
	   )
 public Set<Event> getAllEvents();
}
