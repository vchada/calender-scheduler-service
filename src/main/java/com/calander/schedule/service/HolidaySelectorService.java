package com.calander.schedule.service;

import com.calander.schedule.beans.HolidayDateRequest;
import com.calander.schedule.beans.HolidayPersistRequest;
import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.RuleDefinition;
import com.calander.schedule.repo.RuleDefinitionRepo;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
					.dayOfTheWeek(holidayPersistRequest.getDayOfTheWeek() <= 0 ? null : DayOfWeek.of(holidayPersistRequest.getDayOfTheWeek()))
					.isActive("true".equals(holidayPersistRequest.getIsActive()) ? 1 : 0)
					.month(holidayPersistRequest.getMonth() <= 0 ? null : Month.of(holidayPersistRequest.getMonth()))
					.weekOfTheMonth(holidayPersistRequest.getWeekOfTheMonth())
					.build();
			ruleDefinitionRepo.save(ruleDefinition);
			return StatusResponse.builder().message("HOLIDAY_PERSISTED_SUCCESSFULLY").build();
		}
		return StatusResponse.builder().message("HOLIDAY_PERSIST_FAILED").build();
	}


	public Map<String, String> fetchAllHolidays(final int year) {
		Map<String, String> holidayDateMap = new HashMap<>();
		final List<RuleDefinition> ruleDefinitionList = ruleDefinitionRepo.findByIsActive(1);
		if(null != ruleDefinitionList && !ruleDefinitionList.isEmpty()) {
			ruleDefinitionList.stream()
					.filter(ruleDefinition -> Objects.isNull(ruleDefinition.getCustomDays()) || ruleDefinition.getCustomDays().isEmpty())
					.forEach(ruleDefinition ->
							holidayDateMap.put(ruleDefinition.getHolidayType(), dateOf(ruleDefinition, year).toString()));
			ruleDefinitionList.stream()
					.filter(ruleDefinition -> Objects.nonNull(ruleDefinition.getCustomDays()) && !ruleDefinition.getCustomDays().isEmpty())
					.forEach(ruleDefinition -> {
						List<String> updatedDates = new ArrayList<>();
						for(String customDate : ruleDefinition.getCustomDays().split(",")) {
							final String[] customDateValues = customDate.split("-");
							LocalDate date = LocalDate.of(year,
									Month.of(Integer.parseInt(customDateValues[0])), Integer.parseInt(customDateValues[1]));
							updatedDates.add(date.toString());
						}
						holidayDateMap.put(ruleDefinition.getHolidayType(), String.join(",", updatedDates));
					});
		}
		return holidayDateMap;
	}
	
	private LocalDate dateOf(final RuleDefinition ruleDefinition, final int year) {
		if(ruleDefinition.getDayOfTheMonth() != 0) {
			return adjustForWeekendsIfNecessary(LocalDate.of(year, ruleDefinition.getMonth(), ruleDefinition.getDayOfTheMonth()));
		} else {
			return adjustForWeekendsIfNecessary(LocalDate.now().withYear(year).withMonth(ruleDefinition.getMonth().getValue())
			.with(TemporalAdjusters.dayOfWeekInMonth(ruleDefinition.getWeekOfTheMonth(), ruleDefinition.getDayOfTheWeek())));
		}
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
