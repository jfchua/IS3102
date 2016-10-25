package application.service;

import java.util.Set;

import application.entity.Category;
import application.exception.EventNotFoundException;

public interface TicketingService {

	public boolean addCategory(Long eventId, String catName, double price, int numTix) throws EventNotFoundException;

	Set<Category> getCategories(Long eventId) throws EventNotFoundException;
	
	public boolean deleteCat(Long id);
	
}