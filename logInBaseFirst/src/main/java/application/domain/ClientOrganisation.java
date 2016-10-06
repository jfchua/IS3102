package application.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "client_organisation")
public class ClientOrganisation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	@Column(name = "organisation_name",nullable = false,unique=true)
	private String organisationName;
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private Set<Vendor> vendors = new HashSet<Vendor>();
	@OneToMany(mappedBy="clientOrganisation", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<User> users = new HashSet<User>();	
	//@Column(name = "systemSubscriptions",nullable = true)
	//private String[] systemSubscriptions;
	@Column(name = "systemSubscriptions",nullable = true)
	@ElementCollection //
	@JsonIgnore
	private List<String> systemSubscriptions;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Building> buildings = new HashSet<Building>();

	@Column(name = "logoFilePath",nullable = true,unique=true)
	@JsonIgnore
	private String logoFilePath;
	
	@OneToMany(fetch = FetchType.EAGER)

	
	public ClientOrganisation() {
		super();
	}



	/*public String[] getSystemSubscriptions() {
		return systemSubscriptions;
	}

	public void setSystemSubscriptions(String[] systemSubscriptions) {
		this.systemSubscriptions = systemSubscriptions;
	}*/



	public Set<Building> getBuildings() {
		return buildings;
	}



	public void setBuildings(Set<Building> buildings) {
		this.buildings = buildings;
	}



	public String getLogoFilePath() {
		return logoFilePath;
	}



	public void setLogoFilePath(String logoFilePath) {
		this.logoFilePath = logoFilePath;
	}



	public List<String> getSystemSubscriptions() {
		return systemSubscriptions;
	}



	public void setSystemSubscriptions(List<String> systemSubscriptions) {
		this.systemSubscriptions = systemSubscriptions;
	}



	//TODO: ADD MORE THINGS. MAP TO USERS.
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public Set<Vendor> getVendors() {
		return vendors;
	}
	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public void addUser(User user){
		users.add(user);
	}
	public void removeUser(User user){
		if ( users.contains(user)){
			users.remove(user);
		}
	}



	public Set<Icon> getIcons() {
		return icons;
	}



	public void setIcons(Set<Icon> icons) {
		this.icons = icons;
	}




}