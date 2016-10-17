package application.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.ToDoTask;

public interface ToDoTaskRepository extends JpaRepository<ToDoTask, Long> {

	
	 
}
