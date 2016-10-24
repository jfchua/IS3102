package application.entity;

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
import javax.persistence.OneToOne;
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
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<SpecialRate> specialRates = new HashSet<SpecialRate>();
	
	@OneToOne(fetch = FetchType.EAGER)
	private PaymentPolicy paymentPolicy;
	
	

	@Column(name = "logoFilePath",nullable = true,unique=true)
	@JsonIgnore
	private String logoFilePath;
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<Icon> icons=new HashSet<Icon>();
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<UnitAttributeType> unitAttributeTypes=new HashSet<UnitAttributeType>();
	
	@Column(name = "themecolour")
	private String themeColour="blue";
	
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

	public Set<SpecialRate> getSpecialRates() {
		return specialRates;
	}



	public void setSpecialRates(Set<SpecialRate> specialRates) {
		this.specialRates = specialRates;
	}


	
	public PaymentPolicy getPaymentPolicy() {
		return paymentPolicy;
	}



	public void setPaymentPolicy(PaymentPolicy paymentPolicy) {
		this.paymentPolicy = paymentPolicy;
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



	public Set<UnitAttributeType> getUnitAttributeTypes() {
		return unitAttributeTypes;
	}



	public void setUnitAttributeTypes(Set<UnitAttributeType> unitAttributeTypes) {
		this.unitAttributeTypes = unitAttributeTypes;
	}



	public String getThemeColour() {
		return themeColour;
	}



	public void setThemeColour(String themeColour) {
		this.themeColour = themeColour;
	}







}