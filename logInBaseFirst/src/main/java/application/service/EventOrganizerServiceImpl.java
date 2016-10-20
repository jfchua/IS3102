package application.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import application.entity.EventOrganizer;
import application.repository.EventOrganizerRepository;

@Service
public class EventOrganizerServiceImpl implements EventOrganizerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EventOrganizerServiceImpl.class);
	private final EventOrganizerRepository eventOrganizerRepository;
	
	@Autowired
	public EventOrganizerServiceImpl(EventOrganizerRepository eventOrganizerRepository ) {
		this.eventOrganizerRepository = eventOrganizerRepository;
	}
	
	@Override
	public Optional<EventOrganizer> getEventOrganizerById(long id) {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting event organizer={}", id);
		return Optional.ofNullable(eventOrganizerRepository.findOne(id));
	}

	@Override
	public Optional<EventOrganizer> getEventOrganizerByEmail(String email) {
		// TODO Auto-generated method stub	
		LOGGER.debug("Getting event organizer by email={}", email.replaceFirst("@.*", "@***"));
		return eventOrganizerRepository.findOneByEmail(email);
	}

	@Override
	public Set<EventOrganizer> getAllEventOrganizers() {
		// TODO Auto-generated method stub
		LOGGER.debug("Getting all users");
		return eventOrganizerRepository.getAllEventOrganizers();
	}

	@Override
	public void updateProfile(long id, String firstName, String lastName, String phoneNo) {
		// TODO Auto-generated method stub
		Optional<EventOrganizer> user = getEventOrganizerById(id);
		if (user.isPresent()){
		user.get().setFirstName(firstName);
		user.get().setLastName(lastName);
		user.get().setPhoneNo(phoneNo);
		eventOrganizerRepository.save(user.get());
		}
		System.out.println("update event organizer profile");
	}

}
