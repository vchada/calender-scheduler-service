package com.calander.schedule.service;

import java.time.LocalDate;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import com.calander.schedule.beans.PreviewRequest;
import com.calander.schedule.entity.RuleDefinition;
import org.springframework.stereotype.Service;

import com.calander.schedule.beans.DayOfTheWeekRequest;

@Service
public class DayOfTheWeekService {
	
	
	public List<LocalDate> findDatesByDayOfWeek(final DayOfTheWeekRequest dayOfTheWeekRequest) {
		List<LocalDate> datesList = new ArrayList<>();
		final LocalDate startDate = LocalDate.of(dayOfTheWeekRequest.getYear(), 01, 01);
		final LocalDate endDate = LocalDate.of(Year.now().getValue(), 12, 31);
		LocalDate dayOfWeekDate = startDate.with(TemporalAdjusters.nextOrSame(dayOfTheWeekRequest.getDayOfWeek()));
		while(!dayOfWeekDate.isAfter(endDate)) {
			datesList.add( dayOfWeekDate );
		    dayOfWeekDate = dayOfWeekDate.plusWeeks(1);
		}
		return datesList;
	}

	public LocalDate previewDate(final PreviewRequest previewRequest) {
		if(previewRequest.getDayOfTheMonth() != 0) {
			return LocalDate.of(previewRequest.getYear(), previewRequest.getMonth(), previewRequest.getDayOfTheMonth());
		} else {
			return LocalDate.now().withYear(previewRequest.getYear()).withMonth(previewRequest.getMonth().getValue())
					.with(TemporalAdjusters.dayOfWeekInMonth(previewRequest.getWeekOfTheMonth(), previewRequest.getDayOfTheWeek()));
		}
	}

}
