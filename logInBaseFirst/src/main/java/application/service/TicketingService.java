package application.service;

import java.util.ArrayList;
import java.util.Set;

import application.entity.Category;
import application.entity.Ticket;
import application.entity.User;
import application.exception.EmailAlreadyExistsException;
import application.exception.EventNotFoundException;
import application.exception.InvalidEmailException;
import application.exception.UserNotFoundException;

public interface TicketingService {

	public boolean addCategory(Long eventId, String catName, double price, int numTix) throws EventNotFoundException;

	Set<Category> getCategories(Long eventId) throws EventNotFoundException;

	public boolean deleteCat(Long id);
	
	public Ticket getTicketByCode(String code);

	public String getEventDataAsJson(Long eventId) throws EventNotFoundException;

	public ArrayList<String> generateTicket(User user, String paymentId, int numTickets, Long categoryId);

	public int checkTickets(int numTickets, Long categoryId);

	boolean updateCategory(Long catId, String catName, double price, int numTix) throws EventNotFoundException;
	
	boolean redeemTicket(String qrCode);
	
	public ArrayList<String> viewTransactionHistory(Long userId) throws UserNotFoundException;

	boolean registerNewUser(String name, String email, String pass) throws EmailAlreadyExistsException, UserNotFoundException, InvalidEmailException;

	boolean checkValidity(String code);

}