package com.calander.schedule.service;

import com.calander.schedule.beans.HolidayDateRequest;
import com.calander.schedule.beans.HolidayPersistRequest;
import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.RuleDefinition;
import com.calander.schedule.repo.RuleDefinitionRepo;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class HolidaySelectorService {

	private RuleDefinitionRepo ruleDefinitionRepo;

	public HolidaySelectorService(final RuleDefinitionRepo dateObservanceRepo) {
		this.ruleDefinitionRepo = dateObservanceRepo;
	}

	public LocalDate getHolidayDate(final HolidayDateRequest holidayDateRequest) {
		Optional<RuleDefinition> dateObservance = Optional
				.ofNullable(ruleDefinitionRepo.findByHolidayType(holidayDateRequest.getHolidayType()));
		if (dateObservance.isPresent()) {
			return dateOf(dateObservance.get(), holidayDateRequest.getYear());
		}
		return null;
	}

	public StatusResponse persistHoliday(final HolidayPersistRequest holidayPersistRequest) {
		if(Objects.nonNull(holidayPersistRequest)) {
			final RuleDefinition ruleDefinition = RuleDefinition.builder()
					.createdUser(holidayPersistRequest.getCreatedUser())
					.customDays(holidayPersistRequest.getCustomDays())
					.holidayType(holidayPersistRequest.getHolidayType())
					.dayOfTheMonth(holidayPersistRequest.getDayOfTheMonth())
					.dayOfTheWeek(holidayPersistRequest.getDayOfTheWeek())
					.isActive(holidayPersistRequest.isActive() ? 1 : 0)
					.month(holidayPersistRequest.getMonth())
					.weekOfTheMonth(holidayPersistRequest.getWeekOfTheMonth())
					.build();
			ruleDefinitionRepo.save(ruleDefinition);
			return StatusResponse.builder().message("HOLIDAY_PERSISTED_SUCCESSFULLY").build();
		}
		return StatusResponse.builder().message("HOLIDAY_PERSIST_FAILED").build();
	}


	public Map<String, LocalDate> fetchAllHolidays(final int year) {
		Map<String, LocalDate> holidayDateMap = new HashMap<>();
		final List<RuleDefinition> ruleDefinitionList = ruleDefinitionRepo.findByIsActive(1);
		if(null != ruleDefinitionList && !ruleDefinitionList.isEmpty()) {
			ruleDefinitionList.stream()
					.forEach(ruleDefinition ->
							holidayDateMap.put(ruleDefinition.getHolidayType(), dateOf(ruleDefinition, year)));
		}
		return holidayDateMap;
	}
	
	private LocalDate dateOf(final RuleDefinition dateObservance, final int year) {
		LocalDate localDate = null;
		if(dateObservance.getDayOfTheMonth() != 0) {
			localDate = LocalDate.of(year, dateObservance.getMonth(), dateObservance.getDayOfTheMonth());
		} else {
			localDate = LocalDate.now().withYear(year).withMonth(dateObservance.getMonth().getValue())
			.with(TemporalAdjusters.dayOfWeekInMonth(dateObservance.getWeekOfTheMonth(), dateObservance.getDayOfTheWeek()));
		}
		return adjustForWeekendsIfNecessary(localDate);
	}

	private LocalDate adjustForWeekendsIfNecessary(final LocalDate localDate) {
		final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
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
