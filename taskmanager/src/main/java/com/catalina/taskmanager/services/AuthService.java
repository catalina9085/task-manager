package com.catalina.taskmanager.services;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.catalina.taskmanager.dtos.RegisterRequest;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;
    
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,MailService mailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.mailService=mailService;
	}
	
	public UserEntity getUser(Principal principal) {
		String username = principal.getName();
	    UserEntity userEntity = userRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    return userEntity;
	}
	
	public void register(RegisterRequest registerRequest) throws UnsupportedEncodingException {
		String username=registerRequest.getUsername();
		String email=registerRequest.getEmail();
		String password=registerRequest.getPassword();
		String confirmPassword=registerRequest.getConfirmPassword();
		
		if(userRepository.existsByUsername(username)) {
			throw new RuntimeException("This username is taken!");
		}
		if(userRepository.existsByEmail(email)) {
			throw new RuntimeException("This email is already associated with an account!");
		}
		
		if(!password.equals(confirmPassword))
			throw new RuntimeException("The passwords do not match.");
		
		UserEntity user=new UserEntity(email,username,passwordEncoder.encode(password),LocalDateTime.now());
		userRepository.save(user);
		
	}

	
	public void sendVerificationCode(String email) {
		
		UserEntity user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found!"));
		user.setVerificationCode(mailService.generateVerificationCode());
		user.setCodeExpiration(LocalDateTime.now().plusMinutes(15));
		userRepository.save(user);
		mailService.sendVerificationCode(user.getVerificationCode(),user.getEmail());	
	}
	
	public void verifyUser(String verificationCode,String email) {
		
		UserEntity user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("user not found!"));
		if(user.getCodeExpiration().isBefore(LocalDateTime.now())) throw new RuntimeException("The time expired!");
		if(!verificationCode.equals(user.getVerificationCode())) throw new RuntimeException("Incorrect code!");
		
		user.setVerified(true);
		userRepository.save(user);
	}
	
	public void resetPassword(String email,String verificationCode,String newPassword) {
		 UserEntity user = userRepository.findByEmail(email)
		            .orElseThrow(() -> new RuntimeException("User not found"));
		 verifyUser(verificationCode,email);
		 user.setPassword(passwordEncoder.encode(newPassword));
		 userRepository.save(user);
	}
}
