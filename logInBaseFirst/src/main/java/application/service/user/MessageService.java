package application.service.user;

import java.security.Principal;
import java.util.Collection;

import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;
import application.exception.MessageNotFoundException;
import application.exception.UserNotFoundException;

public interface MessageService {

	boolean sendMessage(User sender,User recipient,String subject, String msg) throws UserNotFoundException;
	Collection<Message> getMessages(User recipient);
	boolean deleteMessage(Message m) throws MessageNotFoundException;
	//Collection<Message> getMessages(Principal principal);
	/*Collection<Message> getMessages();*/
	
    
}
