package application.service;

import application.entity.Discount;
import application.entity.User;

public interface EngagementService {

	
	public void setFeedback(User usr, String cat, String msg);
	
	public Discount getDiscount(String code);
}
