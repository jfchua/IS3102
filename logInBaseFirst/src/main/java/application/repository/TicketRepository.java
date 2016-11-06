package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Message;
import application.entity.Square;
import application.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query(
			value = "SELECT * FROM ticket m where m.ticketuuid = :id", 
			nativeQuery=true
			)
	public Ticket getTicketByCode(@Param("id") String id);
}
