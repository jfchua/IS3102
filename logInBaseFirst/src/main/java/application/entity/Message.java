package application.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "message")
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "Message")
	private String message;
/*	@Column(name = "Sender")
	private String sender;*/
	@Column(name = "Subject")
	private String subject;
	//must be the same as the one referenced from  the SecurityConfig
/*	@Column(name = "Recipient")
	private String recipient;
*/
	
	@ManyToOne  //CHECK CASCADE
	@JsonIgnore
	private User recipient;
	
/*	@ManyToOne(cascade=CascadeType.ALL) //CHECK CASCADE
	private User sender;*/
	
	
	@Column(name = "sender_name", nullable = false)
	private String senderName;
	

	public String getSenderName() {
		return senderName;
	}



	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}



	public Message() {
		super();
	}
	
	

	public User getRecipient() {
		return recipient;
	}



	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}


/*
	public User getSender() {
		return sender;
	}



	public void setSender(User sender) {
		this.sender = sender;
	}
*/


	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getId() {
		return id;
	}
}
