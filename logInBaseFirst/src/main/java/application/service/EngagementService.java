package application.service;

import application.entity.User;

public interface EngagementService {

	
	public void setFeedback(User usr, String cat, String msg);
}
