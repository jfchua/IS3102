package application.entity;

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
@Table(name = "unitAttributeType")
public class UnitAttributeType {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
	
	 @Column(name = "attributeType", nullable = false)
	 private String attributeType;

	 @OneToMany(mappedBy ="unitAttributeType", fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	  @JsonIgnore
	  private Set<UnitAttributeValue> unitAttributeValues = new HashSet<UnitAttributeValue>();
	 
	public String getAttributeType() {
		return attributeType;
	}


	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}


	public Long getId() {
		return id;
	}


	public Set<UnitAttributeValue> getUnitAttributeValues() {
		return unitAttributeValues;
	}


	public void setUnitAttributeValues(Set<UnitAttributeValue> unitAttributeValues) {
		this.unitAttributeValues = unitAttributeValues;
	}
	 
	 
	 
	
}
