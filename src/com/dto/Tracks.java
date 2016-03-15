package com.dto;

import java.util.ArrayList;
import java.util.List;

public class Tracks {

	private List<Task> trackTasks=null;
	private Integer trackId;
	private Integer consumedTime=0;
	

	public Integer getConsumedTime() {
		return consumedTime;
	}

	public void setConsumedTime(Integer consumedTime) {
		this.consumedTime = consumedTime;
	}

	public List<Task> getTrackTasks() {
		return trackTasks;
	}

	public void setTrackTasks(List<Task> trackTasks) {
		this.trackTasks = trackTasks;
		
	}

	public Tracks() {
		trackTasks = new ArrayList<Task>();
	}

	public Integer getTrackId() {
		return trackId;
	}

	public void setTrackId(Integer trackId) {
		this.trackId = trackId;
	}
	public void addToTrack(Task task){
		this.trackTasks.add(task);
	}
	
}
