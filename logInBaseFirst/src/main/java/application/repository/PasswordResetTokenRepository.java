package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.entity.Message;
import application.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>, JpaSpecificationExecutor<PasswordResetToken> {



	@Query(
			value = "SELECT * FROM password_reset_token m where m.token = :token", 
			nativeQuery=true
		   )
	public PasswordResetToken getTokenByString(@Param("token") String token);

}
