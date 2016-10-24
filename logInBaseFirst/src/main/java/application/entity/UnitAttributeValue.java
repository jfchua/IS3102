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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "unitAttributeValue")
public class UnitAttributeValue {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
	
	 @Column(name = "attributeValue", nullable = false)
	 private String attributeValue;
	 
	 @ManyToOne//(cascade=CascadeType.ALL)
		@JsonIgnore
		private UnitAttributeType unitAttributeType;
	 
	 @ManyToMany(fetch = FetchType.EAGER, mappedBy = "unitAttributeValues")
		@JsonIgnore
		private transient Set<Unit> units = new HashSet<Unit>();

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public UnitAttributeType getUnitAttributeType() {
		return unitAttributeType;
	}

	public void setUnitAttributeType(UnitAttributeType unitAttributeType) {
		this.unitAttributeType = unitAttributeType;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

	public Long getId() {
		return id;
	}
	 
}
