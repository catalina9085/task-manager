package com.catalina.taskmanager.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository=userRepository;	
	}
	
	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException{
		Optional<UserEntity> user =userRepository.findByEmail(identifier);
		
		if(user.isEmpty()) user=userRepository.findByUsername(identifier);
				
		UserEntity foundUser=user.orElseThrow(()->new UsernameNotFoundException("The user \""+identifier +"\" does not exist!"));
		
		
		Set<GrantedAuthority> authorities = foundUser.getRoles().stream()
	            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
	            .collect(Collectors.toSet());
		
		return User.builder()
				.username(foundUser.getUsername())
				.password(foundUser.getPassword())
				.authorities(authorities) 
				.build();
	}

}
