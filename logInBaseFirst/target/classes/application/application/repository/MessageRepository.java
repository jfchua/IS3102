package application.repository;

import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

	//@Query("SELECT * FROM Message m where m.recipient = :recipient") 
	
	//public Collection<Message> getMessagesByRecipient(@Param("recipient") String recipient);
	
	 @Query(
		        value = "SELECT * FROM Message m where m.recipient = :recipient", 
		        nativeQuery=true
		   )
	 public Collection<Message> getMessagesByRecipient(@Param("recipient") String recipient);
	 
}
