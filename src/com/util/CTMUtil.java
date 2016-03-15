package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dto.Task;
import com.dto.Tracks;

public class CTMUtil {

	/*
	 * Read each record and create task object for each line of input
	 * 
	 * method -> processInput
	 * 
	 */
	public static List<Task> processInput(List<String> strTaskList) {
		List<Task> resultList = new ArrayList<Task>();

		String pattern = "\\d+";
		Pattern r = Pattern.compile(pattern);
		int index = 1;
		String title = "";
		int time = 0;
		for (String s : strTaskList) {
			Matcher m = r.matcher(s);
			if (m.find()) {
				time = Integer.parseInt(m.group());
				title = s.substring(0, s.indexOf(m.group())).trim();
			} else {
				time = DEFAULTS.LIGHTENING_TIME.getTime();
				title = s.substring(0, s.lastIndexOf("lightning")).trim();
			}
			Task task = new Task(index, title, time);
			index++;
			resultList.add(task);
		}
		return resultList;
	}

	/*
	 * For each track in a day out of K tracks, fill 9AM-12PM slot with tasks in
	 * best way of time utilization
	 * 
	 */
	public static List<Tracks> findKBusySchedules(List<Task> tasks, int tracksNum) {
		List<Tracks> finalTracks = new ArrayList<Tracks>();
		for (int i = 0; i < tracksNum; i++) {
			Tracks tracks = new Tracks();
			tracks.setTrackId(i + 1);
			List<Task> bestFit = CTMAlgo.findBestFit(tasks);
			tracks.setTrackTasks(bestFit);
			tasks = remove(tasks, bestFit);
			for (Task t : bestFit) {
				tracks.setConsumedTime(tracks.getConsumedTime() + t.getDurationMin());
			}
			finalTracks.add(tracks);

		}

		return finalTracks;
	}

	/*
	 * Separate a subset list of task from master lists of tasks
	 * 
	 * 
	 */
	public static List<Task> remove(List<Task> tasks, List<Task> tobeRemovedTasks) {
		int size = tobeRemovedTasks.size();
		for (Task rt : tobeRemovedTasks) {
			if (size == 0)
				break;
			Iterator<Task> it = tasks.iterator();
			while (it.hasNext()) {
				Task t = it.next();
				if (t.getTaskId() == rt.getTaskId())
					it.remove();
			}
			size--;
		}
		return tasks;
	}

	/*
	 * After All tracks have their 9AM-12PM slot filled, Fill remaining tasks
	 * into tracks having maximum free time available
	 * 
	 */
	public static List<Tracks> fillRemainingSlots(List<Tracks> tracks, List<Task> tasks) {
		for (Tracks tr : tracks) {
			List<Task> assignedTasks = tr.getTrackTasks();
			tasks = remove(tasks, assignedTasks);
		}
		Iterator<Task> it = tasks.iterator();

		while (it.hasNext()) {
			Task pendingTask = it.next();
			Tracks tr = getTrackToFill(tracks, pendingTask);
			tr.addToTrack(pendingTask);
			tr.setConsumedTime(tr.getConsumedTime() + pendingTask.getDurationMin());
			it.remove();
		}
		return tracks;
	}

	/*
	 * Out of available tracks, find track with maximum free time available
	 * 
	 * 
	 */
	private static Tracks getTrackToFill(List<Tracks> tracks, Task task) {

		Integer min = Integer.MAX_VALUE;
		Integer shortestTrack = 0;
		for (int i = 0; i < tracks.size(); i++) {
			if (tracks.get(i).getConsumedTime() < min) {
				min = tracks.get(i).getConsumedTime();
				shortestTrack = i;
			}
		}
		return tracks.get(shortestTrack);
	}

	/*
	 * After all tracks are scheduled, Create final print List
	 * 
	 */
	public static List<String> prepareResult(List<Tracks> tracks) throws ParseException {
		List<String> lastTrack = new ArrayList<String>();
		List<String> output = new ArrayList<String>();
		for (Tracks tr : tracks) {
			output.add("Track " + tr.getTrackId() + ":");
			List<Task> tasks = tr.getTrackTasks();
			String startTime = "09:00AM";
			int timeConsumed = 0;
			for (Task t : tasks) {
				if (t.getDurationMin() != 5) {
					output.add(startTime + " " + t.getTaskTitle() + " " + t.getDurationMin() + "min");
				} else {
					output.add(startTime + " " + t.getTaskTitle() + " lightning");
				}
				timeConsumed += t.getDurationMin();
				if (timeConsumed == DEFAULTS.FIRST_HALF_TIME.getTime()) {
					output.add("12:00PM Lunch");
					startTime = getNewTime(startTime, t.getDurationMin());
					startTime = getNewTime(startTime, 60);
				} else {
					startTime = getNewTime(startTime, t.getDurationMin());
				}
			}
			lastTrack.add(startTime);
			output.add("##:##PM Networking Event");
			output.add("\n");
		}
		String networkingTime = findLastEvent(lastTrack);
		output = setNetworkingTime(output, networkingTime);
		return output;
	}

	/*
	 * Add Minutes to old time to get new time in "hh:mm a" format
	 * 
	 * 
	 */

	private static String getNewTime(String oldTime, int timeConsumedInMin) throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("hh:mma");
		Date d = df.parse(oldTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, timeConsumedInMin);
		String newTime = df.format(cal.getTime());
		return newTime;
	}

	/*
	 * Find when can we start Networking event
	 * 
	 * 
	 */
	private static String findLastEvent(List<String> timeList) throws ParseException {
		String maxTime = "04:00PM";
		SimpleDateFormat df = new SimpleDateFormat("hh:mma");
		Date maxd = df.parse(maxTime);

		for (String s : timeList) {

			Date d = df.parse(s);
			if (d.compareTo(maxd) == 1) {
				maxd = d;
			}

		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(maxd);

		String newTime = df.format(cal.getTime());
		return newTime;
	}

	public static List<String> setNetworkingTime(List<String> events, String networkingTime) {

		ListIterator<String> it = events.listIterator();
		while (it.hasNext()) {
			String currentRow = it.next();
			if (currentRow.indexOf("##:##PM") == 0) {
				it.set(networkingTime + " Networking Event");

			}
		}
		return events;
	}

	/*
	 * Number of tracks to be used to complete all tasks in a day We have 7
	 * Hours available at max.
	 * 
	 */
	public static int findTracksCount(List<Task> tasks) {
		int totalTimeNeeded = 0;
		for (Task t : tasks) {
			totalTimeNeeded += t.getDurationMin();
		}
		int result = (totalTimeNeeded / (60 * 7)) + 1;
		return result;
	}

	public static List<Task> sortByTime(List<Task> tasks) {

		Collections.sort(tasks, Task.DURATION_SORT);
		return tasks;
	}

}
