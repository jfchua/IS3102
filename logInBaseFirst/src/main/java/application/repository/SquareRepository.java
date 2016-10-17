package application.repository;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import application.entity.Square;

public interface SquareRepository extends JpaRepository<Square, Long>{
	@Query(
	        value = "SELECT * FROM Square", 
	        nativeQuery=true
	   )

	public Set<Square> fetchAllSquares();
}