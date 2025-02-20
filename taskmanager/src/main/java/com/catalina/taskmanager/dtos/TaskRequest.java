package com.catalina.taskmanager.dtos;

import java.time.LocalDateTime;

import com.catalina.taskmanager.entities.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskRequest {
	private String title;
	private String description;
	private LocalDateTime deadline;
	private String category;
	private UserEntity userEntity;
}
