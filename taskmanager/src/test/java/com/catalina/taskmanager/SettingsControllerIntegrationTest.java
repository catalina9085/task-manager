package com.catalina.taskmanager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class SettingsControllerIntegrationTest {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@BeforeEach
	public void setup() {
		userRepository.deleteAll();
		UserEntity user=new UserEntity("cata@yahoo.com","cata",encoder.encode("parola"),LocalDateTime.now());
		userRepository.save(user);
	}
	
	@Test
	@WithMockUser(username="cata")
	public void showSettingsTest() throws Exception {
		mockMvc.perform(get("/settings"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("backgroundColor"))
		.andExpect(model().attributeExists("verified"))
		.andExpect(view().name("settings"));
	}
	
	@Test
	@WithMockUser(username="cata")
	public void turnOffNotifTest() throws Exception {
		mockMvc.perform(post("/settings/turnOffNotif"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(!(userRepository.findAll().get(0).isEnableNotifications()));
	}
	
	@Test
	@WithMockUser(username="cata")
	public void turnOnNotifTest() throws Exception {
		mockMvc.perform(post("/settings/turnOnNotif"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(userRepository.findAll().get(0).isEnableNotifications());
	}
	
	@Test
	@WithMockUser(username="cata")
	public void enableTimePriorityTest() throws Exception {
		mockMvc.perform(post("/settings/enableTimePriority"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(userRepository.findAll().get(0).isEnableTimePriority());
	}
	
	@Test
	@WithMockUser(username="cata")
	public void enableAndDisbleTimePriorityTest() throws Exception {
		mockMvc.perform(post("/settings/disableTimePriority"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(!(userRepository.findAll().get(0).isEnableTimePriority()));
		
		mockMvc.perform(post("/settings/enableTimePriority"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(userRepository.findAll().get(0).isEnableTimePriority());
	}
	
	@Test
	@WithMockUser(username="cata")
	public void enableAndDisableDeleteAfterCheckTest() throws Exception {
		mockMvc.perform(post("/settings/enableDeleteAfterCheck"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(userRepository.findAll().get(0).isEnableDeleteAfterCheck());
		
		mockMvc.perform(post("/settings/disableDeleteAfterCheck"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(!(userRepository.findAll().get(0).isEnableDeleteAfterCheck()));
		
	}
	
	@Test
	@WithMockUser(username="cata")
	public void changeBackgroundColorTest() throws Exception {
		mockMvc.perform(post("/settings/color")
				.param("backgroundColor", "red"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
		
		assertTrue(userRepository.findAll().get(0).getBackgroundColor().equals("red"));
		
	}
	
	@Test
	@WithMockUser(username="cata")
	public void changePasswordTest() throws Exception {
		mockMvc.perform(post("/settings/changePassword")
				.param("currentPassword", "parola")
				.param("newPassword", "parola2")
				.param("confirmPassword", "parola2"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/settings"))
		.andExpect(flash().attributeExists("message"));
	}
	
	
}
