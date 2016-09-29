package application.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.domain.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

	Vendor findByEmail(String email);
	 
	@Query(
	        value = "SELECT * FROM Vendor", 
	        nativeQuery=true
	   )
 public Set<Vendor> getAllVendors();
	

	@Query(
	        value = "SELECT * FROM Vendor v where v.id = :vendorId", 
	        nativeQuery=true
	   )
 public Optional<Vendor> getVendorById(@Param("vendorId") long vendorId);
	
	
}
