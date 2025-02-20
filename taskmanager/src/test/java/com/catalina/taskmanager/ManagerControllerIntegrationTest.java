package com.catalina.taskmanager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ManagerControllerIntegrationTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserRepository userRepository;
	
	@BeforeEach
	public void setup() {
		userRepository.deleteAll();
		UserEntity user=new UserEntity("user@gmail.com","user","password",LocalDateTime.now());
		user.setRoles(Collections.singleton("MANAGER"));
		userRepository.save(user);
	}
	@Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Test
    public void testProfile() {
        System.out.println("Datasource URL: " + dataSourceUrl);
        Assertions.assertTrue(dataSourceUrl.contains("h2")); // sau ce valoare ar trebui să aibă pentru test
    }
	@Test
	@WithMockUser(roles="MANAGER")
	public void showManagerPageTest() throws Exception {
		mockMvc.perform(get("/manager"))
		.andExpect(status().isOk())
		.andExpect(model().attributeExists("users"));
	}
	
}
