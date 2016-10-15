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
import application.exception.ToDoTaskNotFoundException;
import application.exception.UserNotFoundException;
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

	public Collection<ToDoTask> getToDoList(User user) throws UserNotFoundException {
		if ( user == null ){
			throw new UserNotFoundException("User was not found");
		}
		Collection<ToDoTask> t = user.getToDoList();
		System.out.println("getting todo task w size" + t.size());
		return t;	
	}

	@Transactional
	public boolean addToDoTask(User user, String tsk, Date date) throws UserNotFoundException {
		if ( user == null ){
			throw new UserNotFoundException("User was not found");
		}
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


	public boolean deleteToDoTask(User user, long taskId) throws UserNotFoundException, ToDoTaskNotFoundException {
		if ( user == null){
			throw new UserNotFoundException("User was not found");
		}
		try{
			System.out.println("todotaskisnull getting task with id " + taskId);
			if ( toDoTaskRepository.findOne(taskId) == null){
				System.out.println("todotaskisnull");
				throw new ToDoTaskNotFoundException("To do task of id " + taskId + " was not found");
			}
		}
		catch ( Exception e){
			throw new ToDoTaskNotFoundException("To do task of id " + taskId + " was not found"); 
		}
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
