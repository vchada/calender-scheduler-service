package com.calander.schedule.beans;

import lombok.Data;

@Data
public class PreviewRequest {
    private int year;
    private int month;
    private int dayOfTheMonth;
    private int dayOfTheWeek;
    private int weekOfTheMonth;
}
