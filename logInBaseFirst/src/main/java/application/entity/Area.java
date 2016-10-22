package application.entity;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
@Table(name = "area")
public class Area {


	
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id", nullable = false, updatable = false)
	    private Long id;
		
	    
		@OneToOne(fetch = FetchType.EAGER)
	    private Square square;
		
	    @Column(name = "areaName", nullable = false)
	    private String areaName;

	    @Column(name = "description", nullable = false)
	    private String description;
	   
	   
	    @OneToOne//(cascade=CascadeType.ALL)
	    private BookingAppl booking;


		public Long getId() {
			return id;
		}


		public Square getSquare() {
			return square;
		}


		public void setSquare(Square square) {
			this.square = square;
		}


		public String getAreaName() {
			return areaName;
		}


		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public BookingAppl getBooking() {
			return booking;
		}


		public void setBooking(BookingAppl booking) {
			this.booking = booking;
		}


		@Override
		public String toString() {
			return "Area [id=" + id + ", square=" + square + ", areaName=" + areaName + ", description=" + description
					+ ", booking=" + booking + "]";
		}

	  
	    

	
		
		
}