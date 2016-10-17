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
@Table(name = "schedule")
public class MaintenanceSchedule {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id", nullable = false, updatable = false)
	 private Long id;
	 
	 @Column(name = "room")
	 private Long room;

	@Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "start", nullable = false)
	 private Date start_time;
	    
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "end", nullable = false)
	 private Date end_time;

     @ManyToOne
     @JsonIgnore
     private Unit unit;
     
     @ManyToOne
     @JsonIgnore
     private Maintenance maintenance;
     
     public MaintenanceSchedule() {
 		//super();
 	}

	
	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date event_start_date_time) {
		this.start_time = event_start_date_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date event_end_date_time) {
		this.end_time = event_end_date_time;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public Maintenance getMaintenance() {
		return maintenance;
	}

	public void setMaintenance(Maintenance maintenance) {
		this.maintenance = maintenance;
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

}
