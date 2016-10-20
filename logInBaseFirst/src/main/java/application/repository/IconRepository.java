package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.Icon;

public interface IconRepository extends JpaRepository<Icon, Long>{

}
