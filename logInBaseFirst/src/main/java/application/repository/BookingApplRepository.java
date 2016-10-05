package application.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import application.domain.BookingAppl;
import application.domain.ClientOrganisation;
import application.domain.Unit;
import application.domain.User;

public interface BookingApplRepository extends JpaRepository<BookingAppl, Long> {
	
	  @Query(
	            value = "SELECT count(*) FROM booking_appl book WHERE book.unit_id = :unit AND ((book.event_start_date_time <= :start AND book.event_end_date_time >= :end)"
	            		+ "OR (book.event_start_date_time >= :start AND book.event_end_date_time <= :end) OR (book.event_start_date_time <= :start AND book.event_end_date_time <= :end AND book.event_end_date_time >= :start)"
	            		+ "OR (book.event_start_date_time >= :start AND book.event_end_date_time >= :end AND book.event_start_date_time <= :end))",
	            nativeQuery=true
	       )
	    public int getNumberOfBookings(@Param("unit") Long unit, @Param("start") Date start, @Param("end") Date end);
	  
	
	  
	  @Query(
	            value = "SELECT * FROM booking_appl book WHERE book.unit_id = :unit AND ((book.event_start_date_time <= :start AND book.event_end_date_time >= :end)"
	            		+ "OR (book.event_start_date_time >= :start AND book.event_end_date_time <= :end) OR (book.event_start_date_time <= :start AND book.event_end_date_time <= :end AND book.event_end_date_time >= :start)"
	            		+ "OR (book.event_start_date_time >= :start AND book.event_end_date_time >= :end AND book.event_start_date_time <= :end))",
	            nativeQuery=true
	       )
	    public BookingAppl getBookingEntity(@Param("unit") Long unit, @Param("start") Date start, @Param("end") Date end);
	  
	  
}
