package application.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import application.domain.ToDoTask;

public interface ToDoTaskRepository extends JpaRepository<ToDoTask, Long> {

	
	 
}
