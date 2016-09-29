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

public interface BuildingRepository extends JpaRepository<Building, Long>{
	@Query(
	        value = "SELECT * FROM Building", 
	        nativeQuery=true
	   )
	//public Set<Building> fetchAllBuildings();

	public Set<Building> fetchAllBuildings();
}
