package application.service;

import java.security.Principal;
import java.util.Collection;

import application.entity.Message;
import application.entity.SendMessageForm;
import application.entity.User;
import application.exception.MessageNotFoundException;
import application.exception.UserNotFoundException;

public interface MessageService {

	boolean sendMessage(User sender,User recipient,String subject, String msg) throws UserNotFoundException;
	Collection<Message> getMessages(User recipient);
	boolean deleteMessage(Message m) throws MessageNotFoundException;
	//Collection<Message> getMessages(Principal principal);
	/*Collection<Message> getMessages();*/
	
    
}
