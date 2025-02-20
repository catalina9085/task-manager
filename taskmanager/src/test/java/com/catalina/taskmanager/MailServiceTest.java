package com.catalina.taskmanager;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.catalina.taskmanager.services.MailService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class MailServiceTest {
	
	@Autowired
	private MailService mailService;
	
	@MockBean
	private JavaMailSender mailSender;
	
	
	@Test
	public void sendEmailTest() {
		String to="test@gmail.com";
		String subject="test";
		String body="This is a test email";
		
		mailService.sendEmail(to, subject, body);
		
		SimpleMailMessage expectedMessage=new SimpleMailMessage();
		expectedMessage.setFrom("catalina_ionela7@yahoo.com");
		expectedMessage.setTo(to);
		expectedMessage.setSubject(subject);
		expectedMessage.setText(body);
		
		verify(mailSender).send(expectedMessage);
	}
	
	@Test
    public void testSendVerificationCode() {
        String code = "123456";
        String to = "test@gmail.com";

        mailService.sendVerificationCode(code, to);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(to);
        expectedMessage.setSubject("Verification code");
        expectedMessage.setText("This is your verification code: 123456");
        expectedMessage.setFrom("catalina_ionela7@yahoo.com");

        verify(mailSender).send(expectedMessage); 
    }

}
