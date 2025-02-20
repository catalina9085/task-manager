package com.catalina.taskmanager.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String email;
	private String password;
	
	private boolean enableNotifications;
	private boolean enableTimePriority;
	private boolean enableDeleteAfterCheck;
	private boolean verified;
	
	private String backgroundColor;
	private String verificationCode;
	
	private LocalDateTime codeExpiration;
	private LocalDateTime firstAuth;

	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> roles;
	
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<Task> tasks=new ArrayList<>();
	
	
	public UserEntity() {}
	
	public UserEntity(String email,String username,String password,LocalDateTime firstAuth) {
		this.email=email;
		this.password=password;
		this.username=username;
		this.firstAuth=firstAuth;
		this.backgroundColor="#CFD1D4";
		this.enableNotifications=true;
		this.enableTimePriority=true;
		this.roles=new HashSet<>(Arrays.asList("USER"));
	}
}
