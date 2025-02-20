package com.catalina.taskmanager;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.catalina.taskmanager.repositories.UserRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerIntegrationTest {
	@Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository; //baza de date reala,cea din "test"


    @BeforeEach
    public void setup() {
        userRepository.deleteAll(); 
    }

    @Test
    void testRegisterUserAndLogin() throws Exception {
    	//1.register
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "cata")
                        .param("email", "catalina@yahoo.com")
                        .param("password", "cata")
                        .param("confirmPassword", "cata"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register"))
                .andExpect(flash().attributeExists("email"));

        //2.verificare baza de date UserRepository
        assertTrue(userRepository.existsByUsername("cata"));
        assertTrue(userRepository.existsByEmail("catalina@yahoo.com"));

        //3/logare
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "cata")
                        .param("password", "cata"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/main"));
    }
}
