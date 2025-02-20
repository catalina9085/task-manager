package com.catalina.taskmanager.services;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.catalina.taskmanager.dtos.AddTaskForUserRequest;
import com.catalina.taskmanager.dtos.EditTaskRequest;
import com.catalina.taskmanager.entities.Task;
import com.catalina.taskmanager.entities.TaskComparator;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.TaskRepository;
import com.catalina.taskmanager.repositories.UserRepository;

@Service
public class ManagerService {
	
	private UserRepository userRepository;
	private TaskRepository taskRepository;
	
	public ManagerService(UserRepository userRepository,TaskRepository taskRepository) {
		this.userRepository=userRepository;
		this.taskRepository=taskRepository;
	}
	
	public UserEntity getUser(Principal principal) {
		String username = principal.getName();
	    UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    return userEntity;
	}
	
	public List<UserEntity> getAllUsers(){
		return userRepository.findAll();
	}
	
	public List<Task> getTasksForUser(String username){
		UserEntity user=userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("user not found"));
		List<Task> tasks=taskRepository.findByUser(user);
		Iterator<Task> iterator = tasks.iterator();
		while (iterator.hasNext()) {
		    Task el = iterator.next();
		    if (user.isEnableDeleteAfterCheck() && el.isCompleted()) {
		        deleteTask(el.getId());
		        iterator.remove();
		    }
			else if(el.getDeadline()!=null) {
				Duration duration = Duration.between(LocalDateTime.now(), el.getDeadline());

                long daysLeft = duration.toDays();
                long hoursLeft = duration.toHours() % 24;
                long minutesLeft = duration.toMinutes() % 60;
                
                String timeLeft=String.format("%d days, %d hours, %d minutes",daysLeft,hoursLeft,minutesLeft);
                el.setTimeLeft(timeLeft);
			}
		}
		if(user.isEnableTimePriority())
			tasks.sort(new TaskComparator());
		return tasks;
	}
	
	public Task getTaskById(Long id) {
		return taskRepository.findById(id).orElseThrow(()->new RuntimeException("task not found"));
	}
	
	public void editTask(Long id,EditTaskRequest request) {
		Task task=taskRepository.findById(id).orElseThrow(()->new RuntimeException("task not found"));
		task.setCategory(request.getCategory());
		task.setDeadline(request.getDeadline());
		task.setTitle(request.getTitle());
		task.setDescription(request.getDescription());
		taskRepository.save(task);
	}
	
	public void deleteTask(Long id) {
		taskRepository.delete(taskRepository.findById(id).orElseThrow(()->new RuntimeException("task not found")));
	}
	
	public void addTaskForUser(String username,AddTaskForUserRequest request) {
		UserEntity user=userRepository.findByUsername(username).orElseThrow(()->new RuntimeException("user not found"));
		Task task=new Task(request.getTitle(),request.getDescription(),user);
		task.setDeadline(request.getDeadline());
		task.setCategory(request.getCategory());
		task.setCompleted(false);
		taskRepository.save(task);
	}
	
}
