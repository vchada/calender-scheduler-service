package com.calander.schedule.beans;

import lombok.Data;

@Data
public class HolidayPersistRequest {
    private String holidayType;
    private int month;
    private int dayOfTheMonth;
    private int dayOfTheWeek;
    private int weekOfTheMonth;
    private String customDays;
    private String createdUser;
    private String lastModifiedUser;
    private String isActive;
}
