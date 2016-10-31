package application.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.entity.Discount;
import application.entity.Feedback;
import application.entity.User;
import application.repository.AuditLogRepository;
import application.repository.CategoryRepository;
import application.repository.DiscountRepository;
import application.repository.EventRepository;
import application.repository.FeedbackRepository;
import application.repository.TicketRepository;
import application.repository.UserRepository;

@Service
public class EngagementServiceImpl implements EngagementService{
	
	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;
	private final EventService eventService;
	private final AuditLogRepository auditLogRepository;
	private final EventRepository eventRepository;
	private final CategoryRepository categoryRepository;
	private final TicketRepository ticketRepository;
	private final DiscountRepository discountRepository;

	@Autowired
	public EngagementServiceImpl(DiscountRepository discountRepository, FeedbackRepository feedbackRepository, TicketRepository ticketRepository, CategoryRepository categoryRepository, EventRepository eventRepository, EventService eventService,AuditLogRepository auditLogRepository, UserRepository userRepository) {
		super();
		this.auditLogRepository = auditLogRepository;
		this.userRepository = userRepository;
		this.eventService = eventService;
		this.eventRepository = eventRepository;
		this.categoryRepository = categoryRepository;
		this.ticketRepository = ticketRepository;
		this.feedbackRepository = feedbackRepository;
		this.discountRepository = discountRepository;
	}
	
	public void setFeedback(User usr, String cat, String msg){
		Feedback f = new Feedback();
		f.setCategory(cat);
		f.setFeedbackDate(new Date());
		f.setMessage(msg);
		f.setUser(usr);
		feedbackRepository.save(f);
	}
	
	public Discount getDiscount(String code){
		Discount d = discountRepository.getDiscountByCode(code);
		return d;
	
	}

}
