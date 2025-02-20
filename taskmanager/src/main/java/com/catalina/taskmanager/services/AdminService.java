package com.catalina.taskmanager.services;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.catalina.taskmanager.dtos.AddUserRequest;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.UserRepository;

@Service
public class AdminService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    
	public AdminService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder=passwordEncoder;
	}
	
	public UserEntity getUser(Principal principal) {
		String username = principal.getName();
	    UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    return userEntity;
	}
	
	public List<UserEntity> getAllUsers(Principal principal){
		List<UserEntity> list=userRepository.findAll();
		list.remove(getUser(principal));
		return list;
		
	}
	
	public void setRoles(Long id,List<String> roles) {
		UserEntity user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
		if(roles==null) {
			user.setRoles(new HashSet<>(Arrays.asList("USER")));
		}
		else{
			user.setRoles(new HashSet<>(roles));
		}
		userRepository.save(user);
	}
	
	public void deleteUser(Long id) {
		UserEntity user=userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
		userRepository.delete(user);
	}
	
	public void addUser(AddUserRequest request) {
		if(userRepository.existsByUsername(request.getUsername()))
			throw new RuntimeException("This username is taken!");
		
		if(userRepository.existsByEmail(request.getEmail()))
			throw new RuntimeException("This email is already used!");
		
		if(!(request.getPassword().equals(request.getConfirmPassword())))
			throw new RuntimeException("The passwords do not match!");
		
		UserEntity user=new UserEntity(request.getEmail(),request.getUsername(),passwordEncoder.encode(request.getPassword()),LocalDateTime.now());
		if(request.getRoles()!=null) user.setRoles(new HashSet<>(request.getRoles()));
		
		userRepository.save(user);
		
	}
}
