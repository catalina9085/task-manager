package com.catalina.taskmanager.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EditTaskRequest {
	private String description;
	private String title;
	private LocalDateTime deadline;
	private String category;
}
