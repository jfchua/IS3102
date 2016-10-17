package application.service;

import java.security.Principal;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import application.entity.AuditLog;
import application.entity.Message;
import application.entity.SendMessageForm;
import application.entity.User;
import application.exception.MessageNotFoundException;
import application.exception.UserNotFoundException;
import application.repository.AuditLogRepository;
import application.repository.MessageRepository;
import application.repository.UserRepository;

@Service
public class MessageServiceImpl implements MessageService {
	private final MessageRepository messageRepository;
	private final UserRepository userRepository;

	private final AuditLogRepository auditLogRepository;

	@Autowired
	public MessageServiceImpl(MessageRepository messageRepository, AuditLogRepository auditLogRepository, UserRepository userRepository) {
		super();
		this.messageRepository = messageRepository;
		this.auditLogRepository = auditLogRepository;
		this.userRepository = userRepository;
	}
	//Takes in message information from a form instance and persists it into database
	/*	public Message sendMessage(SendMessageForm form){
		Message message = new Message();
		message.setMessage(form.getMessage());	
		message.setRecipient(form.getRecipient());
		//Get current user and set him as sender, only email available
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails currentUser = (UserDetails) authentication.getPrincipal();
		String currentUsername = currentUser.getUsername();
		message.setSender(currentUsername);

		//INCOMPLETE LOGGING
		AuditLog al = new AuditLog();
		al.setTimeToNow();
		al.setSystem("Notification");
		al.setAction("Sending Notification Message");
		//al.setEmail(currentUsername);
		auditLogRepository.save(al);
		//END LOGGING
		return messageRepository.save(message);
	}*/
	//Overloaded method which  takes in the Sender,Recipient and Message rather than a form and persists it into database.
	//Used for system alerts or general notification rather than p2p messages
	// *Inject this dependency(MsgServiceImpl class) into other service classes before use*
	/*public Message sendMessage(String sender,String recipient,String subject, String msg){
		Message message = new Message();
		message.setMessage(msg);	
		message.setSubject(subject);
		message.setRecipient(recipient);
		message.setSender(sender);
		return messageRepository.save(message);

	}*/
	//Gets a list of rows of messages from the database where currentUser matches recipient
	//Possible improvements with @Scheduled to regularly update for new notifications from database
	public Collection<Message> getMessages(User recipient){
		//To get the current user who is using the application from the spring security	
		//return messageRepository.findAll(new Sort("recipient"));
		Collection<Message> t = recipient.getMessagesReceived();
		System.out.println("GETING MESSAGS FROM " + recipient.getEmail() + " WITH SIZE " + t.size());
		return t; //Currently unsorted
	}
	//FOR SYSTEM MSSAGES, THE SENDER OR USER IS "SYSTEM"?
	@Transactional
	public boolean sendMessage(User sender,User recipient,String subject, String msg) throws UserNotFoundException{
		if ( sender == null || recipient == null ){
			throw new UserNotFoundException("User not found!");
		}
		try{
			Message m = new Message();
			m.setMessage(msg);
			m.setSubject(subject);
			m.setRecipient(recipient);
			//m.setSender(sender);
			m.setSenderName(sender.getEmail());
			//sender.addSentMessage(m);
			recipient.addReceivedMessage(m);
			messageRepository.save(m);
			userRepository.save(sender);
			userRepository.save(recipient);
			AuditLog al = new AuditLog();
			al.setTimeToNow();
			al.setSystem("Notification");
			al.setAction("Send Notification Message To " + recipient.getEmail() );
			//al.setEmail(currentUsername);
			al.setUser(sender);
			al.setUserEmail(sender.getEmail());
			auditLogRepository.save(al);
		}
		catch ( Exception e){
			return false;
		}
		return true;
	}

	public boolean deleteMessage(Message m) throws MessageNotFoundException{
		if ( m == null ){
			throw new MessageNotFoundException("Message was not found");
		}
		try{
			User recipient = m.getRecipient();
			recipient.deleteReceviedMessage(m);
			userRepository.save(recipient);
			messageRepository.delete(m);	
		}
		catch ( Exception e){
			return false;
		}
		return true;

	}

}

//OLD CODE
/*public Collection<Message> getMessages(){

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	      String name = auth.getName(); //get logged in username
	      System.out.println(auth.getCredentials().toString());
	      System.out.println("NAME IS : " + name);
	      return messageRepository.getMessagesByRecipient(name);
	}*/


