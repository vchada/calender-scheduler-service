package com.calander.schedule.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.calander.schedule.beans.HolidayDateRequest;
import com.calander.schedule.entity.DateObservance;
import com.calander.schedule.repo.DateObservanceRepo;

@Service
public class HolidaySelectorService {

	private DateObservanceRepo dateObservanceRepo;

	public HolidaySelectorService(final DateObservanceRepo dateObservanceRepo) {
		this.dateObservanceRepo = dateObservanceRepo;
	}

	public LocalDate getHolidayDate(final HolidayDateRequest holidayDateRequest) {
		Optional<DateObservance> dateObservance = Optional
				.ofNullable(dateObservanceRepo.findByHolidayType(holidayDateRequest.getHolidayType()));
		if (dateObservance.isPresent()) {
			return dateOf(dateObservance.get(), holidayDateRequest.getYear());
		}
		return null;
	}
	
	private LocalDate dateOf(DateObservance dateObservance, int year) {
		LocalDate localDate = null;
		if(dateObservance.getDayOfTheMonth() != 0) {
			localDate = LocalDate.of(year, dateObservance.getMonth(), dateObservance.getDayOfTheMonth());
		} else {
			localDate = LocalDate.now().withYear(year).withMonth(dateObservance.getMonth().getValue())
			.with(TemporalAdjusters.dayOfWeekInMonth(dateObservance.getWeekOfTheMonth(), dateObservance.getDayOfTheWeek()));
		}
		return adjustForWeekendsIfNecessary(localDate);
	}

	private LocalDate adjustForWeekendsIfNecessary(LocalDate localDate) {
		DayOfWeek dayOfWeek = localDate.getDayOfWeek();
		System.out.println(dayOfWeek);
		if(dayOfWeek != null && DayOfWeek.SATURDAY.equals(dayOfWeek)) {
			return localDate.minusDays(1);
		} else if(DayOfWeek.SUNDAY.equals(dayOfWeek)) {
			return localDate.plusDays(1);
		} else {
			return localDate;
		}
	}
	
//	public static void main(String[] args) {
//		DateObservance dateObservance = DateObservance.builder()
//				.holidayType("INDEPENDENCE_DAY")
//				.month(Month.JULY)
//				.dayOfTheMonth(4)
//				.dayOfTheWeek(null)
//				.weekOfTheMonth(0)
//				.build();
//		System.out.println(new HolidaySelectorService().dateOf(dateObservance, 2026));
//		
//	}

}
