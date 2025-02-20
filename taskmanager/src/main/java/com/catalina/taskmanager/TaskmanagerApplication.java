package com.catalina.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TaskmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}

}

/*
http://localhost:8080/ accesare pagina html online(username si parola in fisierul properties)
mvnw.cmd spring-boot:run pentru rulare in terminal
*/