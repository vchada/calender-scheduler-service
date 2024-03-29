package com.calander.schedule.service;

import com.calander.schedule.beans.CalenderScheduleRequest;
import com.calander.schedule.beans.RulesRequest;
import com.calander.schedule.beans.Status;
import com.calander.schedule.beans.StatusResponse;
import com.calander.schedule.entity.CalendarSchedule;
import com.calander.schedule.entity.RuleDefinition;
import com.calander.schedule.repo.CalenderScheduleRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CalenderScheduleService {

    private final CalenderScheduleRepo calenderScheduleRepo;

    public CalenderScheduleService(final CalenderScheduleRepo calenderScheduleRepo) {
        this.calenderScheduleRepo = calenderScheduleRepo;
    }

    public StatusResponse persistCalenderSchedule(final CalenderScheduleRequest calenderScheduleRequest) {
        if(Objects.nonNull(calenderScheduleRequest)) {
            final CalendarSchedule calendarSchedule = CalendarSchedule.builder()
                    .createdUser(calenderScheduleRequest.getCreatedUser())
                    .name(calenderScheduleRequest.getName())
                    .RuleIds(calenderScheduleRequest.getRuleIds())
                    .isActive(calenderScheduleRequest.getIsActive())
                    .lastModifiedUser(calenderScheduleRequest.getLastModifiedUser())
                    .rulesIncluded(calenderScheduleRequest.getRulesIncluded())
                    .rulesExcluded(calenderScheduleRequest.getRulesExcluded())
                    .description(calenderScheduleRequest.getDescription())
                    .year(calenderScheduleRequest.getYear())
                    .displayName(calenderScheduleRequest.getDisplayName())
                    .dataSource(calenderScheduleRequest.getDataSource())
                    .includeWeekends(calenderScheduleRequest.getIncludeWeekends())
                    .build();
            calenderScheduleRepo.save(calendarSchedule);
            return StatusResponse.builder().message("CALENDER_PERSISTED_SUCCESSFULLY").build();

        }
        return StatusResponse.builder().message("CALENDER_PERSIST_FAILED").build();
    }
    @Transactional(rollbackFor = Exception.class)
    public StatusResponse updateCalendarSchedule(final CalendarSchedule calenderScheduleRequest) {
        calenderScheduleRepo.deleteByName(calenderScheduleRequest.getName());
        calenderScheduleRepo.save(calenderScheduleRequest);
        return StatusResponse.builder().message("CALENDAR UPDATED SUCCESSFULLY").build();
       /* if(searchRuleEntity.isPresent()) {
            calenderScheduleRepo.save(calenderScheduleRequest);
            return StatusResponse.builder().message("CALENDAR UPDATED SUCCESSFULLY").build();
        }
        return StatusResponse.builder().message("CALENDAR UPDATED SUCCESSFULLY").build();*/
    }
    public List<CalendarSchedule> getAllCalendars(String year)
    {
        return StreamSupport.stream(calenderScheduleRepo.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Set<String> getAllCalendarNames() {
        Set<String> retVal = new HashSet<>();
        for(CalendarSchedule calendarSchedule: calenderScheduleRepo.findAll())
        {
            retVal.add(calendarSchedule.getDisplayName());
            retVal.add(calendarSchedule.getName());
        }
        return retVal;
    }

    public boolean getRuleUsage(String rulesRequest)
    {
        for(CalendarSchedule calendarSchedule : getAllCalendars(String.valueOf(Calendar.getInstance().getWeekYear())))
        {
                if(calendarSchedule.getRulesIncluded().contains(rulesRequest)
                || calendarSchedule.getRulesExcluded().contains(rulesRequest))
                {
                    return true;
                }
        }
        return false;
    }
}
