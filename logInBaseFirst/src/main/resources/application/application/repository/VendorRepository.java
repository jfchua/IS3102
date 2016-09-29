package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.domain.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

	Vendor findByEmail(String email);
	 
}
