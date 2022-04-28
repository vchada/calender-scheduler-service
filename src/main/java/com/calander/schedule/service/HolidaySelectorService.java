package com.calander.schedule.service;

import com.calander.schedule.beans.HolidayDateRequest;
import com.calander.schedule.beans.HolidayPersistRequest;
import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.RuleDefinition;
import com.calander.schedule.repo.RuleDefinitionRepo;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

	public List<RuleDefinition> fetchAllRules()
	{
		return (List<RuleDefinition>) ruleDefinitionRepo.findAll();
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

	public RuleDefinition fetchAllRulesById(int id)
	{
		return ruleDefinitionRepo.findById(id).get();
	}

	public StatusResponse updateRules(RuleDefinition ruleDefinition)
	{
		Optional<RuleDefinition> searchRuleEntity = ruleDefinitionRepo.findById(ruleDefinition.getId());
		if(searchRuleEntity.isPresent()) {
			RuleDefinition ruleDefinition1 = searchRuleEntity.get();

			ruleDefinition1.setCreatedUser(ruleDefinition.getCreatedUser());
			ruleDefinition1.setCustomDays(ruleDefinition.getCustomDays());
			ruleDefinition1.setHolidayType(ruleDefinition.getHolidayType());
			ruleDefinition1.setDayOfTheMonth(ruleDefinition.getDayOfTheMonth());
			ruleDefinition1.setDayOfTheWeek(!StringUtils.isEmpty(ruleDefinition.getDayOfTheWeek())?  ruleDefinition.getDayOfTheWeek():null);
			ruleDefinition1.setIsActive("true".equals(ruleDefinition.getIsActive()) ? 1 : 0);
			ruleDefinition1.setMonth(!StringUtils.isEmpty(ruleDefinition.getMonth()) ? ruleDefinition.getMonth():null);
			ruleDefinition1.setWeekOfTheMonth(ruleDefinition.getWeekOfTheMonth());

			DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			java.sql.Date today = (java.sql.Date) new Date();
			ruleDefinition1.setLastModifiedDateAndTime(today);

			ruleDefinitionRepo.save(ruleDefinition1);
			return StatusResponse.builder().message("HOLIDAY_UPDATED_SUCCESSFULLY").build();
		}
		return StatusResponse.builder().message("HOLIDAY_UPDATE_FAILED").build();
	}

	/*public RuleDefinition fetchRuleDefinition(RuleDefinition ruleDefinition,int year)
	{
		dateOf(ruleDefinition,year);

	}*/

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
