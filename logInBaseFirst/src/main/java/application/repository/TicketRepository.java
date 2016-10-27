package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.Square;
import application.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	
}
