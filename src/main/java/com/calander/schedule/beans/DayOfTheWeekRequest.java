package com.calander.schedule.beans;

import java.time.DayOfWeek;

import lombok.Data;

@Data
public class DayOfTheWeekRequest {
	
	private int year;
	private DayOfWeek dayOfWeek;

}
