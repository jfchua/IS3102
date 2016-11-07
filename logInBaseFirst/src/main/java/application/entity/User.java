package application.entity;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;

import application.repository.MessageRepository;
import application.repository.UserRepository;
import application.service.MessageService;


@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	//must be the same as the one referenced from  the SecurityConfig
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@OneToMany(fetch = FetchType.LAZY,cascade={CascadeType.ALL})
	@Column(nullable = true)
	@JsonIgnore
	private Set<Ticket> tickets = new HashSet<Ticket>();

	public Set<Ticket> getTickets() {
		return tickets;
	}
	public void setTickets(Set<Ticket> tickets) {
		this.tickets = tickets;
	}
	@Column(name = "password_hash", nullable = false)
	private String passwordHash;

	@Column(name = "security", nullable = true)
	private String security;
	
	@Column(name = "security_question", nullable = true)
	private String securityQuestion;
	
	//// CREATE NEW USER//////////////	
	@Column(name = "name", nullable = false, updatable = true)
	private String name;

	//////////////////////////////////

	/*@Column(name = "role", nullable = false)
    private Set<Role> roles = new HashSet<Role>();*/

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable( 
			name = "users_roles", 
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@JsonIgnore
	private Set<Role> roles = new HashSet<Role>();

	//(fetch = FetchType.EAGER)
	@OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<ToDoTask> toDoList = new HashSet<ToDoTask>();

	//@OneToMany(fetch = FetchType.EAGER)
	//private Set<Message> messagesSent = new HashSet<Message>();
	@OneToMany(fetch = FetchType.LAZY, mappedBy="recipient",cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Message> messagesReceived = new HashSet<Message>();


	@ManyToOne(fetch = FetchType.EAGER) 
	private ClientOrganisation clientOrganisation = new ClientOrganisation();

	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
	@JsonIgnore
	private Set<Event> events = new HashSet<Event>();

	public ClientOrganisation getClientOrganisation() {
		return clientOrganisation;
	}
	public void setClientOrganisation(ClientOrganisation clientOrganisation) {
		this.clientOrganisation = clientOrganisation;
	}

	public Set<Event> getEvents() {
		return events;
	}
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	public User(){

	}
	public Set<Role> getRoles() {
		return roles;
	}

	public String[] getRolesAsStringArray(){
		int size = roles.size();
		String[] temp = new String[size];
		int counter = 0;
		for ( Role r : roles){
			temp[counter] = r.getName();
			counter++;
		}
		return temp;


	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	// ---------- TODO LIST METHODS START ------------------
	public Set<ToDoTask> getToDoList() {
		return toDoList;
	}

	public void setToDoList(Set<ToDoTask> toDoList) {
		this.toDoList = toDoList;
	}

	public void addTask(ToDoTask task){
		toDoList.add(task);	
	}

	public void deleteToDoTask(long taskId){
		for ( ToDoTask t : toDoList){
			if ( t.getId() == taskId){
				toDoList.remove(t);
				break;
			}
		}
	}
	//DO UPDATE IF NEEDED
	/*public void updateToDo(long taskId,String newToDo){
		for ( ToDoTask t : toDoList){
			if ( t.getId() == taskId){
				HashSet<ToDoTask> temp = (HashSet<ToDoTask>)toDoList;
				int currentIndex = temp.indexOf(t);

				ToDoTask tempTask = new ToDoTask();
				tempTask.setTask(newToDo);

				temp.add(currentIndex, tempTask);
				temp.remove(t);
				toDoList = temp;
				break;
			}
		}
	}*/

	//---------- TODO LIST METHODS END ---------------

	//---------- MESSAGES METHODS START -----------------

	/*	public Set<Message> getMessagesSent() {
		return messagesSent;
	}
	public void setMessagesSent(Set<Message> messagesSent) {
		this.messagesSent = messagesSent;
	}*/
	public Set<Message> getMessagesReceived() {
		System.out.println("AT USER: " + messagesReceived.size());
		return messagesReceived;
	}
	public void setMessagesReceived(Set<Message> messagesReceived) {
		this.messagesReceived = messagesReceived;
	}

	/*	public void addSentMessage(Message m){
		this.messagesSent.add(m);
	}*/

	public void addReceivedMessage(Message m){
		//	System.out.println("Received in user message: " + m.getMessage());
		this.messagesReceived.add(m);
		//	System.out.println("Received Message new size " + this.messagesReceived.size());
	}

	public void deleteReceviedMessage(Message m){
		this.messagesReceived.remove(m);
	}



	//----------- MESSAGES METHODS END --------------------

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}


	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email.replaceFirst("@.*", "@***") +
				", passwordHash='" + passwordHash.substring(0, 10) +
				'}';

	}
	public String getSecurity() {
		return security;
	}

	public void setSecurity(String security) {
		this.security = security;
	}
	
	public String getSecurityQuestion() {
		return securityQuestion;
	}

	public void setSecurityQuestion(String securityQuestion) {
		this.securityQuestion = securityQuestion;
	}

}