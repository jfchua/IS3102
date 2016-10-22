package application.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_policy")
public class PaymentPolicy {
	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id", nullable = false, updatable = false)
	 private Long id;
	 
	 @Column(name = "deposit_rate")
	 private Double depositRate;
	 
	 @Column(name = "subsequent_number")
	 private int subsequentNumber;
	 
	 @Column(name = "due_days")
	 private int dueDays;
	 
	 public PaymentPolicy(){
		 super();
	 }

	public Double getDepositRate() {
		return depositRate;
	}

	public void setDepositRate(Double depositRate) {
		this.depositRate = depositRate;
	}

	public int getSubsequentNumber() {
		return subsequentNumber;
	}

	public void setSubsequentNumber(int subsequentNumber) {
		this.subsequentNumber = subsequentNumber;
	}

	public Long getId() {
		return id;
	}

	public int getNumOfDueDays() {
		return dueDays;
	}

	public void setNumOfDueDays(int numOfDueDays) {
		this.dueDays = numOfDueDays;
	}
	
	
}
