package com.catalina.taskmanager.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AddUserRequest {
	private String username;
	private String password;
	private String confirmPassword;
	private String email;
	private List<String> roles;
}
