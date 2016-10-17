package application.repository;

import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

	//@Query("SELECT * FROM Message m where m.recipient = :recipient") 
	
	//public Collection<Message> getMessagesByRecipient(@Param("recipient") String recipient);
	
	 @Query(
		        value = "SELECT * FROM Message m where m.recipient_id = :recipient", 
		        nativeQuery=true
		   )
	 public Set<Message> getMessagesByRecipient(@Param("recipient") String recipient);
	 
	 
	 @Query(
		        value = "SELECT * FROM Message m where m.id = :id", 
		        nativeQuery=true
		   )
	 public Message getMsgById(@Param("id") Long id);
	 
	 
}
