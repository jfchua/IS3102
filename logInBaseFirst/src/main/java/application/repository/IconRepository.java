package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.domain.Icon;

public interface IconRepository extends JpaRepository<Icon, Long>{

}
