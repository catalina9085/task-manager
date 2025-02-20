package com.catalina.taskmanager.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.catalina.taskmanager.entities.Task;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.TaskRepository;

@Service
public class MailService {

	private JavaMailSender mailSender;
	private TaskRepository taskRepository;
	
	public MailService(JavaMailSender mailSender,TaskRepository taskRepository) {
		this.mailSender=mailSender;	
		this.taskRepository=taskRepository;
	}
	
	public void sendEmail(String to,String subject,String body) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		message.setFrom("catalina_ionela7@yahoo.com");
		mailSender.send(message);
	}
	
	public void sendVerificationCode(String code,String to) {
		String body="This is your verification code: "+code;
		sendEmail(to,"Verification code",body);
	}
	
	public String generateVerificationCode() {
		return UUID.randomUUID().toString().substring(0,6);
	}
	
	@Scheduled(cron="0 * * * * ?")
	public void checkTasksDeadline() {
		List<Task> tasks=taskRepository.findAll();
		
		for(Task task:tasks) {
			if(task.isNotified()==true || task.getDeadline()==null || !task.getUser().isEnableNotifications()) continue;
			
			Duration d=Duration.between(LocalDateTime.now(),task.getDeadline());
			long days=d.toDays();
			long minLeft=d.toHours()*60+d.toMinutes();
			//trimitem notificare daca task-ul este due in mai putin de o ora
			if(days==0 && minLeft<=60)
				sendTaskDeadlineNotification(task,task.getUser(),minLeft);
		}
		
		
	}
	
	public void sendTaskDeadlineNotification(Task task,UserEntity user,Long minLeft) {
		task.setNotified(true);
		String body=String.format("We are informing you that the following task is due in %d minutes.\nTitle:%s\nDescription:%s",minLeft,task.getTitle(),task.getDescription());
		sendEmail(user.getEmail(),"Task deadline",body);
		taskRepository.save(task);
	}
}
