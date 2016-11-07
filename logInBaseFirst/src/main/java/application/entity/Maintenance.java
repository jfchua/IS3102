package application.entity;
//import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity

@Table(name = "MAINTENANCE")
public class Maintenance {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	 
	 @Column(name = "commence", nullable = false)
	 private Date start;
	 
	 @Column(name = "finish", nullable = false)
	 private Date end;
	 
	 @Column(name = "description", nullable = false)
	 private String description;

	 /*
	 @Column(name = "fullday", nullable = false)
	    private Boolean fullday;
	 */
	 @Column(name = "client", nullable = false)
	 private Long client;
	 
	 @ManyToMany
	 @Column(name = "vendors", nullable = false)
	 @JsonIgnore
	 private Set<Vendor> vendors = new HashSet<Vendor>();
	 
	 @OneToMany(mappedBy ="maintenance", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
		@JsonIgnore
		private Set<MaintenanceSchedule> schedule = new HashSet<MaintenanceSchedule>();

	    public Set<MaintenanceSchedule> getMaintenanceSchedule() {
			return schedule;
		}
		
		public void setMaintenanceSchedule(Set<MaintenanceSchedule> schedule) {
			this.schedule = schedule;
		}
		/*
	 @ManyToMany(fetch = FetchType.LAZY, mappedBy = "maintenances")
	 @JsonIgnore
	 private Set<Unit> units = new HashSet<Unit>();*/
/*
	public Boolean getFullday() {
		return fullday;
	}

	public void setFullday(Boolean fullday) {
		this.fullday = fullday;
	}

	*/

    public Long getId() {
		return id;
	}

	public Set<Vendor> getVendors() {
		return vendors;
	}

	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Maintenance [ vendors=" + vendors + ", description=" + description
				+ ", toString()=" + super.toString() + "]";
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
/*
	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}*/

	public Long getClient() {
		return client;
	}

	public void setClient(Long client) {
		this.client = client;
	}
}
