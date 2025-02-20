package com.catalina.taskmanager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalina.taskmanager.entities.Task;
import com.catalina.taskmanager.entities.UserEntity;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long>{
	List<Task> findByUser(UserEntity userEntity);
	List<Task> findByUserAndCategory(UserEntity user,String category);
	
	boolean existsByTitle(String title);
	
	void deleteById(Long id);
	
	Optional<Task> findById(Long id);
	Optional<Task> findByTitle(String title);
}
