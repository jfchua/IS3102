package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{


}
