package application.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "event")
public class Event {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id", nullable = false, updatable = false)
	    private Long id;
	    //must be the same as the one referenced from  the SecurityConfig
	    @Column(name = "user")
	    private String user;
	 
	 	public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		@Column(name = "event_title", nullable = false)
	    private String event_title;

	    @Column(name = "event_content", nullable = false)
	    private String event_content;
	    
	    @Column(name = "event_description", nullable = false)
	    private String event_description;
	    
	  

		public String getEvent_title() {
			return event_title;
		}

		public void setEvent_title(String event_title) {
			this.event_title = event_title;
		}

		public String getEvent_content() {
			return event_content;
		}

		public void setEvent_content(String event_content) {
			this.event_content = event_content;
		}

		public String getEvent_description() {
			return event_description;
		}

		public void setEvent_description(String event_description) {
			this.event_description = event_description;
		}

		public void setId(Long id) {
			this.id = id;
		}

	    public Long getId() {
	        return id;
	    }

}
