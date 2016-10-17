package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "special_rate")
public class SpecialRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "rate")
	private Double rate;
	
	@Column(name = "description")
	private String description;	
	/*
	@Column(name = "date")
	private Date date;*/
	
	@Column(name = "period")
	private String period;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
		
	public SpecialRate() {
		super();
	}
	public Long getId() {
		return id;
	}
/*
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
*/
	public String getPeriod() {
		return period;
	}
	
	public void setPeriod(String period) {
		this.period = period;
	}
	
	


}
