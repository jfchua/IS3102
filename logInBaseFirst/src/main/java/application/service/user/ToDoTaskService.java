package application.service.user;

import java.util.Collection;
import java.util.Date;

import application.domain.ToDoTask;
import application.domain.User;
import application.exception.ToDoTaskNotFoundException;
import application.exception.UserNotFoundException;

public interface ToDoTaskService {


	public Collection<ToDoTask> getToDoList(User user) throws UserNotFoundException;

	public boolean addToDoTask(User user, String tsk, Date date) throws UserNotFoundException;


	public boolean deleteToDoTask(User user, long taskId) throws UserNotFoundException, ToDoTaskNotFoundException;


	//public boolean updateToDoTask(User user, long taskId, String newTask);





}
