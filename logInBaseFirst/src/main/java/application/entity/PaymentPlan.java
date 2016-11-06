package application.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "payment_plan")
public class PaymentPlan {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "id", nullable = false, updatable = false)
	 private Long id;
	 
	 @Column(name = "total")
	 private Double total;
	 
	 @Column(name = "deposit")
	 private Double deposit;
	 
	 @Column(name = "deposit_rate")
	 private Double depositRate;
	 
	 @Column(name = "subsequent_number")
	 private int subsequentNumber;
	 
	 @Column(name = "subsequent")
	 private Double subsequent;

	 @Column(name = "owner")
	 private String owner;
	 
	@Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "created", nullable = false)
	 private Date created;
	    
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "notification_due", nullable = false)
	 private Date notificationDue;
	
	 @Temporal(TemporalType.TIMESTAMP)
	 @Column(name = "due", nullable = false)
	 private Date due;

	 @Column(name = "paid")
	 private Double paid;
	 
	 @Column(name = "payable")
	 private Double payable;
	 
	 @Column(name = "ticket_revenue")
	 private Double ticketRevenue;
	 
	 @Column(name = "overdue")
	 private Boolean overdue;
	 
	 @Column(name = "gst")
	 private Double gst;
	 
	 @Column(name = "total_before_gst")
	 private Double totalBeforeGst;  
	 
	 @Column(name = "next_payment")
	 private Double nextPayment;
	 
	 @Column(name = "next_invoice")
	 private String nextInvoice;	

	 @Column(name = "invoice_due")
	 private Boolean invoice_due;	

	 @OneToOne
     @JsonIgnore
     private Event event;
     
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.REMOVE)
	private Set<Payment> payments = new HashSet<Payment>();
	 
     public PaymentPlan() {
 		//super();
 	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public int getSubsequentNumber() {
		return subsequentNumber;
	}

	public void setSubsequentNumber(int subsequentNumber) {
		this.subsequentNumber = subsequentNumber;
	}

	public Double getSubsequent() {
		return subsequent;
	}

	public void setSubsequent(Double subsequent) {
		this.subsequent = subsequent;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getDue() {
		return due;
	}

	public void setDue(Date due) {
		this.due = due;
	}

	public Double getPaid() {
		return paid;
	}

	public void setPaid(Double paid) {
		this.paid = paid;
	}

	public Double getPayable() {
		return payable;
	}

	public void setPayable(Double payable) {
		this.payable = payable;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Long getId() {
		return id;
	}

	public Date getNotificationDue() {
		return notificationDue;
	}

	public void setNotificationDue(Date notificationDue) {
		this.notificationDue = notificationDue;
	}   
	

    public Double getTicketRevenue() {
		return ticketRevenue;
	}

	public void setTicketRevenue(Double ticketRevenue) {
		this.ticketRevenue = ticketRevenue;
	}

	public Boolean getOverdue() {
		return overdue;
	}

	public void setOverdue(Boolean overdue) {
		this.overdue = overdue;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getTotalBeforeGst() {
		return totalBeforeGst;
	}

	public void setTotalBeforeGst(Double totalBeforeGst) {
		this.totalBeforeGst = totalBeforeGst;
	}

	public Double getDepositRate() {
		return depositRate;
	}

	public void setDepositRate(Double depositRate) {
		this.depositRate = depositRate;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Double getNextPayment() {
		return nextPayment;
	}

	public void setNextPayment(Double nextPayment) {
		this.nextPayment = nextPayment;
	}

	public String getNextInvoice() {
		return nextInvoice;
	}

	public void setNextInvoice(String nextInvoice) {
		this.nextInvoice = nextInvoice;
	}

	public Boolean getInvoice_due() {
		return invoice_due;
	}

	public void setInvoice_due(Boolean invoice_due) {
		this.invoice_due = invoice_due;
	}
	
}
