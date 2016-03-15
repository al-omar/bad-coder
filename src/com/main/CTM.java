package com.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import com.dto.Task;
import com.dto.Tracks;
import com.util.CTMUtil;

public class CTM {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader("input.txt"));
		List<String> strTaskList = new ArrayList<String>();
		String currentRead = "";
		while ((currentRead = br.readLine()) != null) {
			strTaskList.add(currentRead);
		}
		// Create list of tasks
		List<Task> taskList = CTMUtil.processInput(strTaskList);
		//find number of tracks required
		int tracksNum = CTMUtil.findTracksCount(taskList);
		List<Task> sortedTaskList = CTMUtil.sortByTime(taskList);

		// fill before lunch slot in most efficient way
		List<Tracks> tracks = CTMUtil.findKBusySchedules(sortedTaskList, tracksNum);
		// fill remaining slots in largest remaining time way
		tracks = CTMUtil.fillRemainingSlots(tracks, sortedTaskList);

		List<String> eventList = CTMUtil.prepareResult(tracks);

		for (String s : eventList) {
			System.out.println(s);
		}
		br.close();
	}

}
