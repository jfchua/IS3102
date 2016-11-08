package application.service;

import java.util.Set;

import application.entity.Discount;
import application.entity.User;
import application.exception.EventNotFoundException;

public interface EngagementService {

	
	public void setFeedback(User usr, String cat, String msg);
	
	public Discount getDiscount(String code);
	
	public Set<Discount> getDiscounts(Long eventId) throws EventNotFoundException;

	public boolean deleteDiscount(User user, Long valueOf);

	boolean addDiscount(String email, Long eventId, String retailerName, String message) throws EventNotFoundException;

	boolean updateDiscount(Long discountId, String name, String msg);
}
