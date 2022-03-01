package com.calander.schedule.beans;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.Month;

@Data
public class HolidayPersistRequest {
    private String holidayType;
    private Month month;
    private int dayOfTheMonth;
    private DayOfWeek dayOfTheWeek;
    private int weekOfTheMonth;
    private String customDays;
    private String createdUser;
    private String lastModifiedUser;
    private boolean isActive;
}
