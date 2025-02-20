package com.catalina.taskmanager.controllers;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.catalina.taskmanager.dtos.ChangePasswordRequest;
import com.catalina.taskmanager.services.SettingsService;

@Controller
@RequestMapping("/settings")
public class SettingsController {
	
	private final SettingsService settingsService;
	
	public SettingsController(SettingsService settingsService) {
		this.settingsService=settingsService;
	}
	
	
	@GetMapping
	public String showSettings(Principal principal,Model model) {
		model.addAttribute("verified",settingsService.getUser(principal).isVerified());
		String color=settingsService.getUser(principal).getBackgroundColor();
		model.addAttribute("backgroundColor",color);
		return "settings";
	}
	
	@PostMapping("/turnOffNotif")
	public String turnOffNotif(Principal principal,RedirectAttributes redirectAttributes) {
		
		settingsService.turnOffNotif(principal);
		String message="The notifications are now turned off.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@PostMapping("/turnOnNotif")
	public String turnOnNotif(Principal principal,RedirectAttributes redirectAttributes) {
		
		settingsService.turnOnNotif(principal);
		String message="The notifications are now turned on.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@PostMapping("/enableTimePriority")
	public String enableTimePriority(Principal principal,RedirectAttributes redirectAttributes) {
		settingsService.enableTimePriority(principal);
		String message="Deadline priority enabled.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@PostMapping("/disableTimePriority")
	public String disableTimePriority(Principal principal,RedirectAttributes redirectAttributes) {
		settingsService.disableTimePriority(principal);
		String message="Deadline priority disabled.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@PostMapping("/enableDeleteAfterCheck")
	public String enableDeleteAfterCheck(Principal principal,RedirectAttributes redirectAttributes) {
		settingsService.enableDeleteAfterCheck(principal);
		String message="Your tasks will be deleted when the checkbox is pressed.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@PostMapping("/disableDeleteAfterCheck")
	public String disableDeleteAfterCheck(Principal principal,RedirectAttributes redirectAttributes) {
		settingsService.disableDeleteAfterCheck(principal);
		String message="Your tasks won't be deleted when the checkbox is pressed.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@PostMapping("/color")
	public String changeBackgroundColor(@RequestParam String backgroundColor,Principal principal,RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		settingsService.changeBackgroundColor(principal,backgroundColor);
		String message ="Your background color has been changed.";
		redirectAttributes.addFlashAttribute("message",message);
		return "redirect:/settings";
	}
	
	@GetMapping("/changePassword")
	public String showChangePasswordPage(Model model,Principal principal) {
		String color=settingsService.getUser(principal).getBackgroundColor();
		model.addAttribute("backgroundColor",color);
		return "changePassword";
	}
	
	@PostMapping("/changePassword")
	public String changePassword(Principal principal,@ModelAttribute ChangePasswordRequest request,RedirectAttributes redirectAttributes) {
		try {
			settingsService.changePassword(principal,request);
			String message="Your password has been successfully changed.";
			redirectAttributes.addFlashAttribute("message",message);
			return "redirect:/settings";
		}catch(RuntimeException e) {
			redirectAttributes.addFlashAttribute("error",e.getMessage());
			return "redirect:/settings/changePassword";
		}
	}
}
