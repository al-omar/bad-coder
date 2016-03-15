package com.util;

import java.util.ArrayList;
import java.util.List;

import com.dto.Task;

public class CTMAlgo {

	static List<Task> maxList =null;
	static int maxUsed = 0;
	static boolean flag = false;

	/*
	 * Get list of tasks that can consume 180 minutes before lunch
	 * 
	 * 
	 */
	public static List<Task> findBestFit(List<Task> tasks) {

		maxList = new ArrayList<Task>();
		maxUsed = 0;
		flag = false;
		List<Task> partial = new ArrayList<Task>();
		int totalTime = DEFAULTS.FIRST_HALF_TIME.getTime();
		find(tasks, partial, totalTime);
		return maxList;
	}
	
	/*
	 * Recursively traverse all combination of tasks,
	 * till we find a set of tasks whose time sum up to 180 minutes
	 * 
	 */

	private static void find(List<Task> tasks, List<Task> partial, int sum) {
		int total = 0;
		if (flag)
			return;
		for (Task t : partial) {
			total += t.getDurationMin();
		}
		if (total > DEFAULTS.FIRST_HALF_TIME.getTime()) {
			return;
		}
		if (total == DEFAULTS.FIRST_HALF_TIME.getTime()) {
			maxList = partial;
			flag = true;
			return;
		}
		if (total > maxUsed) {
			maxList = partial;
			maxUsed = total;
		}
		for (int i = 0; i < tasks.size(); i++) {
			Task n = tasks.get(i);
			List<Task> remain = new ArrayList<Task>();
			for (int j = i + 1; j < tasks.size(); j++) {
				remain.add(tasks.get(j));
			}
			List<Task> partial_recursive = new ArrayList<Task>(partial);
			partial_recursive.add(n);
			find(remain, partial_recursive, sum);
		}

	}
}
