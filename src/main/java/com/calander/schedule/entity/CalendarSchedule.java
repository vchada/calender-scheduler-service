package com.calander.schedule.entity;

import com.calander.schedule.beans.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "CALENDAR_SCHEDULE")
public class CalendarSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "RULE_IDS")
    private String RuleIds;

    @Column(name = "CREATED_DT_TM")
    @CreationTimestamp
    private Date createdDateAndTime;

    @Column(name = "CREATED_USER")
    private String createdUser;

    @Column(name = "LAST_MODIFIED_USR")
    private String lastModifiedUser;

    @Column(name = "LAST_MODIFIED_DT_TM")
    @UpdateTimestamp
    private Date lastModifiedDateAndTime;

    @Column(name = "IS_ACTIVE")
    @Enumerated(EnumType.STRING)
    private Status isActive;

    @Column(name = "RULES_INCLUDED")
    private String rulesIncluded;

    @Column(name = "RULES_EXCLUDED")
    private String rulesExcluded;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "YEAR")
    private String year;

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "DATA_SOURCE")
    private String dataSource;

    @Column(name = "INCLUDE_WEEKENDS")
    private String includeWeekends;
}
