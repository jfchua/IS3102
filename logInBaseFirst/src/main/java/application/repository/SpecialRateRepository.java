package application.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.SpecialRate;

public interface SpecialRateRepository extends JpaRepository<SpecialRate, Long> {

	
	
}
