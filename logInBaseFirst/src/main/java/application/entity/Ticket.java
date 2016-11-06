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

@Entity
@Table(name = "ticket")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "ticketUUID", nullable = false)
	private String ticketUUID;

	@Column(name = "paymentId", nullable = false)
	private String paymentId;
	
	@Column(name = "redeemed", nullable = false)
	private boolean redeemed = false;

	public boolean isRedeemed() {
		return redeemed;
	}


	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	private Date start_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	private Date end_date;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "purchase_date", nullable = false)
	private Date purchase_date;
	
	private String ticketDetails;


	public String getTicketDetails() {
		return ticketDetails;
	}


	public void setTicketDetails(String ticketDetails) {
		this.ticketDetails = ticketDetails;
	}


	@ManyToOne
	private Category category;


	public String getTicketUUID() {
		return ticketUUID;
	}


	public void setTicketUUID(String ticketUUID) {
		this.ticketUUID = ticketUUID;
	}


	public String getPaymentId() {
		return paymentId;
	}


	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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


	public Date getPurchase_date() {
		return purchase_date;
	}


	public void setPurchase_date(Date purchase_date) {
		this.purchase_date = purchase_date;
	}


	public Category getCategory() {
		return category;
	}


	public void setCategory(Category category) {
		this.category = category;
	}


}