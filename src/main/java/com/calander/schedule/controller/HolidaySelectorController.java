package com.calander.schedule.controller;

import java.time.LocalDate;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calander.schedule.beans.HolidayDateRequest;
import com.calander.schedule.service.HolidaySelectorService;

@RestController
@RequestMapping(value = "/holiday")
public class HolidaySelectorController {
	
	
	private final HolidaySelectorService holidaySelectorService;
	
	
	public HolidaySelectorController(final HolidaySelectorService holidaySelectorService) {
		this.holidaySelectorService = holidaySelectorService;
	}
	

	@PostMapping(value = "/find-holiday",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public LocalDate findHolidayDate(@RequestBody final HolidayDateRequest holidayDateRequest) {
		return holidaySelectorService.getHolidayDate(holidayDateRequest);
	}

}
