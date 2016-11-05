package application.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "payment")
public class Payment {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id", nullable = false, updatable = false)
	 private Long id;
	 
	 @Column(name = "amount")
	 private Double amount;
	 
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "paid")
	 private Date paid;
	 
	 @Column(name = "plan")
	 private Long plan;
	 
	 @Column(name = "invoice")
	 private String invoice;
	 
	 @Column(name = "cheque")
	 private String cheque;
	 /*
	 @ManyToOne
	 @JsonIgnore
	 private PaymentPlan paymentPlan;*/
	 
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getPaid() {
		return paid;
	}

	public void setPaid(Date paid) {
		this.paid = paid;
	}

	public Long getPlan() {
		return plan;
	}

	public void setPlan(Long plan) {
		this.plan = plan;
	}
	
	
public String getCheque() {
		return cheque;
	}

	public void setCheque(String cheque) {
		this.cheque = cheque;
	}
	
	public String getInvoice() {
		return invoice;
	}

	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}

	public Long getId() {
		return id;
	}
/*
	public PaymentPlan getPaymentPlan() {
		return paymentPlan;
	}

	public void setPaymentPlan(PaymentPlan paymentPlan) {
		this.paymentPlan = paymentPlan;
	}
	*/
}
