package com.calander.schedule.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.calander.schedule.beans.DayOfTheWeekRequest;

@Service
public class DayOfTheWeekService {
	
	
	public List<LocalDate> findDatesByDayOfWeek(DayOfTheWeekRequest dayOfTheWeekRequest) {
		List<LocalDate> datesList = new ArrayList<>();
		LocalDate startDate = LocalDate.of(dayOfTheWeekRequest.getYear(), 01, 01);
		LocalDate endDate = LocalDate.of(Year.now().getValue(), 12, 31);
		LocalDate dayOfWeekDate = startDate.with(TemporalAdjusters.nextOrSame(dayOfTheWeekRequest.getDayOfWeek()));
		while(!dayOfWeekDate.isAfter(endDate)) {
			datesList.add( dayOfWeekDate );
		    dayOfWeekDate = dayOfWeekDate.plusWeeks(1);
		}
		return datesList;
	}

}
