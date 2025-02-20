package com.catalina.taskmanager.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.catalina.taskmanager.dtos.TaskRequest;
import com.catalina.taskmanager.entities.Task;
import com.catalina.taskmanager.entities.TaskComparator;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.TaskRepository;



@Service
public class TaskService {

	private final TaskRepository taskRepository;
	
	public TaskService(TaskRepository taskRepository) {
		this.taskRepository=taskRepository;
	}
	
	public String addTask(TaskRequest taskRequest,UserEntity userEntity) {
			Task task=new Task(taskRequest.getTitle(),taskRequest.getDescription(),userEntity);
			task.setDeadline(taskRequest.getDeadline());
			task.setCategory(taskRequest.getCategory());
			task.setCompleted(false);
			taskRepository.save(task);
			return "success";
	}
	
	public List<Task> getTasks(UserEntity user){
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
	
	public List<Task> getTasksByCategory(UserEntity user,String category){
		List<Task> tasks=getTasks(user).stream().filter(task->task.getCategory().equals(category)).collect(Collectors.toList());
		return tasks;
	}
	
	public String deleteTask(Long id) {
		taskRepository.deleteById(id);
		return "success";
	}
	
	public void toggleTask(Long id,UserEntity user) {
		Task task=taskRepository.findById(id).orElseThrow(()->new RuntimeException("task not found"));
		task.setCompleted(!task.isCompleted());
		taskRepository.save(task);
	}
	
	public String updateTask(TaskRequest taskRequest,Long id,UserEntity user) {
		Task task=new Task(taskRequest.getTitle(),taskRequest.getDescription(),user);
		task.setDeadline(taskRequest.getDeadline());
		task.setCategory(taskRequest.getCategory());
		task.setId(id);
		taskRepository.save(task);
		return "success";
	}
	
	
}
