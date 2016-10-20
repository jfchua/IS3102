package application.repository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Building;
import application.entity.Level;
import application.entity.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long>{

	@Query(
	        value = "SELECT * FROM Unit", 
	        nativeQuery=true
	   )
	public Set<Unit> fetchAllUnits();
	
	@Query(
	        value = "SELECT * FROM Unit u where u.id = :unitId", 
	        nativeQuery=true
	   )
 public Optional<Unit> getUnitById(@Param("unitId") long unitId);
/*	
	@Query(
	        value = "SELECT u FROM Unit u where u NOT IN (SELECT u from Event e WHERE e.event_start_date > start AND e.event_end_date < end)",
	        		
	        nativeQuery=true
	   )
 public Set<Unit> getUnitsByDates(@Param("start") Date start, @Param ("end") Date end);
	*/
}
