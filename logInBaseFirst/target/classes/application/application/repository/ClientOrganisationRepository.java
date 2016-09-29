package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.domain.ClientOrganisation;

public interface ClientOrganisationRepository extends JpaRepository<ClientOrganisation, Long> {

	
	 
}
