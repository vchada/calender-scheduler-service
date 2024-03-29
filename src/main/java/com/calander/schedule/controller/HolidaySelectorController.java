package com.calander.schedule.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.calander.schedule.beans.*;
import com.calander.schedule.entity.CalendarSchedule;
import com.calander.schedule.entity.RuleDefinition;
import com.calander.schedule.service.CalenderScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.calander.schedule.service.HolidaySelectorService;

@RestController
@RequestMapping(value = "/holiday")
public class HolidaySelectorController {


    private final HolidaySelectorService holidaySelectorService;

    @Autowired
    CalenderScheduleService calenderScheduleService;


    public HolidaySelectorController(final HolidaySelectorService holidaySelectorService) {
        this.holidaySelectorService = holidaySelectorService;
    }


    @PostMapping(value = "/find-holiday",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<LocalDate> findHolidayDate(@RequestBody final HolidayDateRequest holidayDateRequest) {
        return holidaySelectorService.getHolidayDate(holidayDateRequest);
    }

    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse persistHoliday(@RequestBody final List<HolidayPersistRequest> holidayPersistRequest) {
        return holidaySelectorService.persistHoliday(holidayPersistRequest);
    }

    @GetMapping(value = "/get-all-holidays/{year}/{includeWeekends}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getAllHoliday(@PathVariable final int year,@PathVariable boolean includeWeekends) {
        return holidaySelectorService.fetchAllHolidays(year,includeWeekends);
    }
    @PostMapping(value = "/updateCalendar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse persistCalendar(@RequestBody final CalendarSchedule calenderScheduleRequest) {
        return calenderScheduleService.updateCalendarSchedule(calenderScheduleRequest);
    }
    @PostMapping(value = "/saveCalendar",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse persistCalendar(@RequestBody final CalenderScheduleRequest calenderScheduleRequest) {
        return calenderScheduleService.persistCalenderSchedule(calenderScheduleRequest);
    }

    @GetMapping(value = "/get-all-calendars/{year}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CalendarSchedule> getAllCalendars(@PathVariable final String year) {
        return calenderScheduleService.getAllCalendars(year);
    }

    @GetMapping(value = "/get-all-rules", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, List<RuleDefinition>> getAllRules()
    {
        return holidaySelectorService.fetchAllRules();
    }

    @GetMapping(value = "/get-rule-details-by-ruleId", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RuleDefinition> getAllRulesByRuleId(@RequestParam (value = "ruleId",required = false) String ruleName)
    {
        return holidaySelectorService.fetchAllRulesById(ruleName);
    }
    @GetMapping(value = "/get-all-existing-rule-names", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getAllRuleNames()
    {
        return holidaySelectorService.getAllRuleNames();
    }
    @GetMapping(value = "/get-all-existing-calendar-names", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<String> getAllCalendarNames()
    {
        return calenderScheduleService.getAllCalendarNames();
    }
    @PostMapping(value = "/update-rule",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public StatusResponse updateRuleDefinition(@RequestBody final List<RuleDefinition> ruleDefinition) {
        return holidaySelectorService.updateRules(ruleDefinition);
    }
    @GetMapping(value = "/get-rule-usage", produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean getRuleUsage(@RequestParam (value = "ruleId",required = false) String ruleName) {
        return calenderScheduleService.getRuleUsage(ruleName);
    }
    /// schedule all mondays not working
    // change db tables with FK style
    // update feature not working
}
