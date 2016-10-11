package application.repository;

import java.util.Date;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import application.domain.PaymentPlan;
import application.domain.ClientOrganisation;
import application.domain.Unit;
import application.domain.User;

public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
	
	  
	  
	  
}
