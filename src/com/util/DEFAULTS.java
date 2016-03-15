package com.util;

public enum DEFAULTS {
	
	FIRST_HALF_TIME(180),
	LIGHTENING_TIME(5);
	
	private final int time;

	private DEFAULTS(int time) {
		this.time = time;
	}
	
	public int getTime()
	{
		return this.time;
	}
	
}
