package com.catalina.taskmanager.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.catalina.taskmanager.dtos.RegisterRequest;
import com.catalina.taskmanager.services.AuthService;

@Controller
public class AuthController {
	
	private final AuthService authService;
	
	public AuthController(AuthService authService) {
		this.authService=authService;
	}
	
	@GetMapping("/")
    public String home() {
        return "taskManager";
    }
	
	@GetMapping("/taskManager")
	public String showTaskManagerPage() {
		return "taskManager";
	}
	
	@GetMapping("/login")
	public String showLoginPage(){
	    return "login"; 
	}
	
	
	@GetMapping("/register")
    public String showRegisterPage() {
        return "register"; 
    }
	
	@PostMapping("/register")
	public String registerUser(@ModelAttribute RegisterRequest registerRequest,RedirectAttributes redirectAttributes) throws UnsupportedEncodingException{
		try {
			authService.register(registerRequest);
			redirectAttributes.addFlashAttribute("email",registerRequest.getEmail());
			return "redirect:/register";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error",e.getMessage());
			return "redirect:/register";
		}
	}
	@GetMapping("/verifyBeforeLogin")
	public String showVerifyBeforeLoginPage(@RequestParam String email) {
		authService.sendVerificationCode(email);
		return "verifyBeforeLogin";
	}
	
	@PostMapping("/verifyBeforeLogin")
	public String verifyUserBeforeLogin(@RequestParam String verificationCode,@RequestParam String email,RedirectAttributes redirectAttributes) {
		try{
			authService.verifyUser(verificationCode,email);
			redirectAttributes.addFlashAttribute("verified","true");
			return "redirect:/login";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error",e.getMessage());
			return "redirect:/verifyBeforeLogin";
		}
	}
	@GetMapping("/verify")
	public String showVerifyPage(Principal principal,Model model) {
		authService.sendVerificationCode(authService.getUser(principal).getEmail());
		String color=authService.getUser(principal).getBackgroundColor();
		model.addAttribute("backgroundColor",color);
		return "verify";
	}
	
	@PostMapping("/verify")
	public String verifyUser(@RequestParam String verificationCode,Principal principal,RedirectAttributes redirectAttributes) {
		try{
			authService.verifyUser(verificationCode,authService.getUser(principal).getEmail());
			return "redirect:/settings";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error",e.getMessage());
			return "redirect:/verify";
		}
	}
	
	@GetMapping("/resetPassword")
	public String showResetPasswordPage() {
		return "resetPassword";
	}
	
	@PostMapping("/resetPassword")
	public String sendVerificationCode(@RequestParam String email,RedirectAttributes redirectAttributes) {
		try {
			authService.sendVerificationCode(email);
			redirectAttributes.addFlashAttribute("email",email);
			return "redirect:/resetPassword";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error1",e.getMessage());
			return "redirect:/resetPassword";
		}
	}
	
	
	@PostMapping("/resetPassword/request")
	public String resetPassword(@RequestParam String email,@RequestParam String verificationCode,@RequestParam String newPassword,RedirectAttributes redirectAttributes) {
		try{
			authService.resetPassword(email,verificationCode,newPassword);
			redirectAttributes.addFlashAttribute("changedPassword","true");
			return "redirect:/login";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error2",e.getMessage());
			redirectAttributes.addFlashAttribute("email",email);
			return "redirect:/resetPassword";
		}
	}
}
