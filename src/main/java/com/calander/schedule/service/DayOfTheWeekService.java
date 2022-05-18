package com.calander.schedule.service;

import com.calander.schedule.beans.DayOfTheWeekRequest;
import com.calander.schedule.beans.PreviewRequest;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

	public List<LocalDate> previewDate(final List<PreviewRequest> previewRequests) {
		return previewRequests.stream().map(previewRequest -> {
			if(previewRequest.getDayOfTheMonth() != 0) {
				return LocalDate.of(previewRequest.getYear(), previewRequest.getMonth(), previewRequest.getDayOfTheMonth());
			} else {
				return LocalDate.now().withYear(previewRequest.getYear()).withMonth(Month.of(previewRequest.getMonth()).getValue())
						.with(TemporalAdjusters.dayOfWeekInMonth(previewRequest.getWeekOfTheMonth(), DayOfWeek.of(previewRequest.getDayOfTheWeek())));
			}
		}).collect(Collectors.toList());
	}

}
