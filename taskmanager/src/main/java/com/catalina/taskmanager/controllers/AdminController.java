package com.catalina.taskmanager.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.catalina.taskmanager.dtos.AddUserRequest;
import com.catalina.taskmanager.services.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;
	
	public AdminController(AdminService adminService) {
		this.adminService=adminService;
	}
	
	@GetMapping
	public String showAdminPage(Principal principal,Model model) {
		model.addAttribute("backgroundColor",adminService.getUser(principal).getBackgroundColor());
		model.addAttribute("users", adminService.getAllUsers(principal));
		return "admin";
	}
	
	@PostMapping("/setRoles")
	public String setRoles(@RequestParam(name="roles",required=false)List<String> roles,@RequestParam Long id) {
		adminService.setRoles(id,roles);
		return "redirect:/admin";
	}
	
	@PostMapping("/delete")
	public String deleteUser(@RequestParam Long id) {
		adminService.deleteUser(id);
		return "redirect:/admin";
	}
	
	@GetMapping("/addUser")
	public String showAddUserPage(Model model,Principal principal) {
		model.addAttribute("backgroundColor",adminService.getUser(principal).getBackgroundColor());
		return "addUser";
	}
	
	@PostMapping("/addUser")
	public String addUser(@ModelAttribute AddUserRequest request,RedirectAttributes redirectAttributes) {
		try {
			adminService.addUser(request);
			redirectAttributes.addFlashAttribute("message","The user has been successfully created!");
			return "redirect:/admin";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error",e.getMessage());
			return "redirect:/admin/addUser";
		}
	}
	
	
}
