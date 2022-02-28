package com.calander.schedule.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HolidayDateRequest {
	
	private int year;
	private String holidayType;

}
