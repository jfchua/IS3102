package application.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.domain.Area;
import application.domain.Level;
import application.domain.Unit;


public interface AreaRepository extends JpaRepository<Area, Long>{
	@Query(
	        value = "SELECT * FROM Area u where u.id = :areaId", 
	        nativeQuery=true
	   )
 public Optional<Area> getAreaById(@Param("areaId") long areaId);
	@Query(
	        value = "SELECT * FROM Area", 
	        nativeQuery=true
	   )
	public Set<Area> fetchAllAreas();
}
