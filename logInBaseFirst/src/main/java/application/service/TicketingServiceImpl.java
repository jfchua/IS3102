package application.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.Category;
import application.entity.Event;
import application.exception.EventNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.CategoryRepository;
import application.repository.EventRepository;
import application.repository.UserRepository;

@Service
public class TicketingServiceImpl implements TicketingService {

	private final UserRepository userRepository;
	private final EventService eventService;
	private final AuditLogRepository auditLogRepository;
	private final EventRepository eventRepository;
	private final CategoryRepository categoryRepository;

	@Autowired
	public TicketingServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository, EventService eventService,AuditLogRepository auditLogRepository, UserRepository userRepository) {
		super();
		this.auditLogRepository = auditLogRepository;
		this.userRepository = userRepository;
		this.eventService = eventService;
		this.eventRepository = eventRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public boolean addCategory(Long eventId, String catName, double price, int numTix) throws EventNotFoundException {
		Optional<Event> e = eventService.getEventById(eventId);
		try{

			Set<Category> temp = e.get().getCategories();
			Category cat = new Category();
			cat.setCategoryName(catName);
			cat.setEvent(e.get());
			cat.setNumOfTickets(numTix);
			cat.setPrice(price);
			temp.add(cat);
			eventRepository.save(e.get());
			return true;
		}
		catch ( Exception ex){
			System.err.println("add category error" + ex.getMessage());
		}

		return false;
	}

	@Override
	public Set<Category> getCategories(Long eventId) throws EventNotFoundException{
		Optional<Event> e = eventService.getEventById(eventId);
		return e.get().getCategories();
	}

	public boolean deleteCat(Long id){
		try{
			Category c = categoryRepository.findOne(id);
			Event e = c.getEvent();
			Set<Category> cats = e.getCategories();
			cats.remove(c);			
			e.setCategories(cats);
			categoryRepository.delete(c);
			eventRepository.save(e);
			
			categoryRepository.flush();
			return true;
		}
		catch (Exception e){
			return false;
		}

	}
}
