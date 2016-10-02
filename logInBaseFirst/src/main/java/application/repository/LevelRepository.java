package application.repository;

import java.util.Set;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.domain.Building;
//import application.domain.Event;
import application.domain.Level;
//import application.domain.User;

public interface LevelRepository extends JpaRepository<Level, Long>{
	@Query(
	        value = "SELECT * FROM Level l where l.building_id = :buildingId", 
	        nativeQuery=true
	   )
 public Set<Level> getLevelsByBuilding(@Param("buildingId") long buildingId);
}
