package com.calander.schedule.service;

import com.calander.schedule.beans.DayOfTheWeekRequest;
import com.calander.schedule.beans.NumberOfWeeksRequest;
import com.calander.schedule.beans.PreviewRequest;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.*;
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
			if(previewRequest.getWeekOfTheMonth() == -1) {
				LocalDate localDate = LocalDate.of(previewRequest.getYear(), Month.of(previewRequest.getMonth()), 1);

					return localDate.with(TemporalAdjusters.lastInMonth(DayOfWeek.of(previewRequest.getDayOfTheWeek())));
			} else {
				if (previewRequest.getDayOfTheMonth() != 0) {

						return LocalDate.of(previewRequest.getYear(), Month.of(previewRequest.getMonth()), previewRequest.getDayOfTheMonth());
				} else {
					LocalDate date = LocalDate.now()
							.withYear(previewRequest.getYear())
							.withMonth(Month.of(previewRequest.getMonth()).getValue())
							.with(TemporalAdjusters.dayOfWeekInMonth(previewRequest.getWeekOfTheMonth(), DayOfWeek.of(previewRequest.getDayOfTheWeek())));


					return date.getMonth().getValue() == previewRequest.getMonth() ? date : null;
				}
			}
		}).filter(Objects::nonNull).sorted().collect(Collectors.toList());
	}


	public Integer getMaximumNumberWeek(final NumberOfWeeksRequest numberOfWeeksRequest) {
		return numberOfWeeksRequest.getMonths().stream().map(month -> {
			YearMonth yearMonth = YearMonth.of(numberOfWeeksRequest.getYear(), 1);
			return yearMonth.atEndOfMonth().get(WeekFields.ISO.weekOfMonth());
		}).sorted(Comparator.reverseOrder()).findFirst().get();
	}

	/*private LocalDate adjustForWeekendsIfNecessary(final LocalDate localDate) {
		final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		if(dayOfWeek != null && DayOfWeek.SATURDAY.equals(dayOfWeek)) {
			return localDate.minusDays(1);
		} else if(DayOfWeek.SUNDAY.equals(dayOfWeek)) {
			return localDate.plusDays(1);
		} else {
			return localDate;
		}
	}*/
}
