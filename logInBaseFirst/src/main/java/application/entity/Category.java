package application.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id", nullable = false, updatable = false)
	    private Long id;
	    
	    @Column(name = "category_name", nullable = false)
	    private String categoryName;
	    
	    @Column(name = "price", nullable = false)
	    private double price;
	    
	    @Column(name = "numOfTickets", nullable = false)
	    private int numOfTickets;
	    
	    @ManyToOne
	    private Event event;
	    
	    @OneToMany(cascade={CascadeType.ALL}, mappedBy="category")
		@Column(nullable = true)
		private Set<Ticket> tickets = new HashSet<Ticket>();

		public Set<Ticket> getTickets() {
			return tickets;
		}

		public void setTickets(Set<Ticket> tickets) {
			this.tickets = tickets;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getCategoryName() {
			return categoryName;
		}

		public void setCategoryName(String categoryName) {
			this.categoryName = categoryName;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		public int getNumOfTickets() {
			return numOfTickets;
		}

		public void setNumOfTickets(int numOfTickets) {
			this.numOfTickets = numOfTickets;
		}

		public Event getEvent() {
			return event;
		}

		public void setEvent(Event event) {
			this.event = event;
		}
	    public void addTicket(Ticket t){
	    	this.tickets.add(t);
	    }
	    
}