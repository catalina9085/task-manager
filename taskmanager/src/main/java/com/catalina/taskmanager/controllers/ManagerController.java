package com.catalina.taskmanager.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.catalina.taskmanager.dtos.AddTaskForUserRequest;
import com.catalina.taskmanager.dtos.EditTaskRequest;
import com.catalina.taskmanager.services.ManagerService;

@Controller
@RequestMapping("/manager")
public class ManagerController {
	
	private ManagerService managerService;
	
	public ManagerController(ManagerService managerService) {
		this.managerService=managerService;
	}
	
	@GetMapping
	public String showManagerPage(Model model,Principal principal) {
		model.addAttribute("backgroundColor",managerService.getUser(principal).getBackgroundColor());
		model.addAttribute("users", managerService.getAllUsers());
		return "manager";
	}
	
	@GetMapping("/showTasksForUser")
	public String showTasksForUser(@RequestParam String username,Model model,Principal principal) {
		model.addAttribute("tasks",managerService.getTasksForUser(username));
		model.addAttribute("backgroundColor",managerService.getUser(principal).getBackgroundColor());
		model.addAttribute("username", username);
		return "showTasksForUser";
	}
	
	@GetMapping("/showTasksForUser/edit")
	public String editTaskForUserPage(@RequestParam Long id,Model model,@RequestParam String username,Principal principal) {
		model.addAttribute("task", managerService.getTaskById(id));
		model.addAttribute("username", username);
		model.addAttribute("backgroundColor",managerService.getUser(principal).getBackgroundColor());
		return "editTaskForUser";
	}
	
	@PostMapping("/showTasksForUser/edit")
	public String showEditTaskForUserPage(@RequestParam Long id,@RequestParam String username,@ModelAttribute EditTaskRequest request, RedirectAttributes redirectAttributes) {
		managerService.editTask(id,request);
		redirectAttributes.addFlashAttribute("message", "Task successfuly updated!");
		redirectAttributes.addFlashAttribute("username",username);
		return "redirect:/manager/showTasksForUser?username="+username;
	}
	
	@PostMapping("/showTasksForUser/delete")
	public String deleteTaskForUser(@RequestParam Long id,@RequestParam String username,RedirectAttributes redirectAttributes) {
		managerService.deleteTask(id);
		redirectAttributes.addFlashAttribute("message", "Task successfuly deleted!");
		return "redirect:/manager/showTasksForUser?username="+username;
	}
	
	@GetMapping("/showTasksForUser/addTask")
	public String showAddTaskForUserPage(Principal principal,@RequestParam String username,Model model) {
		model.addAttribute("username", username);
		model.addAttribute("backgroundColor",managerService.getUser(principal).getBackgroundColor());
		return "addTaskForUser";
	}
	
	@PostMapping("/showTasksForUser/addTask")
	public String addTaskForUserPage(@RequestParam String username,@ModelAttribute AddTaskForUserRequest request) {
		managerService.addTaskForUser(username,request);
		return "redirect:/manager/showTasksForUser?username="+username;
	}
}
