package com.calander.schedule.beans;

import lombok.Data;

@Data
public class CalenderScheduleRequest {
    private String name;
    private String RuleIds;
    private String createdUser;
    private String lastModifiedUser;
    private Status isActive;
    private String rulesIncluded;
    private String rulesExcluded;
    private String description;
    private String year;
    private String displayName;
    private String dataSource;
    private String includeWeekends;
}
