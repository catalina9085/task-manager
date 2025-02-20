package com.catalina.taskmanager.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddTaskForUserRequest {
	private String title;
	private String description;
	private LocalDateTime deadline;
	private String category;
}
