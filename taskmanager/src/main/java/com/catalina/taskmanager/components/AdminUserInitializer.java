package com.catalina.taskmanager.components;

import java.time.LocalDateTime;
import java.util.Collections;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.UserRepository;

@Component
public class AdminUserInitializer implements CommandLineRunner{
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
	
	@Override
    public void run(String... args) throws Exception {
        if(userRepository.findByUsername("admin").isEmpty()) {
        	UserEntity adminUser = new UserEntity("admin@gmail.com","admin",passwordEncoder.encode("adminPassword"),LocalDateTime.now());
        	adminUser.setRoles(Collections.singleton("ADMIN"));
        	userRepository.save(adminUser);
        }
    }
}
