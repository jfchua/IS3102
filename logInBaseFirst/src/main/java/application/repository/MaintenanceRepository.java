package application.repository;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import application.domain.Maintenance;
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long>{
	@Query(
	        value = "SELECT * FROM Maintenance", 
	        nativeQuery=true
	   )

	public Set<Maintenance> fetchAllMaintenances();
}
