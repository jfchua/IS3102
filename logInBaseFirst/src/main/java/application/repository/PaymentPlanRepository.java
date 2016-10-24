package application.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Building;
import application.entity.ClientOrganisation;
import application.entity.PaymentPlan;
import application.entity.Unit;
import application.entity.User;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
	@Query(
	        value = "SELECT * FROM Payment_plan", 
	        nativeQuery=true
	   )
	//public Set<Building> fetchAllBuildings();

	public Set<PaymentPlan> getAll();
	  
	  
	  
}
