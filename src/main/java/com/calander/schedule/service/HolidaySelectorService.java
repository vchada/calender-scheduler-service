package com.calander.schedule.service;

import com.calander.schedule.beans.HolidayDateRequest;
import com.calander.schedule.beans.HolidayPersistRequest;
import com.calander.schedule.beans.Status;
import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.RuleDefinition;
import com.calander.schedule.repo.RuleDefinitionRepo;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HolidaySelectorService {

	private RuleDefinitionRepo ruleDefinitionRepo;

	public HolidaySelectorService(final RuleDefinitionRepo dateObservanceRepo) {
		this.ruleDefinitionRepo = dateObservanceRepo;
	}

	public List<LocalDate> getHolidayDate(final HolidayDateRequest holidayDateRequest) {
		Optional<List<RuleDefinition>> dateObservance = Optional
				.ofNullable(ruleDefinitionRepo.findByHolidayType(holidayDateRequest.getHolidayType()));
		if (dateObservance.isPresent()) {
			return dateObservance.get().stream().map(request -> dateOf(request, holidayDateRequest.getYear())).collect(Collectors.toList());
		}
		return null;
	}

	public StatusResponse persistHoliday(final List<HolidayPersistRequest> holidayPersistRequests) {
		if (Objects.nonNull(holidayPersistRequests) && !holidayPersistRequests.isEmpty()) {
			final List<RuleDefinition> ruleDefinitions = holidayPersistRequests.stream()
					.map(holidayPersistRequest -> RuleDefinition.builder()
					.createdUser(holidayPersistRequest.getCreatedUser())
					.customDays(holidayPersistRequest.getCustomDays())
					.holidayType(holidayPersistRequest.getHolidayType())
					.dayOfTheMonth(holidayPersistRequest.getDayOfTheMonth())
					.dayOfTheWeek(holidayPersistRequest.getDayOfTheWeek() <= 0 ? null : DayOfWeek.of(holidayPersistRequest.getDayOfTheWeek()))
					.isActive(holidayPersistRequest.getIsActive())
					.month(holidayPersistRequest.getMonth() <= 0 ? null : Month.of(holidayPersistRequest.getMonth()))
					.weekOfTheMonth(holidayPersistRequest.getWeekOfTheMonth())
					.description(holidayPersistRequest.getDescription())
					.build()).collect(Collectors.toList());
			ruleDefinitionRepo.saveAll(ruleDefinitions);
			return StatusResponse.builder().message("HOLIDAY_PERSISTED_SUCCESSFULLY").build();
		}
		return StatusResponse.builder().message("HOLIDAY_PERSIST_FAILED").build();
	}

	public Map<String, List<RuleDefinition>> fetchAllRules()
	{
		List<RuleDefinition> ruleDefinitionList = (List<RuleDefinition>) ruleDefinitionRepo.findAll();
		return ruleDefinitionList.stream().collect(Collectors.groupingBy(RuleDefinition::getHolidayType));
	}

	public Map<String, String> fetchAllHolidays(final int year) {
		Map<String, String> holidayDateMap = new HashMap<>();
		final List<RuleDefinition> ruleDefinitionList = ruleDefinitionRepo.findByIsActive(Status.ACTIVE);
		if(null != ruleDefinitionList && !ruleDefinitionList.isEmpty()) {
			Map<String, List<RuleDefinition>> ruleDefinitionMap = ruleDefinitionList.stream().collect(Collectors.groupingBy(RuleDefinition::getHolidayType));

			for(Map.Entry<String, List<RuleDefinition>> mapList: ruleDefinitionMap.entrySet()) {
				holidayDateMap.put(mapList.getKey(), mapList.getValue().stream()
						.map(rulesDefinition -> this.dateOf(rulesDefinition, year))
						.filter(Objects::nonNull)
						.map(LocalDate::toString)
						.map(date -> date.substring(date.indexOf("-") + 1))
						.collect(Collectors.joining(",")));

			}
//			ruleDefinitionList.stream()
//					.filter(ruleDefinition -> Objects.isNull(ruleDefinition.getCustomDays()) || ruleDefinition.getCustomDays().isEmpty())
//					.forEach(ruleDefinition ->
//							holidayDateMap.put(ruleDefinition.getHolidayType(), dateOf(ruleDefinition, year).toString()));
//			ruleDefinitionList.stream()
//					.filter(ruleDefinition -> Objects.nonNull(ruleDefinition.getCustomDays()) && !ruleDefinition.getCustomDays().isEmpty())
//					.forEach(ruleDefinition -> {
//						List<String> updatedDates = new ArrayList<>();
//						for(String customDate : ruleDefinition.getCustomDays().split(",")) {
//							final String[] customDateValues = customDate.split("-");
//							LocalDate date = LocalDate.of(year,
//									Month.of(Integer.parseInt(customDateValues[0])), Integer.parseInt(customDateValues[1]));
//							updatedDates.add(date.toString());
//						}
//						holidayDateMap.put(ruleDefinition.getHolidayType(), String.join(",", updatedDates));
//					});
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

	public List<RuleDefinition> fetchAllRulesById(String ruleName)
	{
		return ruleDefinitionRepo.findByHolidayType(ruleName);
	}

	@Transactional(rollbackFor = Exception.class)
	public StatusResponse updateRules(List<RuleDefinition> ruleDefinitions)
	{
		/*DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			java.sql.Date today = (java.sql.Date) new Date();*/

		String holidayType = Optional.ofNullable(ruleDefinitions)
				.map(list -> list.get(0))
				.map(RuleDefinition::getHolidayType)
				.orElse(null);
		ruleDefinitionRepo.deleteByHolidayType(holidayType);
		ruleDefinitions.stream().map(ruleDefinition -> {
			ruleDefinition.setId(null);
			ruleDefinition.setLastModifiedUser("User");
			return ruleDefinition;
		}).forEach(ruleDefinitionRepo::save);

//		boolean isValidRequest = true;
//		for(RuleDefinition ruleDefinition: ruleDefinitions) {
//			isValidRequest = Optional.ofNullable(ruleDefinitionRepo.findById(ruleDefinition.getId()))
//					.map(ruleDefinitionFromDb -> Boolean.TRUE).orElse(Boolean.FALSE);
//			if(!isValidRequest) {
//				break;
//			}
//		}
//		if(isValidRequest) {
//			ruleDefinitionRepo.saveAll(ruleDefinitions);
//			return StatusResponse.builder().message("HOLIDAY_UPDATED_SUCCESSFULLY").build();
////			RuleDefinition ruleDefinition1 = searchRuleEntity.get();
////
////			ruleDefinition1.setCreatedUser(ruleDefinition.getCreatedUser());
////			ruleDefinition1.setCustomDays(ruleDefinition.getCustomDays());
////			ruleDefinition1.setHolidayType(ruleDefinition.getHolidayType());
////			ruleDefinition1.setDayOfTheMonth(ruleDefinition.getDayOfTheMonth());
////			ruleDefinition1.setDayOfTheWeek(!StringUtils.isEmpty(ruleDefinition.getDayOfTheWeek())?  ruleDefinition.getDayOfTheWeek():null);
////			ruleDefinition1.setIsActive("true".equals(ruleDefinition.getIsActive()) ? 1 : 0);
////			ruleDefinition1.setMonth(!StringUtils.isEmpty(ruleDefinition.getMonth()) ? ruleDefinition.getMonth():null);
////			ruleDefinition1.setWeekOfTheMonth(ruleDefinition.getWeekOfTheMonth());
////
////			DateFormat df = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
////			java.sql.Date today = (java.sql.Date) new Date();
////			ruleDefinition1.setLastModifiedDateAndTime(today);
////
////			ruleDefinitionRepo.save(ruleDefinition1)
//		}
		return StatusResponse.builder().message("HOLIDAY_UPDATED_SUCCESSFULLY").build();
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
