package com.catalina.taskmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.catalina.taskmanager.dtos.TaskRequest;
import com.catalina.taskmanager.entities.Task;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.TaskRepository;
import com.catalina.taskmanager.repositories.UserRepository;
import com.catalina.taskmanager.services.TaskService;

@Controller
@RequestMapping("/main")
public class TaskController {

	private final TaskService taskService;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	
	public TaskController(TaskService taskService,TaskRepository taskRepository,UserRepository userRepository) {
		this.taskService=taskService;
		this.taskRepository=taskRepository;
		this.userRepository=userRepository;
	}
	
	public UserEntity getUser(Principal principal) {
		String username = principal.getName();
	    UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    return userEntity;
	}
	
	@GetMapping
	public String showMainPage(Model model,Principal principal) {
		model.addAttribute("tasks" ,taskService.getTasks(getUser(principal)));
		model.addAttribute("backgroundColor",getUser(principal).getBackgroundColor());
		model.addAttribute("roles",getUser(principal).getRoles());
		return "main";
	}
	
	@GetMapping("/add")
	public String showAddPage(Model model,Principal principal) {
		String color=getUser(principal).getBackgroundColor();
		model.addAttribute("backgroundColor",color);
		return "addTask";
	}
	
	@PostMapping("/add")
	public String addTask(@ModelAttribute TaskRequest taskRequest,Principal principal) {
		taskService.addTask(taskRequest, getUser(principal));
		return "redirect:/main";
	}
	
	@GetMapping("/update/{id}")
	public String showUpdateTaskPage(@PathVariable("id") Long id,Model model,Principal principal) {
		Task task=taskRepository.findById(id).orElseThrow(()->new RuntimeException("task not found"));
		model.addAttribute(task);
		String color=getUser(principal).getBackgroundColor();
		model.addAttribute("backgroundColor",color);
		return "editTask";
	}
	
	@PostMapping("/update/{id}")
	public String editTask(@PathVariable("id") Long id,@ModelAttribute TaskRequest taskRequest,Principal principal){
		  
		taskService.updateTask(taskRequest,id,getUser(principal));
		return "redirect:/main";
	}
	
	@GetMapping("/category")
	public String showTasksByCategory(@RequestParam String category,Principal principal,Model model) {
	    List<Task> tasks=taskService.getTasksByCategory(getUser(principal),category);
		model.addAttribute("tasks" ,tasks);
		String color=getUser(principal).getBackgroundColor();
		model.addAttribute("backgroundColor",color);
		return "category";
	}
	
	@PostMapping("/toggle/{id}")
	public String toggleCompletedTask(@PathVariable("id") Long id,Principal principal) {
		taskService.toggleTask(id,getUser(principal));
		return "redirect:/main";
	}
	
	
	@PostMapping("/delete/{id}")
	public String deleteTask(@PathVariable Long id) {
		taskService.deleteTask(id);
		return "redirect:/main";
	}

}
