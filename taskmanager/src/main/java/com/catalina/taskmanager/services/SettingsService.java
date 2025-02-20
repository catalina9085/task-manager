package com.catalina.taskmanager.services;

import java.security.Principal;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.catalina.taskmanager.dtos.ChangePasswordRequest;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.UserRepository;

@Service
public class SettingsService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public SettingsService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.userRepository=userRepository;
		this.passwordEncoder=passwordEncoder;
	}
	
	public UserEntity getUser(Principal principal) {
		String username = principal.getName();
	    UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    return userEntity;
	}
	
	public void turnOffNotif(Principal principal) {
		UserEntity user=getUser(principal);
		user.setEnableNotifications(false);
		userRepository.save(user);
	}
	
	public void turnOnNotif(Principal principal) {
		UserEntity user=getUser(principal);
		user.setEnableNotifications(true);
		userRepository.save(user);
	}
	
	public void enableTimePriority(Principal principal) {
		UserEntity user=getUser(principal);
		user.setEnableTimePriority(true);
		userRepository.save(user);
	}
	
	public void disableTimePriority(Principal principal) {
		UserEntity user=getUser(principal);
		user.setEnableTimePriority(false);
		userRepository.save(user);
	}
	
	public void enableDeleteAfterCheck(Principal principal) {
		UserEntity user=getUser(principal);
		user.setEnableDeleteAfterCheck(true);
		userRepository.save(user);
	}
	
	public void disableDeleteAfterCheck(Principal principal) {
		UserEntity user=getUser(principal);
		user.setEnableDeleteAfterCheck(false);
		userRepository.save(user);
	}
	
	public void changeBackgroundColor(Principal principal,String color) {
		UserEntity user=getUser(principal);
		user.setBackgroundColor(color);
		userRepository.save(user);
	}
	
	public void changePassword(Principal principal,ChangePasswordRequest request) {
		UserEntity user=getUser(principal);
		if(!(passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())))
			throw new RuntimeException("Incorrect current password!");
		if(!(request.getNewPassword().equals(request.getConfirmPassword())))
			throw new RuntimeException("New password and confirm password do not match.");
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		userRepository.save(user);
	}
}
