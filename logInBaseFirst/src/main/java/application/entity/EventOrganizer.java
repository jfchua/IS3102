package application.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "organizer")
public class EventOrganizer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
    //must be the same as the one referenced from  the SecurityConfig
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "firstName", nullable = false)
    private String firstName;
    
    @Column(name = "lastName", nullable = false)
    private String lastName;
    
    @Column(name = "phoneNo", nullable = true)
    private String phoneNo;
    /*
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
    private Set<Event> events = new HashSet<Event>();
    
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable( 
			name = "organizers_roles", 
			joinColumns = @JoinColumn(name = "organizer_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 
	private Set<Role> roles = new HashSet<Role>();
	*/
	public String getEmail() {
		return email;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmail(String email) {
		this.email = email;
	}
   /*
	public Set<Event> getEvents() {
		return events;
	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}
	*/
	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String name) {
		this.firstName = name;
	}

	/*	
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRole(Set<Role> roles) {
		this.roles = roles;
	}
	*/
	
	
	public Long getId() {
		return id;
	}

	 public String getPhoneNo() {
			return phoneNo;
		}

		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}
	
	
	 @Override
	    public String toString() {
	        return "Event Organizer{" +
	                "id=" + id +
	                ", email='" + email.replaceFirst("@.*", "@***") +
	                ", passwordHash='" + passwordHash.substring(0, 10) +
	                ", first name=" + firstName +
	                ", last name=" + lastName+
	                ", phone number=" +phoneNo+
	                '}';
	    }
}
