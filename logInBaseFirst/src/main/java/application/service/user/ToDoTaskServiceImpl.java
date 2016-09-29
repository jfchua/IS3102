package application.service.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import application.domain.ToDoTask;
import application.domain.User;
import application.repository.PasswordResetTokenRepository;
import application.repository.ToDoTaskRepository;
import application.repository.UserRepository;

@Service
public class ToDoTaskServiceImpl implements ToDoTaskService {

	
	private final UserRepository userRepository;
	private final ToDoTaskRepository toDoTaskRepository;
	// private final MessageRepository messageRepository = null;

	@Autowired
	public ToDoTaskServiceImpl(UserRepository userRepository, ToDoTaskRepository toDoTaskRepository ) {
		this.userRepository = userRepository;
		this.toDoTaskRepository  = toDoTaskRepository;
	}
	
	public Collection<ToDoTask> getToDoList(User user) {
		Collection<ToDoTask> t = user.getToDoList();
		System.out.println("getting todo task w size" + t.size());
		return t;	
	}
    @Transactional
	public boolean addToDoTask(User user, String tsk, Date date) {
		try{
			ToDoTask t = new ToDoTask();
			t.setTask(tsk);
			
			Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.add(Calendar.DATE, 1); //minus number would decrement the days	
			t.setDate(cal.getTime());
			user.addTask(t);
			toDoTaskRepository.save(t);
			userRepository.save(user);
			System.out.println("Added todo task " + tsk + " to user: " + user.getEmail());
		}
		catch (Exception e){
			return false;
		}
		return true;
	}	


	public boolean deleteToDoTask(User user, long taskId) {
		try{		
			user.deleteToDoTask(taskId);
			userRepository.save(user);
			toDoTaskRepository.delete(taskId);
		}
		catch (Exception e){
			return false;
		}
		return true;
	}
	

	/*public boolean updateToDoTask(User user, long taskId, String newTask) {

		try{
			for ( ToDoTask t : user.getToDoList()){
				if ( t.getId() == taskId){
					ArrayList<ToDoTask> temp = (ArrayList<ToDoTask>)user.getToDoList();
					int currentIndex = temp.indexOf(t);
					ToDoTask tempTask = new ToDoTask();
					tempTask.setTask(newTask);
					temp.add(currentIndex, tempTask);
					temp.remove(t);
					toDoTaskRepository.delete(t);
					toDoTaskRepository.save(tempTask);
					user.setToDoList(temp);
					userRepository.save(user);
					break;
				}
			}
		}
		catch (Exception e){
			return false;
		}
		return true;
	}*/


}
