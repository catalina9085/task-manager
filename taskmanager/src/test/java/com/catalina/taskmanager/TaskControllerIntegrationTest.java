package com.catalina.taskmanager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.catalina.taskmanager.entities.UserEntity;
import com.catalina.taskmanager.repositories.TaskRepository;
import com.catalina.taskmanager.repositories.UserRepository;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TaskControllerIntegrationTest {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired 
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		UserEntity user=new UserEntity();
		user.setUsername("cata");
		user.setBackgroundColor("red");//daca ramane null,nu va fi considerat a face parte din model
		userRepository.deleteAll();
		userRepository.save(user);
		taskRepository.deleteAll();
	}
	
	@Test
	@WithMockUser(username="cata")
	public void showMainPageTest() throws Exception {
		mockMvc.perform(get("/main"))
			.andExpect(status().isOk())
			.andExpect(view().name("main"))
			.andExpect(model().attributeExists("tasks"))
			.andExpect(model().attributeExists("backgroundColor"));
	}
	
	@Test
	@WithMockUser(username="cata")
	public void showAddPageTest() throws Exception {
		mockMvc.perform(get("/main/add"))
		.andExpect(status().isOk())
		.andExpect(view().name("addTask"))
		.andExpect(model().attributeExists("backgroundColor"));
	}
	
	@Test
	@WithMockUser(username="cata")
	public void addTaskTest() throws Exception {
		mockMvc.perform(post("/main/add")
				.param("title","homework")
				.param("description","math")
				.param("category","school")
				.param("deadline","2024-12-12T10:10"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/main"));
		
	}
	
	
	@Test
	@WithMockUser(username="cata")
	public void updateTaskTest() throws Exception {
		mockMvc.perform(post("/main/add")
				.param("title","homework")
				.param("description","math")
				.param("category","school")
				.param("deadline","2024-12-12T10:10"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/main"));
		
		Long id=taskRepository.findAll().get(0).getId();
		
		mockMvc.perform(post("/main/update/"+id)
				.param("title","homework2")
				.param("description","math2")
				.param("category","school2"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/main"));
		
		assertTrue(taskRepository.findAll().get(0).getTitle().equals("homework2"));	
	}
	
	
}
