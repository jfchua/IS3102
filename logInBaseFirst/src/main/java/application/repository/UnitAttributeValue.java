package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.UnitAttributeValue;

public interface UnitAttributeValue extends JpaRepository<UnitAttributeValue, Long>{

}
