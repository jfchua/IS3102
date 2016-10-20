package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.ClientOrganisation;

public interface ClientOrganisationRepository extends JpaRepository<ClientOrganisation, Long> {


	 @Query(
		        value = "SELECT * FROM client_organisation m where m.organisation_name = :name", 
		        nativeQuery=true
		   )
	 public ClientOrganisation getClientOrgByName(@Param("name") String name);
	 
}
