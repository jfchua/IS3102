package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Beacon;
import application.entity.Discount;

public interface BeaconRepository extends JpaRepository<Beacon, Long> {
	
	 @Query(
		        value = "SELECT * FROM beacon m where m.beaconuuid = :name", 
		        nativeQuery=true
		   )
	 public Discount getDiscountByCode(@Param("name") String name);
	 
}
