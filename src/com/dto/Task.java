package com.dto;

import java.util.Comparator;

public class Task {
private int taskId;
private String taskTitle;
private Integer durationMin;
public int getTaskId() {
	return taskId;
}
public void setTaskId(int taskId) {
	this.taskId = taskId;
}
public String getTaskTitle() {
	return taskTitle;
}
public void setTaskTitle(String taskTitle) {
	this.taskTitle = taskTitle;
}
public Integer getDurationMin() {
	return durationMin;
}
public void setDurationMin(Integer durationMin) {
	this.durationMin = durationMin;
}
public Task(int taskId, String taskTitle, Integer durationMin) {
	super();
	this.taskId = taskId;
	this.taskTitle = taskTitle;
	this.durationMin = durationMin;
}

public String toString()
{
	return "taskId:"+this.taskId+" task title:"+this.taskTitle+" tasktime:"+this.durationMin;
}
public static final Comparator<Task> DURATION_SORT = new Comparator<Task>() {
	public int compare(Task t1, Task t2) {
		return t2.getDurationMin().compareTo(t1.getDurationMin());
	}
};
}
