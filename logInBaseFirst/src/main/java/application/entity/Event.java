package application.entity;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import application.enumeration.ApprovalStatus;
import application.enumeration.EventType;
import application.enumeration.IconType;
import application.enumeration.PaymentStatus;


@Entity
@Table(name = "event")
public class Event {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id", nullable = false, updatable = false)
	    private Long id;
	    //must be the same as the one referenced from  the SecurityConfig
	    //@Column(name = "user", nullable = true)
	    //private String user;
		
		@Column(name = "event_title", nullable = false)
	    private String event_title;

		@Enumerated(EnumType.STRING)
	    private EventType event_type;
	    
	    @Column(name = "event_description", nullable = false)
	    private String event_description;
	    
	    @Enumerated(EnumType.STRING)
	    private ApprovalStatus approvalStatus;


	    @Temporal(TemporalType.TIMESTAMP)
		 @Column(name = "event_start_date", nullable = false)
		 private Date event_start_date;
		    
		 @Temporal(TemporalType.TIMESTAMP)
		 @Column(name = "event_end_date", nullable = false)
		 private Date event_end_date;
	    
	    @Column(name = "filePath", nullable = true)
	    private String filePath;	
	    
	    @Enumerated(EnumType.STRING)
		 private PaymentStatus paymentStatus;
	    //@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
	    /*@OneToMany(mappedBy ="event", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	    @JsonIgnore
	    private Set<Area> areas = new HashSet<Area>();*/
	    
		@ManyToOne
		private User eventOrg;
		
		@OneToOne
	    @JsonIgnore
	    private PaymentPlan paymentPlan;
		/*
		@ManyToMany(fetch = FetchType.EAGER)
		@JoinTable( 
				name = "events_units", 
				joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"), 
				inverseJoinColumns = @JoinColumn(name = "unit_id", referencedColumnName = "id")) 
		private Set<Unit> units = new HashSet<Unit>();
		
		
		public Set<Unit> getUnits() {
			return units;
		}

		public void setUnits(Set<Unit> units) {
			this.units = units;
		}*/
		@OneToMany(mappedBy ="event", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
		@JsonIgnore
		private Set<BookingAppl> bookings = new HashSet<BookingAppl>();

		public PaymentPlan getPaymentPlan() {
			return paymentPlan;
		}

		public void setPaymentPlan(PaymentPlan paymentPlan) {
			this.paymentPlan = paymentPlan;
		}

		public Set<BookingAppl> getBookings() {
			return bookings;
		}

		public void setBookings(Set<BookingAppl> bookings) {
			this.bookings = bookings;
		}
		
		
		public Date getEvent_start_date() {
			return event_start_date;
		}

		public void setEvent_start_date(Date event_start_date) {
			this.event_start_date = event_start_date;
		}

		public Date getEvent_end_date() {
			return event_end_date;
		}

		public void setEvent_end_date(Date event_end_date) {
			this.event_end_date = event_end_date;
		}

		public String getFilePath() {
			return filePath;
		}

		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		/*
	    public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}*/
	   
		public String getEvent_title() {
			return event_title;
		}

		public void setEvent_title(String event_title) {
			this.event_title = event_title;
		}

		public EventType getEventType() {
			return event_type;
		}

		public void setEventType(EventType event_content) {
			this.event_type = event_content;
		}

		public String getEvent_description() {
			return event_description;
		}

		public void setEvent_description(String event_description) {
			this.event_description = event_description;
		}

		public ApprovalStatus getApprovalStatus() {
			return approvalStatus;
		}

		public void setApprovalStatus(ApprovalStatus event_approval_status) {
			this.approvalStatus = event_approval_status;
		}
		/*
		public String getEvent_organisation() {
			return event_organisation;
		}

		public void setEvent_organisation(String event_organisation) {
			this.event_organisation = event_organisation;
		}*/

		
		public User getEventOrg() {
			return eventOrg;
		}

		public void setEventOrg(User eventOrg) {
			this.eventOrg = eventOrg;
		}
       /*
		public String getEvent_period() {
			return event_period;
		}

		public void setEvent_period(String event_period) {
			this.event_period = event_period;
		}*/

	    public Long getId() {
	        return id;
	    }
       /*
		public Set<Area> getAreas() {
			return areas;
		}

		public void setAreas(Set<Area> areas) {
			this.areas = areas;
		}*/

		public PaymentStatus getPaymentStatus() {
			return paymentStatus;
		}

		public void setPaymentStatus(PaymentStatus paymentStatus) {
			this.paymentStatus = paymentStatus;
		}

}
