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
	 private Double deposit_rate;
	 
	 @Column(name = "subsequent_number")
	 private int subsequent_number;
	 
	 public PaymentPolicy(){
		 super();
	 }

	public Double getDepositRate() {
		return deposit_rate;
	}

	public void setDepositRate(Double depositRate) {
		this.deposit_rate = depositRate;
	}

	public int getSubsequentNumber() {
		return subsequent_number;
	}

	public void setSubsequentNumber(int subsequentNumber) {
		this.subsequent_number = subsequentNumber;
	}

	public Long getId() {
		return id;
	}
}
