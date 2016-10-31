package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
	
	 @Query(
		        value = "SELECT * FROM discount m where m.qrcode = :name", 
		        nativeQuery=true
		   )
	 public Discount getDiscountByCode(@Param("name") String name);

}
