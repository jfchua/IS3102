package application.service.user;

import java.security.Principal;
import java.util.Collection;

import application.domain.Message;
import application.domain.SendMessageForm;
import application.domain.User;

public interface MessageService {

	void sendMessage(User sender,User recipient,String subject, String msg);
	Collection<Message> getMessages(User recipient);
	void deleteMessage(Message m);
	//Collection<Message> getMessages(Principal principal);
	/*Collection<Message> getMessages();*/
	
    
}
