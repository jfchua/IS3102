package application.entity;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import application.enumeration.Subscription;

@Entity
@Table(name = "client_organisation")
public class ClientOrganisation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	@Column(name = "organisation_name",nullable = false,unique=true)
	private String organisationName;
	@Column(name = "address",nullable = false,unique=true)
	private String address;
	@Column(name = "postal",nullable = false,unique=true)
	private String postal;
	@Column(name = "phone",nullable = false,unique=true)
	private String phone;
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<Vendor> vendors = new HashSet<Vendor>();
	@OneToMany(mappedBy="clientOrganisation", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JsonIgnore
	private Set<User> users = new HashSet<User>();	
	//@Column(name = "systemSubscriptions",nullable = true)
	//private String[] systemSubscriptions;
	@Column(name = "systemSubscriptions",nullable = true)
	@ElementCollection //
	@JsonIgnore
	private List<Subscription> systemSubscriptions;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	private Date start_date;
	    
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	private Date end_date;
	
	@Column(name = "fee")
	private Double fee;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Building> buildings = new HashSet<Building>();
	
	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<SpecialRate> specialRates = new HashSet<SpecialRate>();
	
	@OneToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	private PaymentPolicy paymentPolicy;
	
	

	@Column(name = "logoFilePath",nullable = true,unique=true)
	private String logoFilePath;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
	private Set<Icon> icons=new HashSet<Icon>();
	
	@OneToMany(fetch = FetchType.EAGER)
	@JsonIgnore
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



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getPostal() {
		return postal;
	}



	public void setPostal(String postal) {
		this.postal = postal;
	}



	public String getPhone() {
		return phone;
	}



	public void setPhone(String phone) {
		this.phone = phone;
	}


	public Date getStart_date() {
		return start_date;
	}



	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}



	public Date getEnd_date() {
		return end_date;
	}



	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}



	public Double getFee() {
		return fee;
	}



	public void setFee(Double fee) {
		this.fee = fee;
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



	public List<Subscription> getSystemSubscriptions() {
		return systemSubscriptions;
	}



	public void setSystemSubscriptions(List<Subscription> systemSubscriptions) {
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