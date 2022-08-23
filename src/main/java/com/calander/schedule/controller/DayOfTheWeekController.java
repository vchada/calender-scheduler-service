package com.calander.schedule.controller;

import java.time.LocalDate;
import java.util.List;

import com.calander.schedule.beans.NumberOfWeeksRequest;
import com.calander.schedule.beans.PreviewRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calander.schedule.beans.DayOfTheWeekRequest;
import com.calander.schedule.service.DayOfTheWeekService;

@RestController
@RequestMapping(value = "/dates")
public class DayOfTheWeekController {
	
	
	private final DayOfTheWeekService dayOfTheWeekService;
	
	
	
	public DayOfTheWeekController(final DayOfTheWeekService dayOfTheWeekService) {
		this.dayOfTheWeekService = dayOfTheWeekService;
	}



	@PostMapping(value = "/by-day-of-week")
	public List<LocalDate> getDayOfTheWeekByYear(@RequestBody DayOfTheWeekRequest dayOfTheWeekRequest) {
		return dayOfTheWeekService.findDatesByDayOfWeek(dayOfTheWeekRequest);
		
	}

	@PostMapping(value = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<LocalDate> getPreviewDate(@RequestBody List<PreviewRequest> previewRequest) {
		return dayOfTheWeekService.previewDate(previewRequest);
	}

	@PostMapping(value = "/get-max-week", consumes = MediaType.APPLICATION_JSON_VALUE)
	public Integer getMaximumWeeksInMonth(@RequestBody NumberOfWeeksRequest numberOfWeeksRequest) {
		return dayOfTheWeekService.getMaximumNumberWeek(numberOfWeeksRequest);
	}

	

}
