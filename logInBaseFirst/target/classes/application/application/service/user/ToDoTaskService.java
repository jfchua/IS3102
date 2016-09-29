package application.service.user;

import java.util.Collection;
import java.util.Date;

import application.domain.ToDoTask;
import application.domain.User;

public interface ToDoTaskService {


	public Collection<ToDoTask> getToDoList(User user);

	public boolean addToDoTask(User user, String tsk, Date date);


	public boolean deleteToDoTask(User user, long taskId);


	//public boolean updateToDoTask(User user, long taskId, String newTask);





}
