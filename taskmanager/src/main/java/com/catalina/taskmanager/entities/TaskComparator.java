package com.catalina.taskmanager.entities;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;

public class TaskComparator implements Comparator<Task>{
	//negativ->t1 mai prioritar
	@Override
	public int compare(Task t1,Task t2) {
		if(t1.getDeadline()==null && t2.getDeadline()==null)
			return 0;
		
		if(t1.getDeadline()==null)
			return 1;
		
		if(t2.getDeadline()==null)
			return -1;
		
		Duration d1=Duration.between(LocalDateTime.now(),t1.getDeadline());
		Duration d2=Duration.between(LocalDateTime.now(),t2.getDeadline());
		
		if(d1.toDays()!=d2.toDays())
			return Long.compare(d1.toDays(),d2.toDays());
		
		if(d1.toHours()!=d2.toHours())
			return Long.compare(d1.toHours(),d2.toHours());
		
		if(d1.toMinutes()!=d2.toMinutes())
			return Long.compare(d1.toMinutes(),d2.toMinutes());
		
		return 0;
		
	}

}
