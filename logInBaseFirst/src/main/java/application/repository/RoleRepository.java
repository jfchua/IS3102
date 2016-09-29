package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import application.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(
			value = "SELECT * FROM Role m where m.name = :name", 
			nativeQuery=true
		   )
	public Role getRoleByName(@Param("name") String name);
}
