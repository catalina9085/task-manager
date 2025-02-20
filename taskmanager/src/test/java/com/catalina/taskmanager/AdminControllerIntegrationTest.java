package com.catalina.taskmanager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
import com.catalina.taskmanager.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdminControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	private Long id;
	
	@BeforeEach
	public void setup() {
		userRepository.deleteAll();
		UserEntity user=new UserEntity("user@gmail.com","user","user",LocalDateTime.now());
		user.setRoles(new HashSet<>(Arrays.asList("ADMIN")));
		userRepository.save(user);
		this.id=user.getId();
	}
	
	@Test
	@WithMockUser(roles="ADMIN")
	public void showAdminPageTest() throws Exception {
		mockMvc.perform(get("/admin"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("users"))
		.andExpect(view().name("admin"));
	}
	
	@Test
	@WithMockUser(roles="ADMIN")
	public void setUserRolesTest() throws Exception {
		mockMvc.perform(post("/admin/setRoles")
				.param("id",id.toString())
				.param("roles","ADMIN","MANAGER"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/admin"));
		
		List<String> roles=new ArrayList<>(Arrays.asList("ADMIN","MANAGER"));
		assertTrue(userRepository.findAll().get(0).getRoles().containsAll(roles));
	}
	
	@Test
	@WithMockUser(roles="ADMIN")
	public void addAndDeleteUserTest() throws Exception {
		mockMvc.perform(post("/admin/addUser")
				.param("username", "cata")
				.param("password", "password")
				.param("confirmPassword", "password")
				.param("email", "email")
				.param("roles","USER","MANAGER"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/admin"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(userRepository.existsByUsername("cata"));
		List<String> roles=new ArrayList<>(Arrays.asList("USER","MANAGER"));
		assertTrue(userRepository.findByUsername("cata").get().getRoles().containsAll(roles));
		
		
		Long createdUserId=userRepository.findByUsername("cata").get().getId();
		mockMvc.perform(post("/admin/delete")
				.param("id", createdUserId.toString()))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/admin"));
		
		assertFalse(userRepository.existsByUsername("cata"));
				
	}
	
	
	
}
