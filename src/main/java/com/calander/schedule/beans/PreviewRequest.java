package com.calander.schedule.beans;

import lombok.Data;

import java.time.DayOfWeek;
import java.time.Month;

@Data
public class PreviewRequest {
    private int year;
    private Month month;
    private int dayOfTheMonth;
    private DayOfWeek dayOfTheWeek;
    private int weekOfTheMonth;
}
