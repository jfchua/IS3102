package application.entity;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;








@Entity
@Table(name = "unit")
public class Unit {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;



	@Column(name = "unitNumber", nullable = false)
	private String unitNumber;

	@OneToOne(fetch = FetchType.EAGER)
	private Square square;

	@Column(name = "length", nullable = false)
	private int length;

	@Column(name = "width", nullable = false)
	private int width;

	@Column(name = "rentable", nullable = false)
	private Boolean rentable;

	@Column(name = "rent")
	private Long rent;

	@Column(name = "description", nullable = false)
	private String description;

	
	@Column(name = "availability", nullable = true)
	@JsonIgnore
	private ArrayList<Date> avail = new ArrayList<Date>();

	@ManyToOne//(cascade=CascadeType.ALL)
	@JsonIgnore
	private Level level;
    /*
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "units")
	@JsonIgnore
	private Set<Event> events = new HashSet<Event>();
*/
	
	@OneToMany(mappedBy ="unit", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<BookingAppl> bookings = new HashSet<BookingAppl>();

	public Set<BookingAppl> getBookings() {
		return bookings;
	}
	
	public void setBookings(Set<BookingAppl> bookings) {
		this.bookings = bookings;
	}

	 @OneToMany(mappedBy ="unit", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
     @JsonIgnore
	 private Set<MaintenanceSchedule> schedule = new HashSet<MaintenanceSchedule>();

	 public Set<MaintenanceSchedule> getMaintenanceSchedule() {
			return schedule;
		}
		
     public void setMaintenanceSchedule(Set<MaintenanceSchedule> schedule) {
			this.schedule = schedule;
		}
	/*
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "UNITS_MAINTENANCES", joinColumns = {
			@JoinColumn(name = "UNIT_ID", nullable = false, updatable = false) },
	inverseJoinColumns = { @JoinColumn(name = "MAINTENANCE_ID",
	nullable = false, updatable = false) })
	@JsonIgnore
	private Set<Maintenance> maintenances = new HashSet<Maintenance>();*/

/*
	public Set<Event> getEvents() {
		return events;
	}
	public void setEvents(Set<Event> events) {
		this.events = events;
	}*/
	

	public void createList(){
		if(avail == null)
			avail = new ArrayList<Date>();			
	}

	public Unit() {

	}
	public Long getId() {
		return id;
	}


	public Square getSquare() {
		return square;
	}
	public void setSquare(Square square) {
		this.square = square;
	}
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Boolean getRentable() {
		return rentable;
	}

	public void setRentable(Boolean rentable) {
		this.rentable = rentable;
	}

	public long getRent() {
		return rent;
	}
	public void setRent(long rent) {
		this.rent = rent;
	}
	public int getDimension() {
		return this.width*this.length;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}


	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}

	public ArrayList<Date> getAvail() {
		return avail;
	}	

	public void setAvail(ArrayList<Date> avail) {
		this.avail = avail;
	}

/*
	public Set<Maintenance> getMaintenances() {
		return maintenances;
	}
	public void setMaintenances(Set<Maintenance> maintenances) {
		this.maintenances = maintenances;
	}*/

	@Override
	public String toString() {
		return "Unit [id=" + id + ", unitNumber=" + unitNumber + ", square=" + square.getId() + ", length=" + length
				+ ", width=" + width + ", rentable=" + rentable + ", rent=" + rent + ", description=" + description
				+ ", level=" + level + "]";
	}


}
