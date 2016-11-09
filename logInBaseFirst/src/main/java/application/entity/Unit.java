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

	@Column(name = "sizex", nullable = false)
	private int sizeX;

	@Column(name = "sizey", nullable = false)
	private int sizeY;
	
	@Column(name = "row", nullable = false)
	private int row;

	@Column(name = "col", nullable = false)
	private int col;
	
	@Column(name = "rentable", nullable = false)
	private Boolean rentable;

	@Column(name = "rent")
	private Double rent;

	@Column(name = "description", nullable = false)
	private String description;

	
	
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
	
	
	@ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.REFRESH})
	@JoinTable( 
			name = "units_unitAttributeValues", 
			joinColumns = @JoinColumn(name = "unit_id", referencedColumnName = "id"), 
			inverseJoinColumns = @JoinColumn(name = "unitAttributeValue_id", referencedColumnName = "id"))
	@JsonIgnore
	private Set<UnitAttributeValue> unitAttributeValues = new HashSet<UnitAttributeValue>();
	
	// @OneToMany(mappedBy ="unit", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	// @JsonIgnore
	// private Set<Area> areas = new HashSet<Area>();
	 @OneToMany(fetch = FetchType.EAGER)
		private Set<Area> areas=new HashSet<Area>();
	  
	public Set<Area> getAreas() {
		return areas;
	}

	public void setAreas(Set<Area> areas) {
		this.areas = areas;
	}

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
	

	public int getSizex() {
		return sizeX;
	}

	public void setSizex(int sizex) {
		this.sizeX = sizex;
	}

	public int getSizey() {
		return sizeY;
	}

	public void setSizey(int sizey) {
		this.sizeY = sizey;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public Set<MaintenanceSchedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(Set<MaintenanceSchedule> schedule) {
		this.schedule = schedule;
	}

	public Boolean getRentable() {
		return rentable;
	}

	public void setRentable(Boolean rentable) {
		this.rentable = rentable;
	}

	public Double getRent() {
		return rent;
	}
	public void setRent(Double rent) {
		this.rent = rent;
	}
	public int getDimension() {
		return this.sizeX*this.sizeY;
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


/*
	public Set<Maintenance> getMaintenances() {
		return maintenances;
	}
	public void setMaintenances(Set<Maintenance> maintenances) {
		this.maintenances = maintenances;
	}*/

	public Set<UnitAttributeValue> getUnitAttributeValues() {
		return unitAttributeValues;
	}

	public void setUnitAttributeValues(Set<UnitAttributeValue> unitAttributeValues) {
		this.unitAttributeValues = unitAttributeValues;
	}

	@Override
	public String toString() {
		return "Unit [id=" + id + ", unitNumber=" + unitNumber + ", square=" + square + ", sizex=" + sizeX + ", sizey="
				+ sizeY + ", row=" + row + ", col=" + col + ", rentable=" + rentable + ", rent=" + rent
				+ ", description=" + description + ", level=" + level + ", bookings=" + bookings
				+ ", unitAttributeValues=" + unitAttributeValues + ", schedule=" + schedule + "]";
	}


}
