package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import application.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	
	 @Modifying
	 @Transactional
	 @Query(
		        value = "DELETE FROM audit_log where user_id = :id", 
		        nativeQuery=true
		   )
	 public void deleteAuditLogsOfUserId(@Param("id") Long id);
	 
}
