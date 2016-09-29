package application.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
	@Column(name = "organisation_name",nullable = false)
	private String organisationName;
	@OneToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	private Set<Vendor> vendors = new HashSet<Vendor>();
	@OneToMany(mappedBy="clientOrganisation", fetch = FetchType.EAGER,cascade=CascadeType.ALL)
	@JsonIgnore
	private Set<User> users = new HashSet<User>();
	
	public ClientOrganisation() {
		super();
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
	
	


}
