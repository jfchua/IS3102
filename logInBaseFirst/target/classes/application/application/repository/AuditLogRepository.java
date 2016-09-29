package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.domain.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

	
	 
}
