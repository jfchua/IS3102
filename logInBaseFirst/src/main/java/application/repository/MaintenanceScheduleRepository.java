package application.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.BookingAppl;
import application.entity.ClientOrganisation;
import application.entity.MaintenanceSchedule;
import application.entity.Unit;
import application.entity.User;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long> {
	
	  @Query(
	            value = "SELECT count(*) FROM schedule book WHERE book.unit_id = :unit AND ((book.start <= :start AND book.end >= :end)"
	            		+ "OR (book.start >= :start AND book.end <= :end) OR (book.start <= :start AND book.end <= :end AND book.end >= :start)"
	            		+ "OR (book.start >= :start AND book.end >= :end AND book.start <= :end))",
	            nativeQuery=true
	       )
	    public int getNumberOfMaintenanceSchedules(@Param("unit") Long unit, @Param("start") Date start, @Param("end") Date end);
	  
	
	  
	  @Query(
	            value = "SELECT * FROM schedule book WHERE book.unit_id = :unit AND ((book.start <= :start AND book.end >= :end)"
	            		+ "OR (book.start >= :start AND book.end <= :end) OR (book.start <= :start AND book.end <= :end AND book.end >= :start)"
	            		+ "OR (book.start >= :start AND book.end >= :end AND book.start <= :end))",
	            nativeQuery=true
	       )
	    public MaintenanceSchedule getMaintenanceScheduleEntity(@Param("unit") Long unit, @Param("start") Date start, @Param("end") Date end);
	  
	  
}
