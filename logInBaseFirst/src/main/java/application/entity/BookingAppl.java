package application.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "booking_appl")
public class BookingAppl {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id", nullable = false, updatable = false)
	 private Long id;
	 
	 @Column(name = "room")
	 private Long room;
	 
	 @Column(name = "owner")
	 private Long owner;


	@Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "event_start_date_time", nullable = false)
	 private Date event_start_date_time;
	    
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "event_end_date_time", nullable = false)
	 private Date event_end_date_time;

     @ManyToOne
     @JsonIgnore
     private Unit unit;
     
     @ManyToOne
     @JsonIgnore
     private Event event;
     
     @OneToMany(mappedBy ="booking", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
     @JsonIgnore
	 private Set<Area> areas = new HashSet<Area>();
     
     public BookingAppl() {
 		//super();
 	}

     
     public Set<Area> getAreas() {
		return areas;
	}

	public void setAreas(Set<Area> areas) {
		this.areas = areas;
	}

	
	public Date getEvent_start_date_time() {
		return event_start_date_time;
	}

	public void setEvent_start_date_time(Date event_start_date_time) {
		this.event_start_date_time = event_start_date_time;
	}

	public Date getEvent_end_date_time() {
		return event_end_date_time;
	}

	public void setEvent_end_date_time(Date event_end_date_time) {
		this.event_end_date_time = event_end_date_time;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Long getId() {
		return id;
	}
	
	
	
	public Long getRoom() {
		return room;
	}


	public void setRoom(Long room) {
		this.room = room;
	}


	public Long getOwner() {
		return owner;
	}


	public void setOwner(Long owner) {
		this.owner = owner;
	}

}
