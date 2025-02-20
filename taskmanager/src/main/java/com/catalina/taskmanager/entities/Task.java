package com.catalina.taskmanager.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	@CreationTimestamp
	private LocalDateTime createdAt;

	private LocalDateTime deadline;
	private String timeLeft;
	private String description;
	private String category;
	private boolean notified;
	
	private boolean completed;
	
	@ManyToOne
	@JoinColumn(name="user_id",nullable=false)
	private UserEntity user;
	
	public Task() {}
	public Task(String title, String description,UserEntity user) {
        this.title = title;
        this.description = description;
        this.user = user;
    }
	
	
	
	
}
