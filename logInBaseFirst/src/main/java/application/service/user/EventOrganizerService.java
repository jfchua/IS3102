package application.service.user;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import application.domain.EventOrganizer;

public interface EventOrganizerService{
     //void createEventOrganizer();
	
	 Optional<EventOrganizer> getEventOrganizerById(long id);

	 Optional<EventOrganizer> getEventOrganizerByEmail(String email);

	 Set<EventOrganizer> getAllEventOrganizers();
	 
	 void updateProfile(long id, String firstName, String lastName, String phoneNo);
}
