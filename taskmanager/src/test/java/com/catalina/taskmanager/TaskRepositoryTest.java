package com.catalina.taskmanager;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.catalina.taskmanager.entities.Task;
import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.TaskRepository;
import com.catalina.taskmanager.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test") 
public class TaskRepositoryTest {
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void cleanDataBase() {
		taskRepository.deleteAll();
	}
	
	@Test
	public void saveTaskTest() {
		UserEntity user=new UserEntity();
		user.setEmail("ana@yahoo.com");
		user.setPassword("ana");
		user.setUsername("ana");
		userRepository.save(user);
		
		assertTrue(userRepository.existsByUsername("ana"));
        assertTrue(userRepository.existsByEmail("ana@yahoo.com"));
        
		Task task=new Task("aaa","bbb",user);
		taskRepository.save(task);
		
		List<Task> t=taskRepository.findByUser(user);
		
		assertTrue(t.size()==1);
		assertTrue(taskRepository.existsByTitle("aaa"));
		
	}
	
	
	

}
